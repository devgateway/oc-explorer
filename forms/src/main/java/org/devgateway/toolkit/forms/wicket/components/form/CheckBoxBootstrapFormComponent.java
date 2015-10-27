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

import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 * 
 */
public class CheckBoxBootstrapFormComponent extends GenericEnablingBootstrapFormComponent<Boolean, CheckBox> {
	private static final long serialVersionUID = -4032850928243673675L;

	private Boolean isFloatedInput = false;

	public CheckBoxBootstrapFormComponent(String id, IModel<String> labelModel, IModel<Boolean> model) {
		super(id, labelModel, model);
	}

	/**
	 * @param id
	 * @param model
	 */
	public CheckBoxBootstrapFormComponent(String id, IModel<Boolean> model) {
		super(id, model);
	}

	public CheckBoxBootstrapFormComponent(String id) {
		super(id);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		if(isFloatedInput) {
			Attributes.addClass(tag, "floated-input");
		}
	}

	@Override
	protected CheckBox inputField(String id, IModel<Boolean> model) {
		return (CheckBox) new CheckBox(id,initFieldModel());
	}

	@Override
	public String getUpdateEvent() {
		return "onclick";
	}

    public Boolean getIsFloatedInput() {
        return isFloatedInput;
    }

    public void setIsFloatedInput(Boolean isFloatedInput) {
        this.isFloatedInput = isFloatedInput;
    }

	@Override
	protected boolean boundComponentsVisibilityAllowed(Boolean selectedValue) {
		return selectedValue;
	}
}
