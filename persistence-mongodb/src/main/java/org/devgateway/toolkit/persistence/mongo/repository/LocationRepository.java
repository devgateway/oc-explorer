package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.toolkit.persistence.mongo.dao.Location;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {

	//@Cacheable(value="locations", key="#projectId")
	public Location findById(String projectId);

	//@Cacheable(value="locations", key="#name")
	public Location findByName(String name);
	
}
