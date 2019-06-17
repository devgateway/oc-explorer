/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.persistence.spring;

import net.sf.ehcache.management.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.management.MBeanServer;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@EnableCaching
@Profile("!integration")
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class CacheConfiguration {
    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }


    @Autowired(required = false)
    private MBeanServer mbeanServer;

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        final EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager(final EhCacheManagerFactoryBean factory) {
        return new EhCacheCacheManager(factory.getObject());
    }

    @Bean(destroyMethod = "dispose", initMethod = "init")
    public ManagementService ehCacheManagementService(final EhCacheManagerFactoryBean factory) {
        final ManagementService managementService =
                new ManagementService(factory.getObject(), mbeanServer, true, true, true, true);
        return managementService;
    }
}
