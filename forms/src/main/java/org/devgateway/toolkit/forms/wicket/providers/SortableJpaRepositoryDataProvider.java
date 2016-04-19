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

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.models.PersistableJpaRepositoryModel;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mpostelnicu
 *
 *         Smart generic {@link SortableDataProvider} that binds to
 *         {@link JpaRepository}
 */
public class SortableJpaRepositoryDataProvider<T extends GenericPersistable> extends SortableDataProvider<T, String> {

	private static final long serialVersionUID = 6507887810859971417L;

	protected JpaRepository<T, Long> jpaRepository;

	/**
	 * Always provide a proxy jpaRepository here! For example one coming from a
	 * {@link SpringBean}
	 *
	 * @param jpaRepository
	 */
	public SortableJpaRepositoryDataProvider(final JpaRepository<T, Long> jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	/**
	 * Translates from a {@link SortParam} to a Spring {@link Sort}
	 *
	 * @return
	 */
	protected Sort translateSort() {
		if (getSort() == null) {
			return null;
		}
		return new Sort(getSort().isAscending() ? Direction.ASC : Direction.DESC, getSort().getProperty());
	}

	/**
	 * @see SortableDataProvider#iterator(long, long)
	 */
	@Override
	public Iterator<? extends T> iterator(final long first, final long count) {
		int page = (int) ((double) first / WebConstants.PAGE_SIZE);
		Page<T> findAll = jpaRepository.findAll(new PageRequest(page, WebConstants.PAGE_SIZE, translateSort()));
		return findAll.iterator();
	}

	@Override
	public long size() {
		return jpaRepository.count();
	}

	/**
	 * This ensures that the object is detached and reloaded after
	 * deserialization of the page, since the
	 * {@link PersistableJpaRepositoryModel} is also loadabledetachable
	 */
	@Override
	public IModel<T> model(final T object) {
		return new PersistableJpaRepositoryModel<T>(object, jpaRepository);
	}

}
