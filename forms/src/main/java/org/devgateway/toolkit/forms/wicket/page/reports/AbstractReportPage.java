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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.devgateway.toolkit.forms.util.FolderContentResource;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.styles.BlockUiReportsJavaScript;
import org.devgateway.toolkit.reporting.ReportUtil;
import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.DataFactory;
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
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.BandedMDXDataFactory;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.DefaultNameGenerator;
import org.pentaho.reporting.libraries.repository.file.FileRepository;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * @author mpostelnicu This is the base class for Pentaho reports displayed in
 *         Wicket. If you need filters, please use
 *         {@link AbstractFilteredReportPage}
 */
public abstract class AbstractReportPage extends BasePage {

	private static final long serialVersionUID = -7471616582122767104L;

	/**
	 * The name of the .prpt report file
	 */
	protected String reportResourceName;

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
	 * 
	 * @author mpostelnicu
	 * @see http://blog.jdriven.com/2013/05/wicket-quick-tips-create-a-download-
	 *      link/
	 */
	public class ReportDownloadLink extends Link<Void> {

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

		private static final long serialVersionUID = 2227561249211024004L;

		@Override
		public void onClick() {
			AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
				private static final long serialVersionUID = 1147133679640377670L;

				@Override
				public void write(final OutputStream output) throws IOException {
					try {
						if (canRenderReport()) {
							generateReport(outputType, output);
						}
					} catch (IllegalArgumentException | ReportProcessingException e) {
						e.printStackTrace();
					}
				}
			};

			ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream,
					AbstractReportPage.this.reportResourceName.replace(".prpt", outputExtension.get(outputType)));
			getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
	};

	/**
	 * A {@link Panel} that instead of its markup stream returns another
	 * {@link OutputStream}
	 * 
	 * @see http
	 *      ://wicketbypranav.blogspot.ro/2013/05/throw-outputstream-html-data
	 *      -in-wicket.html
	 * @author mpostelnicu
	 *
	 */
	public class ResourceStreamPanel extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

		private static final long serialVersionUID = 6760926366225472057L;

		public ResourceStreamPanel(final String id) {
			super(id);
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
		 * Implementing getCacheKey() method of 'IMarkupCacheKeyProvider'
		 * interface.
		 */
		@Override
		public String getCacheKey(final MarkupContainer container, final Class<?> containerClass) {
			// Must return null so that the markup isn't cached.
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

		add(new AjaxLazyLoadPanel("htmlReportPanel") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getLazyLoadComponent(final String id) {
				htmlReportPanel = new ResourceStreamPanel(id);
				return htmlReportPanel;
			}
		});
		// htmlReportPanel = new ResourceStreamPanel("htmlReportPanel");
		// add(htmlReportPanel);

		pdfDownload = new ReportDownloadLink("pdfDownload", OutputType.PDF);
		add(pdfDownload);

		xlsDownload = new ReportDownloadLink("xlsDownload", OutputType.EXCEL);
		add(xlsDownload);

		rtfDownload = new ReportDownloadLink("rtfDownload", OutputType.RTF);
		add(rtfDownload);
	}

	/**
	 * Use this to alter the existing or create a new datafactory on the fly.
	 * Here we adjust the path to the cube file to match the resource name in
	 * Java
	 * 
	 * @param dataFactory
	 */
	public void adjustDataFactory(final DataFactory dataFactory) {

		/**
		 * We find the BandedMDXDataFactory
		 */
		CompoundDataFactory cdf = (CompoundDataFactory) dataFactory;
		BandedMDXDataFactory bmdf = null;
		for (int i = 0; i < cdf.size(); i++) {
			if (cdf.get(i) instanceof BandedMDXDataFactory) {
				bmdf = (BandedMDXDataFactory) cdf.get(i);
				break;
			}
		}

		if (bmdf == null) {
			return;
		}

		// /**
		// * We assume no other CubeFileProvider is implemented but the Default
		// */
		// DefaultCubeFileProvider dcfp = (DefaultCubeFileProvider)
		// bmdf.getCubeFileProvider();
		//
		// // if not, we try to fetch it through the classloader resource
		// locator
		// final ClassLoader classloader = this.getClass().getClassLoader();
		// final URL mondrianCubeFileURL =
		// classloader.getResource(MondrianConstants.CATALOG_FILE);
		//
		// /**
		// * We change the cube file in the provider to the new location
		// */
		// dcfp.setMondrianCubeFile(mondrianCubeFileURL.getFile());
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
	 * <p/>
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
			throws IllegalArgumentException, ReportProcessingException {
		if (outputStream == null) {
			throw new IllegalArgumentException("The output stream was not specified");
		}

		// Get the report and data factory
		final MasterReport report = getReportDefinition();

		// we need this only for Mondrian queries
		// adjustDataFactory(report.getDataFactory());

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
				final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(report.getConfiguration(),
						outputStream, report.getResourceManager());
				reportProcessor = new PageableReportProcessor(report, outputProcessor);
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

					// we manually make the folder to drop all exported html
					// files into
					tempDir = ReportUtil.createTemporaryDirectory("tmpreport");
					targetRoot = new FileRepository(tempDir).getRoot();
				} catch (ContentIOException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// we create a folder content resource for the entire tmpdir.
				// This dir will only hold the fields for this export
				FolderContentResource fcr = new FolderContentResource(tempDir);

				// we always have an authenticated web app
				AuthenticatedWebApplication authApp = (AuthenticatedWebApplication) getApplication();

				// we add the folder resource as a shared resource
				authApp.getSharedResources().add(tempDir.getName(), fcr);
				SharedResourceReference folderResourceReference = new SharedResourceReference(tempDir.getName());
				authApp.mountResource(tempDir.getName(), folderResourceReference);

				final HtmlOutputProcessor outputProcessor = new StreamHtmlOutputProcessor(report.getConfiguration());
				final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
				printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, "index", "html"));

				printer.setDataWriter(targetRoot, new DefaultNameGenerator(targetRoot, "content")); //$NON-NLS-1$

				// we use a special URL Rewriter that knows how to speak Wicket
				// :-)
				printer.setUrlRewriter(new WicketResourceURLRewriter(folderResourceReference));
				outputProcessor.setPrinter(printer);
				reportProcessor = new StreamReportProcessor(report, outputProcessor);
				reportProcessor.processReport();

				// we plug the html file stream into the output stream
				FileInputStream indexFileStream = new FileInputStream(
						tempDir.getAbsolutePath() + File.separator + "index.html");
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
