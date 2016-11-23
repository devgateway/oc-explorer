package org.devgateway.ocds.persistence.mongo.repository;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericOrganizationRepository<T extends Organization> extends MongoRepository<T, String> {

    T findOne(String id);

    T findByIdOrNameAllIgnoreCase(String id, String name);
    
    @Query(value = "{ $or: [ {'_id' : ?0 }, " + "{'additionalIdentifiers.identifier._id': ?0} ] }")
    T findByAllIds(String id);

}
