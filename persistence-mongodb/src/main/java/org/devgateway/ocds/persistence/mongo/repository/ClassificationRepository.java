package org.devgateway.ocds.persistence.mongo.repository;

import java.util.List;

import org.devgateway.ocds.persistence.mongo.Classification;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

@CacheConfig(cacheNames = "classifications")
public interface ClassificationRepository extends MongoRepository<Classification, String> {

	@Cacheable
	@Override
	Classification findOne(String id);

	@Override
	@CacheEvict(allEntries = true)
	<S extends Classification> List<S> save(Iterable<S> entites);

}
