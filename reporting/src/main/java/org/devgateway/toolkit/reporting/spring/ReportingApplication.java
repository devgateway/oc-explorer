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
package org.devgateway.toolkit.reporting.spring;

import org.devgateway.toolkit.persistence.spring.DatabaseConfiguration;
import org.devgateway.toolkit.reporting.spring.util.ReportsCacheService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author mpostelnicu Simple configuration for mondrian backend module
 *
 */
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = ReportsCacheService.class)
@Import({ DatabaseConfiguration.class })
public class ReportingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReportingApplication.class, args);
    }
}