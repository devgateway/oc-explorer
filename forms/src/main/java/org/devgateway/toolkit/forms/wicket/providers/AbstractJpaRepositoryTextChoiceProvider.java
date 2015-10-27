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
package org.devgateway.toolkit.forms.wicket.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.repository.category.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author mpostelnicu
 *
 */
public abstract class AbstractJpaRepositoryTextChoiceProvider<T extends GenericPersistable & Labelable>
		extends TextChoiceProvider<T> {

	private static final long serialVersionUID = 5709987900445896586L;

	protected static Logger logger = Logger.getLogger(AbstractJpaRepositoryTextChoiceProvider.class);

	protected T newObject;

	protected Sort sort;

	protected Boolean addNewElements = false;

	private Class<T> clazz;

	protected IModel<Collection<T>> restrictedToItemsModel;

	protected TextSearchableRepository<T, Long> textSearchableRepository;

	public AbstractJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository) {
		this.textSearchableRepository = textSearchableRepository;
	}

	public AbstractJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository,
												   Class<T> clazz, Boolean addNewElements) {
		this.textSearchableRepository = textSearchableRepository;
		this.clazz = clazz;
		this.addNewElements = addNewElements;
	}

	public AbstractJpaRepositoryTextChoiceProvider(TextSearchableRepository<T, Long> textSearchableRepository,
												   IModel<Collection<T>> restrictedToItemsModel) {
		this(textSearchableRepository);
		this.restrictedToItemsModel = restrictedToItemsModel;
	}

	public TextSearchableRepository<T, Long> getTextSearchableRepository() {
		return textSearchableRepository;
	}

	@Override
	public Object getId(T choice) {
		// if the object is not null but it hasn't an ID return 0
		if (choice != null && choice.getId() == null && addNewElements) {
			return 0;
		}

		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<T> response) {
		Page<T> itemsByTerm;
		if (term.isEmpty()) {
            itemsByTerm = findAll(page);
        } else {
            itemsByTerm = getItemsByTerm(term.toLowerCase(), page);
        }
	
	
		if (itemsByTerm != null) {
			if (itemsByTerm.getContent().size() == 0 && addNewElements) {
				// add new element dynamically
				// the new element should extend Category so that we can attache a 'label' to it
				try {
					newObject = clazz.newInstance();
					newObject.setLabel(term);
				} catch (InstantiationException e) {
					logger.error(e);
				} catch (IllegalAccessException e) {
					logger.error(e);
				}

				List<T> newElementsList = new ArrayList<>();
				newElementsList.add(newObject);

				response.addAll(newElementsList);
			} else {
				response.setHasMore(itemsByTerm.hasNext());
				response.addAll(itemsByTerm.getContent());
			}
		}
	}

	protected Page<T> getItemsByTerm(String term, int page) {
		PageRequest pageRequest = new PageRequest(page, WebConstants.SELECT_PAGE_SIZE, sort);
            return getTextSearchableRepository().searchText(term, pageRequest);
	}

	public Page<T> findAll(int page) {
		PageRequest pageRequest = new PageRequest(page, WebConstants.SELECT_PAGE_SIZE, sort);
            return getTextSearchableRepository().findAll(pageRequest);
	}

	@Override
	public Collection<T> toChoices(Collection<String> ids) {
		ArrayList<String> idsList = new ArrayList<>();

		for (String id : ids) {
			// create new element
			if (Long.parseLong(id) == 0 && addNewElements) {
				if (newObject != null && newObject.getId() == null) {
					getTextSearchableRepository().save(newObject);
				}

				id = newObject.getId().toString();
			}

			idsList.add(id);
		}

		ArrayList<T> response = new ArrayList<>();
		for (String s : idsList) {
			Long id = Long.parseLong(s);
			T findOne = getTextSearchableRepository().findOne(id);
			if (findOne == null) {
				logger.error("Cannot find entity with id=" + id
						+ " in repository " + getTextSearchableRepository().getClass());
			} else {
				response.add(findOne);
			}
		}
		return response;
	}

}
