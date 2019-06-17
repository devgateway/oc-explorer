package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.repository.TestFormRepository;
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
public class TestFormServiceImpl extends BaseJpaServiceImpl<TestForm> implements TestFormService {
    @Autowired
    private TestFormRepository testFormRepository;

    @Override
    protected BaseJpaRepository<TestForm, Long> repository() {
        return testFormRepository;
    }

    @Override
    public TestForm newInstance() {
        return new TestForm();
    }
}
