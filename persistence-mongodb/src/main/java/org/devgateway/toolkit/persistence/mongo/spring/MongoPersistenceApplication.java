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

import java.math.BigDecimal;
import java.util.Arrays;

import org.devgateway.ocvn.persistence.mongo.ocds.BigDecimal2;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
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
@EnableMongoRepositories(basePackageClasses = ReleaseRepository.class)
public class MongoPersistenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoPersistenceApplication.class, args);
	}

	public static enum BigDecimal2ToDoubleConverter implements Converter<BigDecimal2, Double> {
		INSTANCE;

		public Double convert(BigDecimal2 source) {
			return source == null ? null : source.doubleValue();
		}
	}

	public static enum DoubleToBigDecimal2Converter implements Converter<Double, BigDecimal2> {
		INSTANCE;

		public BigDecimal2 convert(Double source) {
			return source != null ? new BigDecimal2(source) : null;
		}
	}

	public static enum BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {
		INSTANCE;

		public Double convert(BigDecimal source) {
			return source == null ? null : source.doubleValue();
		}
	}

	public static enum DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {
		INSTANCE;

		public BigDecimal convert(Double source) {
			return source != null ? new BigDecimal(source) : null;
		}
	}

	@Bean
	public CustomConversions customConversions() {
		return new CustomConversions(Arrays
				.asList(new Object[] { BigDecimal2ToDoubleConverter.INSTANCE, DoubleToBigDecimal2Converter.INSTANCE,
						BigDecimalToDoubleConverter.INSTANCE, DoubleToBigDecimalConverter.INSTANCE }));
	}
	
	@Bean
	public CascadeSaveMongoEventListener cascadeSaveMongoEventListener() {
	    return new CascadeSaveMongoEventListener();
	}

}