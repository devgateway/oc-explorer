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

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.devgateway.ocvn.persistence.mongo.ocds.BigDecimal2;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.toolkit.persistence.mongo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

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
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MongoPersistenceApplication.class, args);
	}

	public static enum BigDecimal2ToStringConverter implements Converter<BigDecimal2, String> {
		INSTANCE;

		public String convert(BigDecimal2 source) {
			return source == null ? null : source.toString();
		}
	}

	public static enum StringToBigDecimal2Converter implements Converter<String, BigDecimal2> {
		INSTANCE;

		public BigDecimal2 convert(String source) {
			return StringUtils.hasText(source) ? new BigDecimal2(source) : null;
		}
	}

	@Bean
	public CustomConversions customConversions() {
		return new CustomConversions(Arrays
				.asList(new Object[] { BigDecimal2ToStringConverter.INSTANCE, StringToBigDecimal2Converter.INSTANCE }));
	}

	

	@PostConstruct
	public void addMongoIndex() {
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.bidNo", Direction.ASC));
	}
	


}