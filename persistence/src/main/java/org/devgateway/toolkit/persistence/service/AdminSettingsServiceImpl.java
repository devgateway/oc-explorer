package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
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
public class AdminSettingsServiceImpl extends BaseJpaServiceImpl<AdminSettings> implements AdminSettingsService {
    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    @Override
    protected BaseJpaRepository<AdminSettings, Long> repository() {
        return adminSettingsRepository;
    }

    @Override
    public AdminSettings newInstance() {
        return new AdminSettings();
    }

}
