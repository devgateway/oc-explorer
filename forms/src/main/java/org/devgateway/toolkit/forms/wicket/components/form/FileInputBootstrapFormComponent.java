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

import java.util.Collection;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.FileMetadata;

import de.agilecoders.wicket.core.util.Attributes;

/**
 * @author idobre
 * @since 11/13/14
 *
 * Multi-file upload file component. We use FileInputBootstrapFormComponentWrapper
 * since BootstrapFileInput it's not actually a FormComponent
 * @see FileInputBootstrapFormComponentWrapper
 */
public class FileInputBootstrapFormComponent extends
        GenericBootstrapFormComponent<Collection<FileMetadata>, FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>>> {

	private static final long serialVersionUID = 1L;

	private Boolean isFloatedInput = false;

	private FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>> fileInputBootstrapFormComponentWrapper;

	/**
	 * @param id
	 * @param model
	 */
	public FileInputBootstrapFormComponent(String id, IModel<Collection<FileMetadata>> model) {
		super(id, model);
	}

	/**
	 * @param id
	 * @param labelModel
	 * @param model
	 */
	public FileInputBootstrapFormComponent(String id, IModel<String> labelModel, IModel<Collection<FileMetadata>> model) {
		super(id, labelModel, model);
	}

	/**
	 * @param id
	 */
	public FileInputBootstrapFormComponent(String id) {
		super(id);
	}

	@Override
	protected void getAjaxFormComponentUpdatingBehavior () {
		// do nothing;
	}

	public FileInputBootstrapFormComponent maxFiles(int maxFiles) {
        field.maxFiles(maxFiles);
        return this;
    }

    @Override
    protected FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>> inputField(String id, IModel<Collection<FileMetadata>> model) {
		fileInputBootstrapFormComponentWrapper = new FileInputBootstrapFormComponentWrapper<>(id, initFieldModel());

        return fileInputBootstrapFormComponentWrapper;
    }

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		if(getIsFloatedInput()) {
			Attributes.addClass(tag, "floated-input");
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		ComponentUtil.enableDisableEvent(this, event);
	}

	public void setVisibleOnlyToAdmin(Boolean visibleOnlyToAdmin) {
		fileInputBootstrapFormComponentWrapper.setVisibleOnlyToAdmin(visibleOnlyToAdmin);
	}
	
	public void setDisableDeleteButton(Boolean disableDeleteButton) {
		fileInputBootstrapFormComponentWrapper.setDisableDeleteButton(disableDeleteButton);
	}

	public Boolean getIsFloatedInput() {
		return isFloatedInput;
	}

	public void setIsFloatedInput(Boolean isFloatedInput) {
		this.isFloatedInput = isFloatedInput;
	}

	public FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>> getFileInputBootstrapFormComponentWrapper() {
		return fileInputBootstrapFormComponentWrapper;
	}
}
