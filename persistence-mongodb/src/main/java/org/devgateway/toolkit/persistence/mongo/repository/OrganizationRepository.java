package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OrganizationRepository extends MongoRepository<Organization, String> {

	@Query(value = "{ $or : [ { 'identifier._id' : ?0 }, { 'additionalIdentifiers._id' : ?0 }]}")
	public Organization findById(String id);
}
