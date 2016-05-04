/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListReportProvider;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * @author mpostelnicu
 *
 */
@Service
public class JsonSchemaValidationService {
	private final Logger logger = LoggerFactory.getLogger(JsonSchemaValidationService.class);
	private JsonSchema schema;

	public static final String OCDS_1_0_SCHEMA_LOCATION = "/release-schema.json";

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	@PostConstruct
	private void init() {

		try {
	
			final JsonNode jsonNode = JsonLoader.fromResource(OCDS_1_0_SCHEMA_LOCATION);
			
			schema = JsonSchemaFactory.newBuilder()
					.setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL)).freeze()
					.getJsonSchema(jsonNode);			
			
		} catch (ProcessingException | IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public ProcessingReport validate(final JsonNode jsonNode) {
		try {
			return schema.validate(jsonNode);
		} catch (ProcessingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public ProcessingReport validate(final Object object) {
		JsonNode value = jacksonObjectMapper.convertValue(object, JsonNode.class);
		return validate(value);
	}

	public <S> List<ProcessingReport> validateAll(final Collection<S> values) {
		return values.stream().map(this::validate).collect(Collectors.toList());
	}
	
	
}
