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
package org.devgateway.ocds.persistence.repository;

import java.util.Optional;

import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.category.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mpostelnicu
 *
 */
@Transactional
@RepositoryRestResource
@PreAuthorize("hasRole('ROLE_USER')")
public interface UserDashboardRepository extends TextSearchableRepository<UserDashboard, Long> {

    @Query("select p.defaultDashboard from Person p where p.username = ?1")
    @RestResource
    Optional<UserDashboard> findDefaultByUsername(String username);
    
    @Query("select p.dashboards from Person p where p.id = ?1")
    Page<UserDashboard> findDashboardsForPersonId(long userId, 
            Pageable pageable);

    @Query("select count(d) from UserDashboard d where d.user.id = ?1")
    long countDashboardsForPersonId(long userId);
    
    @Override
    @Query("select e from  #{#entityName} e where lower(e.name) like %:code%")
    Page<UserDashboard> searchText(@Param("code") String code, Pageable page);

}
