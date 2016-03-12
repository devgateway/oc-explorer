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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.util.Attributes;

/**
 * @author mpostelnicu
 * 
 */
public class CheckBoxBootstrapFormComponent extends GenericEnablingBootstrapFormComponent<Boolean, BootstrapCheckbox> {
	private static final long serialVersionUID = -4032850928243673675L;

	private Boolean isFloatedInput = false;

	protected CheckBox wrappedCheckbox;

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
	protected FormComponent<Boolean> updatingBehaviorComponent() {
		return wrappedCheckbox;
	}

	@Override
	protected BootstrapCheckbox inputField(String id, IModel<Boolean> model) {
		return  new BootstrapCheckbox(id,initFieldModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CheckBox newCheckBox(String id, IModel<Boolean> model) {
				wrappedCheckbox=super.newCheckBox(id, model);
				wrappedCheckbox.setOutputMarkupId(true);
				return wrappedCheckbox;
			}
		};
	}
	

	@Override
	public String getUpdateEvent() {
		return "click";
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
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if(field.isRequired()) 
			wrappedCheckbox.setRequired(true);
	}
}
