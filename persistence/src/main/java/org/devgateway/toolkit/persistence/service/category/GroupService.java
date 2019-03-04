package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.repository.category.GroupRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class GroupService extends BaseJpaService<Group> implements TextSearchableService<Group> {
    @Autowired
    private GroupRepository groupRepository;

    @Override
    protected BaseJpaRepository<Group, Long> repository() {
        return groupRepository;
    }

    @Override
    public TextSearchableRepository<Group, Long> textRepository() {
        return groupRepository;
    }

    @Override
    public Optional<Group> newInstance() {
        return Optional.of(new Group());
    }
}
