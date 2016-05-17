/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.reports;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.devgateway.toolkit.forms.util.FolderContentResource;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.styles.BlockUiReportsJavaScript;
import org.devgateway.toolkit.reporting.ReportUtil;
import org.devgateway.toolkit.forms.util.MarkupCacheService;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.output.AbstractReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.base.FlowReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.base.StreamReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.AllItemsHtmlPrinter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlPrinter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.StreamHtmlOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.rtf.FlowRTFOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.xls.FlowExcelOutputProcessor;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.DefaultNameGenerator;
import org.pentaho.reporting.libraries.repository.file.FileRepository;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mihai This is the base class for Pentaho reports displayed in Wicket.
 *         If you need filters, please use {@link AbstractFilteredReportPage}
 */
public abstract class AbstractReportPage extends BasePage {

    private static final long serialVersionUID = -7471616582122767104L;

    /**
     * start-key used to identify the reports markup
     */
    private static final String START_NAME_REPORT_KEY = "REPORTMARKUP";

    /**
     * The name of the .prpt report file
     */
    protected String reportResourceName;

    /**
     * Should we cache the HTML, PDF, Word, Excel for this report?
     */
    protected Boolean caching = true;

    @SpringBean
    protected MarkupCacheService markupCacheService;

    /**
     * A special Wicket panel that displays the stream coming from Pentaho's
     * {@link AbstractReportProcessor}
     */
    protected ResourceStreamPanel htmlReportPanel;

    protected ReportDownloadLink pdfDownload;

    protected ReportDownloadLink xlsDownload;

    protected ReportDownloadLink rtfDownload;

    /**
     * @return true if this report page can be displayed without further
     *         conditions. Possible conditions include checking the report
     *         parameters were provided.
     */
    public boolean canRenderReport() {
        return true;
    }

    /**
     * A generic download link component
     * @author mpostelnicu
     * See http://blog.jdriven.com/2013/05/wicket-quick-tips-create-a-download-link/
     */
    public class ReportDownloadLink extends Link<Void> {
        private static final long serialVersionUID = 2227561249211024004L;

        private OutputType outputType;

        private Map<OutputType, String> outputExtension = new HashMap<>();

        public ReportDownloadLink(final String id, final OutputType outputType) {
            super(id);

            this.outputType = outputType;
            outputExtension.put(OutputType.PDF, ".pdf");
            outputExtension.put(OutputType.EXCEL, ".xls");
            outputExtension.put(OutputType.RTF, ".doc");
            setOutputMarkupId(true);
            setOutputMarkupPlaceholderTag(true);
        }

        @Override
        protected void onConfigure() {
            setVisible(canRenderReport());
            super.onConfigure();
        }

        @Override
        public void onClick() {
            AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                private static final long serialVersionUID = 1147133679640377670L;

                @Override
                public void write(final OutputStream output) throws IOException {
                    try {
                        if (canRenderReport()) {
                            // first try to fetch the report from cache, otherwise create the report and cache it
                            byte []reportContent = markupCacheService.getReportFromCache(outputType.name(),
                                    FilenameUtils.getName(AbstractReportPage.this.reportResourceName).
                                            replace(".prpt", ""),
                                    getPageParameters().toString());
                            if (reportContent == null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                generateReport(outputType, baos);
                                reportContent = baos.toByteArray();

                                if (caching) {
                                    markupCacheService.addReportToCache(outputType.name(),
                                            FilenameUtils.getName(AbstractReportPage.this.reportResourceName).
                                                    replace(".prpt", ""),
                                            getPageParameters().toString(), reportContent);
                                }
                            }

                            output.write(reportContent);
                        }
                    } catch (IllegalArgumentException | ReportProcessingException e) {
                        e.printStackTrace();
                    }
                }
            };

            ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream,
                    FilenameUtils.getName(AbstractReportPage.this.reportResourceName).
                            replace(".prpt", outputExtension.get(outputType)));
            getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        }
    }

    /**
     * A {@link Panel} that instead of its markup stream returns another
     * {@link OutputStream}
     *
     * See http
     *      ://wicketbypranav.blogspot.ro/2013/05/throw-outputstream-html-data
     *      -in-wicket.html
     * @author mpostelnicu
     *
     */
    public class ResourceStreamPanel extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {
        private static final long serialVersionUID = 6760926366225472057L;

        private AbstractReportPage parent;

        public ResourceStreamPanel(final String id, final AbstractReportPage parent) {
            super(id);
            this.parent = parent;
            setOutputMarkupId(true);
        }

        /**
         * Implementing getMarkupResourceStream() method to return byte[] data
         * coming from reports generator.
         */
        @Override
        public IResourceStream getMarkupResourceStream(final MarkupContainer container, final Class<?> containerClass) {
            StringBuilder panelMarkup = new StringBuilder();
            panelMarkup.append("<wicket:panel wicket:id='panel'>");
            ByteArrayOutputStream htmlStreamData = new ByteArrayOutputStream();
            try {
                if (canRenderReport()) {
                    generateReport(OutputType.HTML, htmlStreamData);
                }
            } catch (IllegalArgumentException | ReportProcessingException e) {
                e.printStackTrace();
            }
            String content = new String(htmlStreamData.toByteArray());
            panelMarkup.append(content);
            panelMarkup.append("</wicket:panel>");
            return new StringResourceStream(panelMarkup.toString());
        }

        @Override
        public void onAfterRender() {
            super.onAfterRender();

            // unblock the UI after we render the HTML report
            AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
            if (target != null) {
                target.appendJavaScript("$.unblockUI();");
            }
        }

        /**
         * Implementing getCacheKey() method of 'IMarkupCacheKeyProvider' interface.
         */
        public String getCacheKey(final MarkupContainer container, final Class<?> containerClass) {
            // return a key based on page parameters and report class name,
            // also a common start name so that we can identify them
            // (if null is returned then the markup is not cached)
            if (caching) {
                return START_NAME_REPORT_KEY + "-" + getPageParameters().toString()
                        + "-" + this.parent.getClass().getSimpleName();
            }

            return null;
        }
    }

    /**
     * The supported output types
     */
    public enum OutputType {
        PDF, EXCEL, HTML, RTF
    }

    public AbstractReportPage(final String reportResourceName, final PageParameters pageParameters) {
        super(pageParameters);
        this.reportResourceName = reportResourceName;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        add(new AjaxLazyLoadPanel("htmlReportPanel") {
            @Override
            public Component getLazyLoadComponent(final String id) {
                htmlReportPanel = new ResourceStreamPanel(id, AbstractReportPage.this);
                return htmlReportPanel;
            }
        });

        pdfDownload = new ReportDownloadLink("pdfDownload", OutputType.PDF);
        add(pdfDownload);

        xlsDownload = new ReportDownloadLink("xlsDownload", OutputType.EXCEL);
        add(xlsDownload);

        rtfDownload = new ReportDownloadLink("rtfDownload", OutputType.RTF);
        add(rtfDownload);
    }

    /**
     * Returns the report definition used by this report generator. If this
     * method returns <code>null</code>, the report generation process will
     * throw a <code>NullPointerException</code>.
     *
     * @return the report definition used by thus report generator
     */
    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource(reportResourceName);

            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);

            return (MasterReport) directly.getResource();
        } catch (ResourceException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the set of parameters that will be passed to the report
     * generation process. If there are no parameters required for report
     * generation, this method may return either an empty or a <code>null</code>
     * <code>Map</code>
     *
     * @return the set of report parameters to be used by the report generation
     *         process, or <code>null</code> if no parameters are required.
     */
    public abstract Map<String, Object> getReportParameters();

    /**
     * Generates the report in the specified <code>outputType</code> and writes
     * it into the specified <code>outputStream</code>.
     *
     * It is the responsibility of the caller to close the
     * <code>outputStream</code> after this method is executed.
     *
     * @param outputType
     *            the output type of the report (HTML, PDF, HTML)
     * @param outputStream
     *            the stream into which the report will be written
     * @throws IllegalArgumentException
     *             indicates the required parameters were not provided
     * @throws ReportProcessingException
     *             indicates an error generating the report
     */

    public void generateReport(final OutputType outputType, final OutputStream outputStream)
            throws IllegalArgumentException,
            ReportProcessingException {
        if (outputStream == null) {
            throw new IllegalArgumentException("The output stream was not specified");
        }

        // Get the report and data factory
        final MasterReport report = getReportDefinition();

        // Add any parameters to the report
        final Map<String, Object> reportParameters = getReportParameters();
        if (reportParameters == null) {
            return;
        }

        for (String key : reportParameters.keySet()) {
            report.getParameterValues().put(key, reportParameters.get(key));
        }

        // Prepare to generate the report
        AbstractReportProcessor reportProcessor = null;
        try {
            // Greate the report processor for the specified output type
            switch (outputType) {
                case PDF: {
                    final PdfOutputProcessor target = new PdfOutputProcessor(report.getConfiguration(),
                            outputStream, report.getResourceManager());
                    reportProcessor = new PageableReportProcessor(report, target);
                    reportProcessor.processReport();
                    break;
                }

                case EXCEL: {
                    final FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(report.getConfiguration(),
                            outputStream, report.getResourceManager());
                    reportProcessor = new FlowReportProcessor(report, target);
                    reportProcessor.processReport();
                    break;
                }

                case RTF: {
                    final FlowRTFOutputProcessor target = new FlowRTFOutputProcessor(report.getConfiguration(),
                            outputStream, report.getResourceManager());
                    reportProcessor = new FlowReportProcessor(report, target);
                    reportProcessor.processReport();
                    break;
                }

                case HTML: {
                    ContentLocation targetRoot = null;
                    File tempDir = null;
                    try {

                        //we manually make the folder to drop all exported html files into
                        tempDir = ReportUtil.createTemporaryDirectory("tmpreport");
                        targetRoot = new FileRepository(tempDir).getRoot();
                    } catch (ContentIOException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //we create a folder content resource for the entire tmpdir.
                    // This dir will only hold the fields for this export
                    FolderContentResource fcr = new FolderContentResource(tempDir);

                    //we always have an authenticated web app
                    AuthenticatedWebApplication authApp = (AuthenticatedWebApplication) getApplication();

                    //we add the folder resource as a shared resource
                    authApp.getSharedResources().add(tempDir.getName(), fcr);
                    SharedResourceReference folderResourceReference = new SharedResourceReference(tempDir.getName());
                    authApp.mountResource(tempDir.getName(), folderResourceReference);


                    final HtmlOutputProcessor outputProcessor =
                            new StreamHtmlOutputProcessor(report.getConfiguration());
                    final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
                    printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, "index", "html"));

                    printer.setDataWriter(targetRoot, new DefaultNameGenerator(targetRoot, "content")); //$NON-NLS-1$

                    //we use a special URL Rewriter that knows how to speak Wicket :-)
                    printer.setUrlRewriter(new WicketResourceURLRewriter(folderResourceReference));
                    outputProcessor.setPrinter(printer);
                    reportProcessor = new StreamReportProcessor(report, outputProcessor);
                    reportProcessor.processReport();

                    // we plug the html file stream into the output stream
                    FileInputStream indexFileStream = new FileInputStream(tempDir.getAbsolutePath()
                            + File.separator + "index.html");
                    IOUtils.copy(indexFileStream, outputStream);
                    indexFileStream.close();

                    break;
                }

                default: throw new RuntimeException("Unknown output type provided!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reportProcessor != null) {
                reportProcessor.close();
            }
        }
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // block UI for reports page
        response.render(JavaScriptHeaderItem.forReference(BlockUiReportsJavaScript.INSTANCE));
    }
}
