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
package org.devgateway.toolkit.web.spring;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.devgateway.ocds.web.cache.generators.GenericPagingRequestKeyGenerator;
import org.devgateway.ocds.web.rest.serializers.GeoJsonPointSerializer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		builder.serializationInclusion(Include.NON_EMPTY).dateFormat(dateFormatGmt);
		builder.serializerByType(GeoJsonPoint.class, new GeoJsonPointSerializer());

		return builder;
	}
	
	@Bean(name = "genericPagingRequestKeyGenerator")
	public KeyGenerator genericPagingRequestKeyGenerator(ObjectMapper objectMapper) {
		return new GenericPagingRequestKeyGenerator(objectMapper);
	}

}
