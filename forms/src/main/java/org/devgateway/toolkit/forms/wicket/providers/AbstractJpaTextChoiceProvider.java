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

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author mpostelnicu
 */
public abstract class AbstractJpaTextChoiceProvider<T extends GenericPersistable & Labelable & Serializable>
        extends ChoiceProvider<T> {

    private static final long serialVersionUID = 5709987900445896586L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractJpaTextChoiceProvider.class);

    private T newObject;

    private Sort sort;

    protected Boolean addNewElements = false;

    private Class<T> clazz;

    private IModel<Collection<T>> restrictedToItemsModel;

    private final TextSearchableService<T> textSearchableService;

    public AbstractJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService) {
        this.textSearchableService = textSearchableService;
    }

    public AbstractJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService,
                                         final Class<T> clazz, final Boolean addNewElements) {
        this.textSearchableService = textSearchableService;
        this.clazz = clazz;
        this.addNewElements = addNewElements;
    }

    public AbstractJpaTextChoiceProvider(final TextSearchableService<T> textSearchableService,
                                         final IModel<Collection<T>> restrictedToItemsModel) {
        this(textSearchableService);
        this.restrictedToItemsModel = restrictedToItemsModel;
    }

    @Override
    public String getIdValue(final T choice) {
        // if the object is not null but it hasn't an ID return 0
        if (choice != null && choice.getId() == null && addNewElements) {
            return "0";
        }

        return choice.getId().toString();
    }

    @Override
    public void query(final String term, final int page, final Response<T> response) {
        Page<T> itemsByTerm;
        if (term == null || term.isEmpty()) {
            itemsByTerm = findAll(page);
            response.setHasMore(itemsByTerm.hasNext());
        } else {
            itemsByTerm = getItemsByTerm(term.toLowerCase(), page);
        }

        if (itemsByTerm != null) {
            if (itemsByTerm.getContent().size() == 0 && addNewElements) {
                // add new element dynamically
                // the new element should extend Category so that we can attache
                // a 'label' to it
                try {
                    newObject = clazz.newInstance();
                    newObject.setLabel(term);
                } catch (InstantiationException e) {
                    logger.error("Error creating a new Item", e);
                } catch (IllegalAccessException e) {
                    logger.error("Error creating a new Item", e);
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

    protected Page<T> getItemsByTerm(final String term, final int page) {
        final PageRequest pageRequest = sort == null
                ? PageRequest.of(page, WebConstants.SELECT_PAGE_SIZE)
                : PageRequest.of(page, WebConstants.SELECT_PAGE_SIZE, sort);
        return textSearchableService.searchText(term, pageRequest);
    }

    public Page<T> findAll(final int page) {
        final PageRequest pageRequest = sort == null
                ? PageRequest.of(page, WebConstants.SELECT_PAGE_SIZE)
                : PageRequest.of(page, WebConstants.SELECT_PAGE_SIZE, sort);
        return textSearchableService.findAll(pageRequest);
    }

    @Override
    public Collection<T> toChoices(final Collection<String> ids) {
        ArrayList<String> idsList = new ArrayList<>();

        for (String id : ids) {
            // create new element
            if (Long.parseLong(id) == 0 && addNewElements) {
                if (newObject != null && newObject.getId() == null) {
                    textSearchableService.save(newObject);
                }

                id = newObject.getId().toString();
            }

            idsList.add(id);
        }

        ArrayList<T> response = new ArrayList<>();
        for (String s : idsList) {
            Long id = Long.parseLong(s);
            Optional<T> findOne = textSearchableService.findById(id);
            if (!findOne.isPresent()) {
                logger.error("Cannot find entity with id=" + id + " in service "
                        + textSearchableService.getClass());
            } else {
                response.add(findOne.get());
            }
        }
        return response;
    }

}
