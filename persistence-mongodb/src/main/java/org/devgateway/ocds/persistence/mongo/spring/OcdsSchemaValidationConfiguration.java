package org.devgateway.ocds.persistence.mongo.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OcdsSchemaValidationConfiguration {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Bean
    public OcdsSchemaValidation ocdsSchemaValidation() {
        OcdsSchemaValidation jsonSchemaValidation = new OcdsSchemaValidation(jacksonObjectMapper);
        jsonSchemaValidation
               	.withJsonPatches(OcdsSchemaValidation.OCDS_1_0_LOCATION_PATCH_LOCATION)
                .init();
        return jsonSchemaValidation;
    }

}
