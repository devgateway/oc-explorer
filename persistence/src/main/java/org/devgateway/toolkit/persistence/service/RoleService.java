package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;

public interface RoleService extends BaseJpaService<Role>, TextSearchableService<Role> {
    TextSearchableRepository<Role, Long> textRepository();

}
