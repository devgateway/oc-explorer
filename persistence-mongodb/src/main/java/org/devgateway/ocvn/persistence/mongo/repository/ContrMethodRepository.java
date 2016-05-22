package org.devgateway.ocvn.persistence.mongo.repository;

import java.util.List;

import org.devgateway.ocvn.persistence.mongo.dao.ContrMethod;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

@CacheConfig(cacheNames = "contrMethods")
public interface ContrMethodRepository extends MongoRepository<ContrMethod, Integer> {

	@Cacheable
	@Override
	ContrMethod findOne(Integer id);

	@Override
	@CacheEvict(allEntries = true)
	<S extends ContrMethod> List<S> save(Iterable<S> entites);

}
