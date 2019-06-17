package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.Classification;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "classifications")
public interface ClassificationRepository extends MongoRepository<Classification, String> {


    @Cacheable
    @Override
    Optional<Classification> findById(String s);

    @Override
    @CacheEvict(allEntries = true)
    <S extends Classification> List<S> saveAll(Iterable<S> entites);

    @Override
    @CacheEvict(allEntries = true)
    <S extends Classification> S save(S entity);

    @Override
    @CacheEvict(allEntries = true)
    <S extends Classification> List<S> insert(Iterable<S> entities);

    @Override
    @CacheEvict(allEntries = true)
    <S extends Classification> S insert(S entity);
}
