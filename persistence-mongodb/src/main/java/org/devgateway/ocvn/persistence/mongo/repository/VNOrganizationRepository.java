package org.devgateway.ocvn.persistence.mongo.repository;

import java.util.List;

import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

@CacheConfig(cacheNames = "organizations")
public interface VNOrganizationRepository extends MongoRepository<VNOrganization, String> {

	@Cacheable
	@Override
	VNOrganization findOne(String id);

	@Override
	@CacheEvict(allEntries = true)
	<S extends VNOrganization> S save(S entity);

	@Override
	@CacheEvict(allEntries = true)
	<S extends VNOrganization> List<S> save(Iterable<S> entites);
	
	@Override
	@CacheEvict(allEntries = true)
	<S extends VNOrganization> List<S> insert(Iterable<S> entities);
	
	@Override
	@CacheEvict(allEntries = true)
	<S extends VNOrganization> S insert(S entity);

}
