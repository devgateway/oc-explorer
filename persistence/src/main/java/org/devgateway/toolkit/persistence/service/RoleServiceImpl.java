package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.repository.RoleRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseJpaServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected BaseJpaRepository<Role, Long> repository() {
        return roleRepository;
    }

    @Override
    public TextSearchableRepository<Role, Long> textRepository() {
        return roleRepository;
    }

    @Override
    public Role newInstance() {
        return new Role();
    }
}
