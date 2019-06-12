package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author idobre
 * @since 2019-03-04
 * <p>
 * This interface is usually implemented by a class that already extends {@link BaseJpaService} so no need to
 * implement
 * any methods beside {@link #textRepository}. The defaul method {@link #searchText} should be enough
 * for normal use cases.
 */
public interface TextSearchableService<T extends GenericPersistable & Serializable> {
    TextSearchableRepository<T, Long> textRepository();

    Page<T> findAll(Pageable pageable);

    <S extends T> S save(S entity);

    Optional<T> findById(Long id);

    /**
     * Default implementation of {@link TextSearchableRepository#searchText}.
     */
    default Page<T> searchText(final String code, final Pageable page) {
        return textRepository().searchText(code, page);
    }
}
