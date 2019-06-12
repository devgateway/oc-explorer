package org.devgateway.toolkit.persistence.repository;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 6/22/16
 */
@Transactional
public interface AdminSettingsRepository extends BaseJpaRepository<AdminSettings, Long> {

}
