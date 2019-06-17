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

import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * @author idobre
 * @since 11/13/14
 *
 *        Multi-file upload file component. We use
 *        FileInputBootstrapFormComponentWrapper since BootstrapFileInput it's
 *        not actually a FormComponent
 * @see FileInputBootstrapFormComponentWrapper
 */
public class FileInputBootstrapFormComponent extends GenericBootstrapFormComponent<Collection<FileMetadata>,
        FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>>> {

    private static final long serialVersionUID = 1L;

    private FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>> fileInputBootstrapFormComponentWrapper;

    /**
     * @param id
     * @param model
     */
    public FileInputBootstrapFormComponent(final String id, final IModel<Collection<FileMetadata>> model) {
        super(id, model);
    }

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public FileInputBootstrapFormComponent(final String id, final IModel<String> labelModel,
            final IModel<Collection<FileMetadata>> model) {
        super(id, labelModel, model);
    }

    /**
     * @param id
     */
    public FileInputBootstrapFormComponent(final String id) {
        super(id);
    }

    @Override
    public void enableRevisionsView(final Class<?> auditorClass,
                                    final EntityManager entityManager,
                                    final IModel<? extends GenericPersistable> owningEntityModel) {
        throw new NotImplementedException("");
    }

    @Override
    protected void getAjaxFormComponentUpdatingBehavior() {
        // do nothing;
    }

    public FileInputBootstrapFormComponent maxFiles(final int maxFiles) {
        field.maxFiles(maxFiles);
        return this;
    }

    @Override
    protected FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>> inputField(final String id,
            final IModel<Collection<FileMetadata>> model) {
        fileInputBootstrapFormComponentWrapper = new FileInputBootstrapFormComponentWrapper<>(id, initFieldModel());

        return fileInputBootstrapFormComponentWrapper;
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    public void setVisibleOnlyToAdmin(final Boolean visibleOnlyToAdmin) {
        fileInputBootstrapFormComponentWrapper.setVisibleOnlyToAdmin(visibleOnlyToAdmin);
    }

    public void setDisableDeleteButton(final Boolean disableDeleteButton) {
        fileInputBootstrapFormComponentWrapper.setDisableDeleteButton(disableDeleteButton);
    }

    public FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>>
            getFileInputBootstrapFormComponentWrapper() {
        return fileInputBootstrapFormComponentWrapper;
    }


    @Override
    public GenericBootstrapFormComponent<Collection<FileMetadata>,
            FileInputBootstrapFormComponentWrapper<Collection<FileMetadata>>> required() {
        field.requireAtLeastOneItem();
        return this;
    }
}
