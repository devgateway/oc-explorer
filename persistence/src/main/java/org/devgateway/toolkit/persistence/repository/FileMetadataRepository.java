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

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author idobre
 * @since 1/7/15
 */

@Transactional
public interface FileMetadataRepository extends BaseJpaRepository<FileMetadata, Long> {

    @Query("select file from FileMetadata file where file.isUserSupportDocument = cast('true' as boolean)")
    Set<FileMetadata> findByIsUserSupportDocumentTrue();

    FileMetadata findByName(String name);
}
