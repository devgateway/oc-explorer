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
package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;

public class PasswordFieldBootstrapFormComponent extends GenericBootstrapFormComponent<String, PasswordTextField> {
    private static final long serialVersionUID = -2865390099361839324L;

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public PasswordFieldBootstrapFormComponent(final String id, final IModel<String> labelModel,
            final IModel<String> model) {
        super(id, labelModel, model);
    }

    /**
     * @param id
     * @param model
     */
    public PasswordFieldBootstrapFormComponent(final String id, final IModel<String> model) {
        super(id, model);
    }

    /**
     * @param id
     */
    public PasswordFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    protected PasswordTextField inputField(final String id, final IModel<String> model) {
        return new PasswordTextField(id, initFieldModel());
    }
}
