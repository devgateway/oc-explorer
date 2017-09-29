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

import java.util.List;
import org.devgateway.ocds.persistence.dao.ColorIndicatorPair;
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
 * @author mpostelnicu
 */
@Transactional
@RepositoryRestResource
public interface ColorIndicatorPairRepository extends TextSearchableRepository<ColorIndicatorPair, Long> {

    @Override
    List<ColorIndicatorPair> findAll();

    @Override
    Page<ColorIndicatorPair> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Long id);

    @Query("select e from #{#entityName} e where (firstIndicator=:firstIndicator and "
            + "secondIndicator=:secondIndicator ) or (firstIndicator=:secondIndicator and "
            + "secondIndicator=:firstIndicator)")
    ColorIndicatorPair findByFirstIndicatorAndSecondIndicator(@Param("firstIndicator") String firstIndicator,
                                                 @Param("secondIndicator") String secondIndicator);

    @Override
    @Query("select e from  #{#entityName} e where lower(e.firstIndicator) like %:txt% OR "
            + "lower(e.secondIndicator) like %:txt%")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<ColorIndicatorPair> searchText(@Param("txt") String txt, Pageable page);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();

    @RestResource(exported = true)
    @Override
    ColorIndicatorPair getOne(Long id);

}
