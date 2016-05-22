package org.devgateway.ocds.web.rest.serializers;

import java.io.IOException;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeoJsonPointSerializer extends JsonSerializer<GeoJsonPoint> {

	@Override
	public void serialize(final GeoJsonPoint value, final JsonGenerator gen, final SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getType());
		gen.writeArrayFieldStart("coordinates");
		gen.writeNumber(value.getX());
		gen.writeNumber(value.getY());
		gen.writeEndArray();
		gen.writeEndObject();
	}

}