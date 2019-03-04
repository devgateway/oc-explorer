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
package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author mpostelnicu
 *
 */
public class GenericPersistableJpaTextChoiceProvider<T extends GenericPersistable & Labelable & Serializable>
        extends AbstractJpaTextChoiceProvider<T> {
    private static final long serialVersionUID = -643286578944834690L;

    public GenericPersistableJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService) {
        super(textSearchableService);
    }

    public GenericPersistableJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService,
                                                   final IModel<Collection<T>> restrictedToItemsModel) {
        super(textSearchableService, restrictedToItemsModel);
    }

    public GenericPersistableJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService,
                                                   final Class<T> clazz, final Boolean addNewElements) {
        super(textSearchableService, clazz, addNewElements);
    }

    @Override
    public String getDisplayValue(final T choice) {
        if (addNewElements && choice.getId() == null) {
            return choice.toString() + " ---> (press enter to create new element)";
        }

        return choice.toString();
    }
}
