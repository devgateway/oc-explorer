package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseJpaService<T extends GenericPersistable & Serializable> {
    @Cacheable
    List<T> findAll();

    @Cacheable
    List<T> findAll(Sort sort);

    @Cacheable
    List<T> findAll(Specification<T> spec);

    @Cacheable
    Page<T> findAll(Specification<T> spec, Pageable pageable);

    @Cacheable
    Page<T> findAll(Pageable pageable);

    @Cacheable
    List<T> findAll(Specification<T> spec, Sort sort);

    @Cacheable
    long count(Specification<T> spec);

    @Cacheable
    Optional<T> findById(Long id);

    @Cacheable
    long count();

    @Transactional(readOnly = false)
    <S extends T> S save(S entity);

    @Transactional(readOnly = false)
    <S extends T> S saveAndFlush(S entity);

    @Transactional(readOnly = false)
    void delete(T entity);

    T newInstance();
}
