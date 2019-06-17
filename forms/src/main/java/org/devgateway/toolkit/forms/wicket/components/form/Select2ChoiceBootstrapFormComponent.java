/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2BootstrapTheme;
import org.wicketstuff.select2.Select2Choice;

import javax.persistence.EntityManager;

/**
 * @author mpostelnicu
 *
 */
public class Select2ChoiceBootstrapFormComponent<TYPE>
        extends GenericEnablingBootstrapFormComponent<TYPE, Select2Choice<TYPE>> {
    private static final long serialVersionUID = -3430670677135618576L;

    public Select2ChoiceBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                               final IModel<TYPE> model, final ChoiceProvider<TYPE> choiceProvider) {
        super(id, labelModel, model);
        provider(choiceProvider);
    }

    public Select2ChoiceBootstrapFormComponent<TYPE> provider(final ChoiceProvider<TYPE> choiceProvider) {
        field.setProvider(choiceProvider);
        return this;
    }

    public Select2ChoiceBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                               final ChoiceProvider<TYPE> choiceProvider) {
        this(id, labelModel, null, choiceProvider);
    }

    public Select2ChoiceBootstrapFormComponent(final String id, final ChoiceProvider<TYPE> choiceProvider,
                                               final IModel<TYPE> model) {
        super(id, model);
        provider(choiceProvider);
    }

    public Select2ChoiceBootstrapFormComponent(final String id, final ChoiceProvider<TYPE> choiceProvider) {
        super(id);
        provider(choiceProvider);
    }

    @Override
    protected Select2Choice<TYPE> inputField(final String id, final IModel<TYPE> model) {
        return new Select2Choice<TYPE>(id, initFieldModel());
    }

    @Override
    public String getUpdateEvent() {
        return "change";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.devgateway.toolkit.forms.wicket.components.form.
     * GenericBootstrapFormComponent#onConfigure()
     */
    @Override
    protected void onInitialize() {
        field.getSettings().setPlaceholder("Click to select");
        field.getSettings().setAllowClear(true);
        field.getSettings().setCloseOnSelect(true);
        field.getSettings().setDropdownAutoWidth(true);
        field.getSettings().setTheme(new Select2BootstrapTheme(false));
        super.onInitialize();
    }

    @Override
    public void enableRevisionsView(final Class<?> auditorClass,
                                    final EntityManager entityManager,
                                    final IModel<? extends GenericPersistable> owningEntityModel) {
        throw new NotImplementedException("");
    }

    @Override
    protected boolean boundComponentsVisibilityAllowed(final TYPE selectedValue) {
        return false;
    }
}
