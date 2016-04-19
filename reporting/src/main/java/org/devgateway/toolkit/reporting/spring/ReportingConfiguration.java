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
package org.devgateway.toolkit.reporting.spring;

import org.apache.log4j.Logger;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@Profile("reports")
public class ReportingConfiguration {

	protected static Logger logger = Logger.getLogger(ReportingConfiguration.class);

	/**
	 * Bean that starts the mondrian engine
	 * 
	 * @return the {@link ClassicEngineBoot} instance
	 */
	@Bean
	@DependsOn(value = "dataSource")
	public ClassicEngineBoot classicEngineBoot() {
		ClassicEngineBoot instance = ClassicEngineBoot.getInstance();
		instance.start();
		return instance;
	}

}
