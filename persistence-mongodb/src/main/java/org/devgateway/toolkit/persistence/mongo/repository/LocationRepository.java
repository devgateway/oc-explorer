package org.devgateway.toolkit.persistence.mongo.repository;

import java.util.List;

import org.devgateway.toolkit.persistence.mongo.dao.Location;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

@CacheConfig(cacheNames = "locations")
public interface LocationRepository extends MongoRepository<Location, String> {

	@Cacheable
	Location findByName(String name);

	@Override
	@CacheEvict(allEntries = true)
	<S extends Location> List<S> save(Iterable<S> entites);

	@Override
	@CacheEvict(allEntries = true)
	<S extends Location> S save(S entity);
}
