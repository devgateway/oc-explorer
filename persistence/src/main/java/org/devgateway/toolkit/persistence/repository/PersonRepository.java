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
package org.devgateway.toolkit.persistence.repository;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface PersonRepository extends BaseJpaRepository<Person, Long>, TextSearchableRepository<Person, Long> {

    @Query("select p from Person p where p.username = ?1")
    List<Person> findByName(String username);

    Person findByUsername(String username);

    Person findByEmail(String email);

    @Override
    @Query("select p from Person p where lower(p.firstName) like %:name% or lower(p.lastName) like %:name%")
    Page<Person> searchText(@Param("name")  String name, Pageable page);
}