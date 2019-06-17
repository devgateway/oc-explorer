package org.devgateway.toolkit.persistence.service;

import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserDashboardService extends BaseJpaService<UserDashboard>, TextSearchableService<UserDashboard> {

    Page<UserDashboard> findDashboardsForPersonId(long userId, Pageable pageable);


    UserDashboard getDefaultDashboardForPersonId(long userId);

    @Override
    Page<UserDashboard> searchText(String code, Pageable page);

    @Override
    List<UserDashboard> findAll();

    @Override
    Page<UserDashboard> findAll(Pageable pageable);

    void deleteById(Long id);

    void deleteAll();

    @Override
    Optional<UserDashboard> findOne(Specification<UserDashboard> spec);
}
