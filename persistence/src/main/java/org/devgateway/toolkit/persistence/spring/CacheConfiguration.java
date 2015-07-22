/**
 * 
 */
package org.devgateway.toolkit.persistence.spring;

import javax.management.MBeanServer;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author mpostelnicu
 *
 */
@Configuration
public class CacheConfiguration {
	

	@Autowired
	private MBeanServer mbeanServer;

	@Autowired
	private CacheManager cacheManager;
	
	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}
	
    
//	@Bean(destroyMethod="dispose",initMethod="init")
//	//-- also enable statistics in ehcache.xml
//	@DependsOn(value = {"ehCacheManagerFactoryBean","mbeanServer"})
//	public ManagementService ehCacheManagementService() {
//		ManagementService managementService = new ManagementService(cacheManager, mbeanServer, true, true, true, true);
//		return managementService;
//	}

}
