package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@CacheConfig(cacheNames = "servicesCache")
public abstract class BaseJpaService<T extends GenericPersistable & Serializable> {

    @Cacheable(key = "{ #root.targetClass, #root.methodName }")
    public List<T> findAll() {
        return repository().findAll();
    }

    public List<T> findAll(final Sort sort) {
        return repository().findAll(sort);
    }

    public List<T> findAll(final Specification<T> spec) {
        return repository().findAll(spec);
    }

    public Page<T> findAll(final Specification<T> spec, final Pageable pageable) {
        return repository().findAll(spec, pageable);
    }

    public List<T> findAll(final Specification<T> spec, final Sort sort) {
        return repository().findAll(spec, sort);
    }

    public long count(final Specification<T> spec) {
        return repository().count(spec);
    }

    public Optional<T> findById(final Long id) {
        return repository().findById(id);
    }

    public long count() {
        return repository().count();
    }

    @Transactional(readOnly = false)
    public <S extends T> S save(final S entity) {
        return repository().save(entity);
    }

    @Transactional(readOnly = false)
    public <S extends T> S saveAndFlush(final S entity) {
        return repository().saveAndFlush(entity);
    }

    @Transactional(readOnly = false)
    public void delete(final T entity) {
        repository().delete(entity);
    }

    protected abstract BaseJpaRepository<T, Long> repository();

    public abstract Optional<T> newInstance();
}
