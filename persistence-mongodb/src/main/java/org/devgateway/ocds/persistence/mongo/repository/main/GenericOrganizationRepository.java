package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface GenericOrganizationRepository<T extends Organization> extends MongoRepository<T, String> {

    Optional<T> findById(String s);

    T findByIdOrNameAllIgnoreCase(String id, String name);
    
    @Query(value = "{'additionalIdentifiers._id': ?0}")
    T findByAllIds(String id);

}
