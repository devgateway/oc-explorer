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
package org.devgateway.toolkit.forms.wicket.page.lists;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullEditPageClassException;
import org.devgateway.toolkit.forms.exceptions.NullJpaRepositoryException;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.RevisionsPage;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaRepositoryDataProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.excel.service.ExcelGeneratorService;
import org.devgateway.toolkit.persistence.repository.BaseJpaRepository;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mpostelnicu This class can be use to display a list of Categories
 *
 *         T - entity type Y - filter
 */
public abstract class AbstractListPage<T extends GenericPersistable> extends BasePage {
    private static final long serialVersionUID = 1958350868666244087L;

    protected SortableJpaRepositoryDataProvider<T> dataProvider;

    protected BootstrapBookmarkablePageLink<T> editPageLink;

    protected Form excelForm;

    @SpringBean
    protected ExcelGeneratorService excelGeneratorService;

    /**
     * Get a stub print button that does nothing
     *
     * @param pageParameters
     * @return
     */
    protected Component getPrintButton(final PageParameters pageParameters) {
        return new WebMarkupContainer("printButton").setVisibilityAllowed(false);
    }

    public class ActionPanel extends Panel {
        private static final long serialVersionUID = 5821419128121941939L;

        /**
         * @param id
         * @param model
         */
        public ActionPanel(final String id, final IModel<T> model) {
            super(id, model);

            final PageParameters pageParameters = new PageParameters();

            @SuppressWarnings("unchecked")
            T entity = (T) ActionPanel.this.getDefaultModelObject();
            if (entity != null) {
                pageParameters.set(WebConstants.PARAM_ID, entity.getId());
            }

            BootstrapBookmarkablePageLink<T> editPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", editPageClass, pageParameters, Buttons.Type.Info);
            editPageLink.setIconType(FontAwesomeIconType.edit).setSize(Size.Small)
                    .setLabel(new StringResourceModel("edit", AbstractListPage.this, null));
            add(editPageLink);

            add(getPrintButton(pageParameters));

            PageParameters revisionsPageParameters = new PageParameters();
            revisionsPageParameters.set(WebConstants.PARAM_ID, entity.getId());
            revisionsPageParameters.set(WebConstants.PARAM_ENTITY_CLASS, entity.getClass().getName());

            BootstrapBookmarkablePageLink<Void> revisionsPageLink = new BootstrapBookmarkablePageLink<>("revisions",
                    RevisionsPage.class, revisionsPageParameters, Buttons.Type.Info);
            revisionsPageLink.setIconType(FontAwesomeIconType.clock_o).setSize(Size.Small)
                    .setLabel(new StringResourceModel("revisions", AbstractListPage.this, null));
            add(revisionsPageLink);

        }
    }

    protected Class<? extends AbstractEditPage<T>> editPageClass;
    protected AjaxFallbackBootstrapDataTable<T, String> dataTable;
    protected List<IColumn<T, String>> columns;

    protected BaseJpaRepository<T, Long> jpaRepository;

    public AbstractListPage(final PageParameters parameters) {
        super(parameters);

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<T, String>(new Model<>("ID"), "id", "id"));
    }

    public ActionPanel getActionPanel(final String id, final IModel<T> model) {
        return new ActionPanel(id, model);
    }

    public SortableJpaRepositoryDataProvider<T> getProvider() {
        return new SortableJpaRepositoryDataProvider<>(jpaRepository);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (jpaRepository == null) {
            throw new NullJpaRepositoryException();
        }
        if (editPageClass == null) {
            throw new NullEditPageClassException();
        }

        dataProvider = new SortableJpaRepositoryDataProvider<>(jpaRepository);
        dataProvider.setFilterState(newFilterState());

        // create the excel download form; by default this form is hidden and we should make it visible only to pages
        // where we want to export entities to excel file
        excelForm = new ExcelDownloadForm("excelForm");
        excelForm.setVisibilityAllowed(false);
        add(excelForm);

        // add the 'Edit' button
        columns.add(new AbstractColumn<T, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,
                                     final IModel<T> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });
        dataTable = new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, WebConstants.PAGE_SIZE);

        ResettingFilterForm<JpaFilterState<T>> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);
        filterForm.add(dataTable);
        add(filterForm);

        if (hasFilteredColumns()) {
            dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm));
        }

        PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, null);

        editPageLink = new BootstrapBookmarkablePageLink<T>("new", editPageClass, pageParameters, Buttons.Type.Success);
        editPageLink.setIconType(FontAwesomeIconType.plus_circle).setSize(Size.Large)
                .setLabel(new StringResourceModel("new", AbstractListPage.this, null));

        add(editPageLink);
    }

    private boolean hasFilteredColumns() {
        for (IColumn<?, ?> column : columns) {
            if (column instanceof IFilteredColumn) {
                return true;
            }
        }
        return false;
    }

    public JpaFilterState<T> newFilterState() {
        return new JpaFilterState<>();
    }

    protected String getClassName() {
        return Classes.simpleName(getClass());
    }

    /**
     * A wrapper form that is used to fire the excel download action
     */
    public class ExcelDownloadForm extends Form {
        public ExcelDownloadForm(final String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final AJAXDownload download = new AJAXDownload() {
                @Override
                protected IRequestHandler getHandler() {
                    return new IRequestHandler() {
                        @Override
                        public void respond(final IRequestCycle requestCycle) {
                            final HttpServletResponse response = (HttpServletResponse) requestCycle
                                    .getResponse().getContainerResponse();

                            try {
                                final int batchSize = 10000;

                                final long count = excelGeneratorService.count(
                                        jpaRepository,
                                        dataProvider.getFilterState().getSpecification());

                                // if we need to export just one file then we don't create an archive
                                if (count <= batchSize) {
                                    // set a maximum download of objects per excel file
                                    final PageRequest pageRequest = new PageRequest(0, batchSize,
                                            Sort.Direction.ASC, "id");

                                    final byte[] bytes = excelGeneratorService.getExcelDownload(
                                            jpaRepository,
                                            dataProvider.getFilterState().getSpecification(),
                                            pageRequest);

                                    response.setContentType(
                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.xlsx");
                                    response.getOutputStream().write(bytes);
                                } else {
                                    response.setContentType("application/zip");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.zip");
                                    response.flushBuffer();
                                    final ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(
                                            response.getOutputStream()));
                                    zout.setMethod(ZipOutputStream.DEFLATED);
                                    zout.setLevel(Deflater.NO_COMPRESSION);
                                    final int numberOfPages = (int) Math.ceil((double) count / batchSize);
                                    for (int i = 0; i < numberOfPages; i++) {
                                        final PageRequest pageRequest = new PageRequest(i, batchSize,
                                                Sort.Direction.ASC, "id");
                                        final byte[] bytes = excelGeneratorService.getExcelDownload(
                                                jpaRepository,
                                                dataProvider.getFilterState().getSpecification(),
                                                pageRequest);
                                        final ZipEntry ze = new ZipEntry("excel-export-page " + (i + 1) + ".xlsx");
                                        zout.putNextEntry(ze);
                                        zout.write(bytes, 0, bytes.length);
                                        zout.closeEntry();
                                        response.flushBuffer();
                                    }
                                    zout.close();
                                    response.flushBuffer();
                                }
                            } catch (IOException e) {
                                logger.error(e);
                            }

                            RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);
                        }

                        @Override
                        public void detach(final IRequestCycle requestCycle) {
                            // do nothing;
                        }
                    };
                }
            };
            add(download);

            final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton",
                    new Model<>("Excel Download"),
                    Buttons.Type.Warning) {
                @Override
                protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                    // initiate the file download
                    download.initiate(target);
                }
            };
            excelButton.setIconType(FontAwesomeIconType.file_excel_o);
            add(excelButton);
        }
    }
}
