package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ClassificationRepository extends MongoRepository<Classification, String> {

	//@Cacheable(value="classifications", key="#id")
	public Classification findById(String id);
}
