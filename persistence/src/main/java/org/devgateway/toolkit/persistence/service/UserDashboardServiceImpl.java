package org.devgateway.toolkit.persistence.service;

import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserDashboardServiceImpl extends BaseJpaServiceImpl<UserDashboard> implements UserDashboardService {

    @Autowired
    private UserDashboardRepository repository;

    @Override
    protected BaseJpaRepository<UserDashboard, Long> repository() {
        return repository;
    }

    @Override
    public Page<UserDashboard> findDashboardsForPersonId(long userId, Pageable pageable) {
        return repository.findDashboardsForPersonId(userId, pageable);
    }

    @Override
    public UserDashboard getDefaultDashboardForPersonId(long userId) {
        return repository.getDefaultDashboardForPersonId(userId);
    }

    @Override
    public TextSearchableRepository<UserDashboard, Long> textRepository() {
        return repository;
    }

    @Override
    public Page<UserDashboard> searchText(String code, Pageable page) {
        return repository.searchText(code, page);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public UserDashboard newInstance() {
        return new UserDashboard();
    }
}
