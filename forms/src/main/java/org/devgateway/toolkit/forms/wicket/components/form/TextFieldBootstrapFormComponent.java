/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;

import java.math.BigDecimal;

/**
 * @author mpostelnicu
 *
 */
public class TextFieldBootstrapFormComponent<TYPE> extends GenericBootstrapFormComponent<TYPE, TextField<TYPE>> {
    private static final long serialVersionUID = 8062663141536130313L;

    private StringValidator validator = WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXT;

    public TextFieldBootstrapFormComponent(final String id, final IModel<String> labelModel, final IModel<TYPE> model) {
        super(id, labelModel, model);
    }

    public TextFieldBootstrapFormComponent(final String id, final IModel<TYPE> model) {
        super(id, model);
    }

    /**
     * @param id
     */
    public TextFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    protected TextField<TYPE> inputField(final String id, final IModel<TYPE> model) {
        return new TextField<TYPE>(id, initFieldModel());
    }

    public TextFieldBootstrapFormComponent<TYPE> integer() {
        field.setType(Integer.class);
        return this;
    }

    public TextFieldBootstrapFormComponent<TYPE> decimal() {
        field.setType(BigDecimal.class);
        return this;
    }

    public TextFieldBootstrapFormComponent<TYPE> asDouble() {
        field.setType(Double.class);
        return this;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getField().getType() == null || !Number.class.isAssignableFrom(getField().getType())) {
            getField().add(validator);
        }
    }
}
