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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;

/**
 * @author mpostelnicu An {@link AbstractReportPage} that also has filters
 */
public abstract class AbstractFilteredReportPage<T> extends AbstractReportPage {

    private static final long serialVersionUID = 4999135497729882432L;
    protected FilterForm form;

    protected abstract void onFilterSubmit(AjaxRequestTarget target, Form<T> form);

    public class FilterForm extends Form<T> {

        private static final long serialVersionUID = 4507175422744029036L;

        public FilterForm(final String id, final IModel<T> model) {
            this(id);
        }

        public void setCompoundPropertyModel(final IModel<T> model) {
            setModel(new CompoundPropertyModel<T>(model));
        }

        public FilterForm(final String id) {
            super(id);

            BootstrapSubmitButton searchButton = new BootstrapSubmitButton("search", new Model<>("Search")) {

                private static final long serialVersionUID = -4557954259726916159L;

                @SuppressWarnings("unchecked")
                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    onFilterSubmit(target, (Form<T>) form);

                    target.add(feedbackPanel);
                    target.add(htmlReportPanel);
                    target.add(pdfDownload);
                    target.add(xlsDownload);
                    target.add(rtfDownload);
                }

                @Override
                protected void onError(final AjaxRequestTarget target) {
                    target.add(feedbackPanel);
                    target.add(htmlReportPanel);
                    target.add(pdfDownload);
                    target.add(xlsDownload);
                    target.add(rtfDownload);
                }

            };

            add(searchButton);
        }
    }

    public AbstractFilteredReportPage(final String reportResourceName, final PageParameters pageParameters) {
        super(reportResourceName, pageParameters);

        form = new FilterForm("editForm");
        add(form);

        form.add(new Label("search.info", new StringResourceModel("search.info", this, null)));
    }
}
