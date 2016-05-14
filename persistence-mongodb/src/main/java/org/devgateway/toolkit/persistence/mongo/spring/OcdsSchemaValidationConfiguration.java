package org.devgateway.toolkit.persistence.mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class OcdsSchemaValidationConfiguration {

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	@Bean
	public OcdsSchemaValidation ocdsSchemaValidation() {
		OcdsSchemaValidation jsonSchemaValidation = new OcdsSchemaValidation(jacksonObjectMapper);
		jsonSchemaValidation
		//.withJsonPatches(OcdsSchemaValidation.OCDS_1_0_LOCATION_PATCH_LOCATION)
		.init();		
		return jsonSchemaValidation;
	}

}
