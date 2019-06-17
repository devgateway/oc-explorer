package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseJpaService<T extends GenericPersistable & Serializable> {
    List<T> findAll();

    List<T> findAll(Sort sort);

    List<T> findAll(Specification<T> spec);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    Page<T> findAll(Pageable pageable);

    List<T> findAll(Specification<T> spec, Sort sort);

    Optional<T> findOne(@Nullable Specification<T> spec);

    long count(Specification<T> spec);

    Optional<T> findById(Long id);

    long count();

    <S extends T> S save(S entity);

    <S extends T> S saveAndFlush(S entity);

    void delete(T entity);

    T newInstance();
}
