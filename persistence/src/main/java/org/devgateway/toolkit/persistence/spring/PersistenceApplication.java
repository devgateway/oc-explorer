package org.devgateway.toolkit.persistence.spring;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Run this application only when you need access to Spring Data JPA but without Wicket frontend
 * @author mpostelnicu
 *
 */
@EntityScan(basePackageClasses = GenericPersistable.class)
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = PersonRepository.class)
@EnableTransactionManagement
@Import({ org.devgateway.toolkit.persistence.spring.CacheConfiguration.class,
	org.devgateway.toolkit.persistence.spring.DatabaseConfiguration.class })
public class PersistenceApplication {
	

    public static void main(String[] args) {
        SpringApplication.run(PersistenceApplication.class, args);
    }
}