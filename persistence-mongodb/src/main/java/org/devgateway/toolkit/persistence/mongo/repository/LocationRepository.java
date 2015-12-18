package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.toolkit.persistence.mongo.dao.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {

	public Location findById(String projectId);

	public Location findByName(String name);
	
}
