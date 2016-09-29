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
package org.devgateway.toolkit.persistence.mongo.spring;

import org.devgateway.toolkit.persistence.mongo.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Run this application only when you need access to Spring Data JPA but without
 * Wicket frontend
 * 
 * @author mpostelnicu
 *
 */
@SpringBootApplication
@ComponentScan("org.devgateway.toolkit")
@PropertySource("classpath:/org/devgateway/toolkit/persistence/mongo/application.properties")
@EnableMongoRepositories(basePackageClasses = CustomerRepository.class)
public class MongoPersistenceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MongoPersistenceApplication.class, args);
    }
}