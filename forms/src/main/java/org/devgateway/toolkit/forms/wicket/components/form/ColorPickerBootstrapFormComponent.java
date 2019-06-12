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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.ColorPickerTextField;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 * 
 */
public class ColorPickerBootstrapFormComponent extends GenericBootstrapFormComponent<String, ColorPickerTextField> {

    public ColorPickerBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                             final IModel<String> model) {
        super(id, labelModel, model);
    }

    public ColorPickerBootstrapFormComponent(final String id, final IModel<String> model) {
        super(id, model);
    }

    /**
     * @param id
     */
    public ColorPickerBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    protected ColorPickerTextField inputField(final String id, final IModel<String> model) {
        return new ColorPickerTextField(id, initFieldModel());
    }
}
