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
package org.devgateway.toolkit.persistence.spring;

import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.apache.catalina.startup.Tomcat;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Run this application only when you need access to Spring Data JPA but without
 * Wicket frontend
 *
 * @author mpostelnicu
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = { RoleRepository.class, UserDashboardRepository.class })
@EnableTransactionManagement
@EntityScan(basePackageClasses = { GenericPersistable.class, UserDashboard.class })
@PropertySource("classpath:/org/devgateway/toolkit/persistence/application.properties")
@ComponentScan("org.devgateway.toolkit")
public class PersistenceApplication {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(PersistenceApplication.class, args);
    }

    @Bean
    @Profile("!integration")
    public TomcatServletWebServerFactory tomcatFactory(@Qualifier("liquibaseAfterJPA") final
                                                       SpringLiquibaseRunner liquibaseAfterJPA) {
        logger.info("Instantiating tomcat after initialization of " + liquibaseAfterJPA);
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(final Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }
        };
    }
}