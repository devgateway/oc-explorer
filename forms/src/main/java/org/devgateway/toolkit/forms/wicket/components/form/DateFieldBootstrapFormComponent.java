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
/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.AbstractDateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import java.util.Date;

/**
 * @author mpostelnicu
 * 
 */
public class DateFieldBootstrapFormComponent extends GenericBootstrapFormComponent<Date, TextField<Date>> {
    private static final long serialVersionUID = 6829640010904041758L;

    public static final String DEFAULT_FORMAT = "dd/MM/yy";

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public DateFieldBootstrapFormComponent(final String id, final IModel<String> labelModel, final IModel<Date> model) {
        super(id, labelModel, model);
    }

    public DateFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param model
     */
    public DateFieldBootstrapFormComponent(final String id, final IModel<Date> model) {
        super(id, model);
    }

    @Override
    protected TextField<Date> inputField(final String id, final IModel<Date> model) {
        DateTextFieldConfig config = new DateTextFieldConfig()
                .withView(AbstractDateTextFieldConfig.View.Year)
                .withFormat(DEFAULT_FORMAT)
                .autoClose(true).calendarWeeks(true).forceParse(false).highlightToday(true).clearButton(true)
                .allowKeyboardNavigation(true)
                .showTodayButton(AbstractDateTextFieldConfig.TodayButton.LINKED)
                .withView(AbstractDateTextFieldConfig.View.Decade);

        return new DateTextField(id, initFieldModel(), config);
    }

    @Override
    public String getUpdateEvent() {
        return "change";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.devgateway.toolkit.forms.wicket.components.form.
     * GenericBootstrapFormComponent#onConfigure()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        IndicatingAjaxLink<String> clearDateLink = new IndicatingAjaxLink<String>("clearDate") {
            private static final long serialVersionUID = -1705495886974891511L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                DateFieldBootstrapFormComponent.this.field.setModelObject(null);
                target.add(DateFieldBootstrapFormComponent.this.field);
            }
        };
        border.add(clearDateLink);
    }
}
