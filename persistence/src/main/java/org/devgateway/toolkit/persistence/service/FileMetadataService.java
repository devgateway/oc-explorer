package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.FileMetadata;

public interface FileMetadataService extends BaseJpaService<FileMetadata> {
    FileMetadata findByName(String name);
}
