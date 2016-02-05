package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VNOrganizationRepository extends MongoRepository<VNOrganization, String> {

	@Query(value = "{ $or : [ { 'identifier._id' : ?0 }, { 'additionalIdentifiers._id' : ?0 }]}")
	public VNOrganization findById(String id);
}
