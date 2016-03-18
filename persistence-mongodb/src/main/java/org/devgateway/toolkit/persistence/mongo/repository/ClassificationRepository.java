package org.devgateway.toolkit.persistence.mongo.repository;

import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

@CacheConfig(cacheNames = "classifications")
public interface ClassificationRepository extends MongoRepository<Classification, String> {

	@Cacheable
	public Classification findById(String id);
	
	@Override
	@CacheEvict(allEntries=true)
	public <S extends Classification> List<S> save(Iterable<S> entites);
	
}
