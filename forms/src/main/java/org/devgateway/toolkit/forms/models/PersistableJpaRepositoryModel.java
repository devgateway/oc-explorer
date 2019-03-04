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
package org.devgateway.toolkit.forms.models;

import nl.dries.wicket.hibernate.dozer.DozerModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaServiceDataProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.io.Serializable;

/**
 * USE THIS ONLY FOR {@link SortableJpaServiceDataProvider}S Use
 * {@link DozerModel} for editing complex forms
 *
 * @param <T> the type of the entity to be accessed
 * @author mpostelnicu
 */
public class PersistableJpaRepositoryModel<T extends GenericPersistable & Serializable>
        extends LoadableDetachableModel<T> {
    private static final long serialVersionUID = -3668189792112474025L;

    private final Long id;

    private final BaseJpaService<T> jpaService;

    public PersistableJpaRepositoryModel(final Long id, final BaseJpaService<T> jpaService) {
        super();
        this.id = id;
        this.jpaService = jpaService;
    }

    public PersistableJpaRepositoryModel(final T t, final BaseJpaService<T> jpaService) {
        super(t);
        this.id = t.getId();
        this.jpaService = jpaService;
    }

    @Override
    protected T load() {
        return jpaService.findById(id).orElse(null);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof PersistableJpaRepositoryModel) {
            final PersistableJpaRepositoryModel<T> other = (PersistableJpaRepositoryModel<T>) obj;
            return other.id.equals(id);
        }
        return false;
    }
}