package org.devgateway.toolkit.persistence.mongo.repository;

import java.util.List;

import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@CacheConfig(cacheNames = "organizations")
public interface VNOrganizationRepository extends MongoRepository<VNOrganization, String> {

	@Cacheable
	//@Query(value = "{ $or : [ { 'identifier._id' : ?0 }, { 'additionalIdentifiers._id' : ?0 }]}")
	@Query(value = "{ 'identifier._id' : ?0 }")
	public VNOrganization findById(String id);

	@Override
	@CacheEvict(allEntries=true)	
	public <S extends VNOrganization> S save(S entity);

	@Override	
	@CacheEvict(allEntries=true)
	public <S extends VNOrganization> List<S> save(Iterable<S> entites);

}
