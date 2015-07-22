package org.devgateway.toolkit.persistence.repository;

import java.util.Set;

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 1/7/15
 */

@Transactional
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    @Query("select file from FileMetadata file where file.isUserSupportDocument = cast('true' as boolean)")
    Set<FileMetadata> findByIsUserSupportDocumentTrue();
}
