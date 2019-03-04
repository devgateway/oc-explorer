package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.repository.FileMetadataRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class FileMetadataService extends BaseJpaService<FileMetadata> {
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    public FileMetadata findByName(final String name) {
        return fileMetadataRepository.findByName(name);
    }

    @Override
    protected BaseJpaRepository<FileMetadata, Long> repository() {
        return fileMetadataRepository;
    }

    @Override
    public Optional<FileMetadata> newInstance() {
        return Optional.empty();
    }
}
