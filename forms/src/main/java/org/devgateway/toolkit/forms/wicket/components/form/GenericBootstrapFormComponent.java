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

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.models.SubComponentWrapModel;
import org.devgateway.toolkit.forms.models.ViewModeConverterModel;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;
import org.devgateway.toolkit.forms.wicket.components.TooltipLabel;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior.Size;
import de.agilecoders.wicket.core.util.Attributes;

/**
 * @author mpostelnicu
 *
 */
public abstract class GenericBootstrapFormComponent<TYPE, FIELD extends FormComponent<TYPE>> extends FieldPanel<TYPE> {

	private static final long serialVersionUID = -7051128382707812456L;

	protected static Logger logger = Logger.getLogger(GenericBootstrapFormComponent.class);

	protected FormGroup border;
	protected FIELD field;
	

	
	protected Label viewModeField;

	protected InputBehavior sizeBehavior;

	private TooltipConfig.OpenTrigger configWithTrigger = TooltipConfig.OpenTrigger.hover;
	protected TooltipLabel tooltipLabel;
	protected IModel<String> labelModel;
	protected boolean nextYear=false;
	

	@Override
	public void onEvent(IEvent<?> event) {
		ComponentUtil.enableDisableEvent(this, event);
	}

	@SuppressWarnings("unchecked")
	protected IModel<TYPE> initFieldModel() {
		if (getDefaultModel() == null)
			return new SubComponentWrapModel<TYPE>(this);
		return (IModel<TYPE>) getDefaultModel();
	}

	/**
	 * use this behavior for choices/groups that are not one component in the html but many.
	 */
	protected void getAjaxFormChoiceComponentUpdatingBehavior() {
		field.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				GenericBootstrapFormComponent.this.onUpdate(target);
			}
		});
	}

	protected void getAjaxFormComponentUpdatingBehavior () {
		field.add(new AjaxFormComponentUpdatingBehavior(getUpdateEvent()) {

			private static final long serialVersionUID = -2696538086634114609L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(border);
				GenericBootstrapFormComponent.this.onUpdate(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				target.add(border);
			}
		});
	}

	public String getUpdateEvent() {
		return "blur";
	}

	public GenericBootstrapFormComponent<TYPE, FIELD> type(Class<?> clazz) {
		field.setType(clazz);
		return this;
	}

	public GenericBootstrapFormComponent<TYPE, FIELD> size(Size size) {
		sizeBehavior.size(size);
		return this;
	}


	public GenericBootstrapFormComponent<TYPE, FIELD> nextYear() {
		nextYear=true;
		return this;
	}


	public GenericBootstrapFormComponent(String id) {
		this(id, null);
	}


	public String getLabelKey() {
		return this.getId()+".label";
	}
	
	public GenericBootstrapFormComponent(String id, IModel<TYPE> model) {
		this(id,new ResourceModel(id+".label"),model);
	}



	public GenericBootstrapFormComponent(String id, IModel<String> labelModel, IModel<TYPE> model) {
		super(id, model);
		this.labelModel=labelModel;
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
		
		border=new FormGroup("enclosing-field-group");
		border.setOutputMarkupId(true);
		add(border);
		
		field = inputField("field", model);
		field.setVisibilityAllowed(!ComponentUtil.isViewMode());
		field.setOutputMarkupId(true);
		sizeBehavior=new InputBehavior(InputBehavior.Size.Medium);
		field.add(sizeBehavior);
		border.add(field);

		tooltipLabel=new TooltipLabel("tooltipLabel", id);
		border.add(tooltipLabel);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		// add a new class for required fields
		if (field.isRequired()) {
			Attributes.addClass(tag, "required");
		}
	}

	public GenericBootstrapFormComponent<TYPE, FIELD> hideLabel() {
		field.setLabel(null);
		return this;
	}

	protected abstract FIELD inputField(String id, IModel<TYPE> model);

	public  GenericBootstrapFormComponent<TYPE, FIELD> required() {
		field.setRequired(true);
		return this;
	}

	protected void onUpdate(AjaxRequestTarget target) {
	}

	public FIELD getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();

		field.setLabel(labelModel);
		
		if ((field instanceof RadioGroup) || (field instanceof CheckGroup)) {
			getAjaxFormChoiceComponentUpdatingBehavior();
		} else {
			getAjaxFormComponentUpdatingBehavior();
		}
		
		viewModeField=new Label("viewModeField", new ViewModeConverterModel<TYPE>(getModel()));
		viewModeField.setEscapeModelStrings(false);
		viewModeField.setVisibilityAllowed(ComponentUtil.isViewMode());
		border.add(viewModeField);
	
		tooltipLabel.setConfigWithTrigger(configWithTrigger);
	}

	/**
	 * @return the border
	 */
	public FormGroup getBorder() {
		return border;
	}

	public TooltipConfig.OpenTrigger getConfigWithTrigger() {
		return configWithTrigger;
	}

	public void setConfigWithTrigger(TooltipConfig.OpenTrigger configWithTrigger) {
		this.configWithTrigger = configWithTrigger;
	}
}
