package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassificationRepository extends MongoRepository<Classification, String> {
	
	public Classification findById(String id);
}
