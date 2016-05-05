/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.toolkit.persistence.spring;

import javax.management.MBeanServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

	@Autowired
	private MBeanServer mbeanServer;

	@Autowired
	private CacheManager cacheManager;

	@Bean
	@Profile("prod")
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBeanProd() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache-prod.xml"));
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

	@Bean
	@Profile({ "integration", "dev" })
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBeanDev() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache-dev.xml"));
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

	@Bean(destroyMethod = "dispose", initMethod = "init")
	@Profile("!integration")
	public ManagementService ehCacheManagementService() {
		ManagementService managementService = new ManagementService(cacheManager, mbeanServer, true, true, true, true);
		return managementService;
	}

}
