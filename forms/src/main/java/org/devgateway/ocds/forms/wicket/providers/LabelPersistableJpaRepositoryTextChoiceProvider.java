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
package org.devgateway.ocds.forms.wicket.providers;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.Collection;

/**
 * @author mpostelnicu
 *
 */
public class LabelPersistableJpaRepositoryTextChoiceProvider<T extends GenericPersistable & Labelable>
        extends GenericPersistableJpaTextChoiceProvider<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -9109118476966448737L;

    public LabelPersistableJpaRepositoryTextChoiceProvider(
            final TextSearchableService<T> textSearchableRepository) {
        super(textSearchableRepository);
    }

    public LabelPersistableJpaRepositoryTextChoiceProvider(
            final TextSearchableService<T> textSearchableRepository,
            final IModel<Collection<T>> restrictedToItemsModel) {
        super(textSearchableRepository, restrictedToItemsModel);
    }

    public LabelPersistableJpaRepositoryTextChoiceProvider(
            final TextSearchableService<T> textSearchableRepository, final Class<T> clazz,
            final Boolean addNewElements) {
        super(textSearchableRepository, clazz, addNewElements);
    }

    @Override
    public String getDisplayValue(final T choice) {
        return choice.getLabel();
    }
}
