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

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2MultiChoice;

import de.agilecoders.wicket.core.util.Attributes;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;

import java.util.Collection;

/**
 * @author mpostelnicu
 * 
 */
public class Select2MultiChoiceBootstrapFormComponent<TYPE> extends GenericBootstrapFormComponent<Collection<TYPE>, Select2MultiChoice<TYPE>> {
	private static final long serialVersionUID = 7177558191815237814L;
	private ChoiceProvider<TYPE> choiceProvider;

    private Boolean isFloatedInput = false;

	public Select2MultiChoiceBootstrapFormComponent(String id, IModel<String> labelModel, IModel<Collection<TYPE>> model,
			ChoiceProvider<TYPE> choiceProvider) {
		super(id, labelModel, model);
		this.choiceProvider=choiceProvider;
	}
	

	
	public Select2MultiChoiceBootstrapFormComponent<TYPE> provider(ChoiceProvider<TYPE> choiceProvider) {
		field.setProvider(choiceProvider);
		return this;
	}

	public Select2MultiChoiceBootstrapFormComponent(String id, IModel<String> labelModel, ChoiceProvider<TYPE> choiceProvider) {
		super(id,labelModel,null);
		this.choiceProvider=choiceProvider;
	}

    public Select2MultiChoiceBootstrapFormComponent(String id, ChoiceProvider<TYPE> choiceProvider,IModel<Collection<TYPE>> model) {
        super(id,model);
        this.choiceProvider=choiceProvider;
    }

	public Select2MultiChoiceBootstrapFormComponent(String id, ChoiceProvider<TYPE> choiceProvider) {
		super(id);
		this.choiceProvider=choiceProvider;
	}

	@Override
	protected Select2MultiChoice<TYPE> inputField(String id, IModel<Collection<TYPE>> model) {
		Select2MultiChoice<TYPE> multiChoice = new Select2MultiChoice<TYPE>(id, initFieldModel());
		multiChoice.setEscapeModelStrings(false);
		return multiChoice;
	}

	
	@Override
	public String getUpdateEvent() {
		return "onchange";
	}
	
	
	/* (non-Javadoc)
	 * @see org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent#onConfigure()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		field.setProvider(choiceProvider);
		field.getSettings().setAllowClear(true);
		field.getSettings().setPlaceholder("");
		field.getSettings().setDropdownAutoWidth(true);
		field.getSettings().setEscapeMarkup("function (m) {return m;}");
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
}
