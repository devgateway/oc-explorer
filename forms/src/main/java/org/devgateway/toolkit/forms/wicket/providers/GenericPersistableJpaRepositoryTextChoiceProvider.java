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
package org.devgateway.toolkit.forms.wicket.providers;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.repository.category.TextSearchableRepository;

/**
 * @author mpostelnicu
 *
 */
public class GenericPersistableJpaRepositoryTextChoiceProvider<T extends GenericPersistable & Labelable> extends
		AbstractJpaRepositoryTextChoiceProvider<T> {
	private static final long serialVersionUID = -643286578944834690L;
	
	public GenericPersistableJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository) {
		super(textSearchableRepository);
	}

	public GenericPersistableJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository,IModel<Collection<T>> restrictedToItemsModel) {
		super(textSearchableRepository, restrictedToItemsModel);
	}

	public GenericPersistableJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository,
										 Class<T> clazz, Boolean addNewElements) {
		super(textSearchableRepository, clazz, addNewElements);
	}

	/* (non-Javadoc)
	 * @see com.vaynberg.wicket.select2.TextChoiceProvider#getDisplayText(java.lang.Object)
	 */
	@Override
	public String getDisplayValue(T choice) {
		if (addNewElements && choice.getId() == null) {
			return choice.toString() + " ---> (press enter to create new element)";
		}

		return choice.toString();
	}
}
