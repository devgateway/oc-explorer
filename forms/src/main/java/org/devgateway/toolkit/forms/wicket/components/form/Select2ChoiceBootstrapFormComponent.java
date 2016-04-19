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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2Choice;

import de.agilecoders.wicket.core.util.Attributes;

/**
 * @author mpostelnicu
 * 
 */
public class Select2ChoiceBootstrapFormComponent<TYPE> extends GenericEnablingBootstrapFormComponent<TYPE, Select2Choice<TYPE>> {
	private static final long serialVersionUID = -3430670677135618576L;

    private Boolean isFloatedInput = false;

	public Select2ChoiceBootstrapFormComponent(String id, IModel<String> labelModel, IModel<TYPE> model,
			ChoiceProvider<TYPE> choiceProvider) {
		super(id, labelModel, model);
		provider(choiceProvider);
	}
	
	public Select2ChoiceBootstrapFormComponent<TYPE> provider(ChoiceProvider<TYPE> choiceProvider) {
		field.setProvider(choiceProvider);
		return this;
	}

	public Select2ChoiceBootstrapFormComponent(String id, IModel<String> labelModel, ChoiceProvider<TYPE> choiceProvider) {
		this(id, labelModel, null, choiceProvider);
	}
	
	public Select2ChoiceBootstrapFormComponent(String id, ChoiceProvider<TYPE> choiceProvider,IModel<TYPE> model) {
		super(id,model);
		provider(choiceProvider);
	}

	public Select2ChoiceBootstrapFormComponent(String id, ChoiceProvider<TYPE> choiceProvider) {
		super(id);
		provider(choiceProvider);
	}

	
	@Override
	protected Select2Choice<TYPE> inputField(String id, IModel<TYPE> model) {
		return new Select2Choice<TYPE>(id, initFieldModel());
	}
	

	@Override
	public String getUpdateEvent() {
		return "change";
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent#onConfigure()
	 */
	@Override
	protected void onInitialize() {
		field.getSettings().setPlaceholder("Click to select");
		field.getSettings().setAllowClear(true);		
		field.getSettings().setCloseOnSelect(true);
		field.getSettings().setDropdownAutoWidth(true);		
		super.onInitialize();

	}

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        if(isFloatedInput) {
            Attributes.addClass(tag, "floated-input");
        }
    }

    public Boolean getIsFloatedInput() {
        return isFloatedInput;
    }

    public void setIsFloatedInput(Boolean isFloatedInput) {
        this.isFloatedInput = isFloatedInput;
    }

	@Override
	protected boolean boundComponentsVisibilityAllowed(TYPE selectedValue) {
		return false;
	}
}
