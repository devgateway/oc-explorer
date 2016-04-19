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

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.FormsWebApplication;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteEditor;

/**
 * @author mpostelnicu
 * 
 */
public class SummernoteBootstrapFormComponent extends GenericBootstrapFormComponent<String, SummernoteEditor> {
	private StringValidator validator=WebConstants.StringValidators.maximumLengthValidatorTextArea;


	private SummernoteConfig config; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7822733988194369835L;

	public SummernoteBootstrapFormComponent(String id, IModel<String> labelModel, IModel<String> model) {
		super(id, labelModel, model);
	}
	
	public SummernoteBootstrapFormComponent(String id, IModel<String> labelModel) {
		super(id, labelModel, null);
	}
	
	

	/**
	 * @param id
	 */
	public SummernoteBootstrapFormComponent(String id) {
		super(id);
	}

	@Override
	protected SummernoteEditor inputField(String id, IModel<String> model) {

		config = new SummernoteConfig();
		
		//this enabled for demo purposes, but it stores the files in volatile disk dir
		config.useStorageId(FormsWebApplication.STORAGE_ID);
		
		config.withHeight(50);
		config.withAirMode(false);

		SummernoteEditor summernoteEditor = new SummernoteEditor(id, initFieldModel(), config);

		return summernoteEditor;
	}
	

    @Override
    protected void onInitialize() {
    	super.onInitialize();
    	getField().add(validator);
    }

	public SummernoteConfig getConfig() {
		return config;
	}
}
