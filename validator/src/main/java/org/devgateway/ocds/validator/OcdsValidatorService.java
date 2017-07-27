package org.devgateway.ocds.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListReportProvider;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by mpostelnicu on 7/5/17.
 */
@Service
public class OcdsValidatorService {


    private Map<String, JsonSchema> keySchema = new ConcurrentHashMap<>();

    private Map<String, String> schemaNamePrefix = new ConcurrentHashMap<>();

    private Map<String, JsonNode> extensionMeta = new ConcurrentHashMap<>();

    private Map<String, JsonNode> extensionJson = new ConcurrentHashMap<>();


    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private JsonNode getUnmodifiedSchemaNode(OcdsValidatorRequest request) {
        try {
            return JsonLoader.fromResource(schemaNamePrefix.get(request.getSchemaType()) + request.getVersion()
                    + OcdsValidatorConstants.SCHEMA_POSTFIX);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JsonNode applyExtensions(JsonNode schemaNode, OcdsValidatorRequest request) {
        if (ObjectUtils.isEmpty(request.getExtensions())) {
            return schemaNode;
        }

        List<String> unrecognizedExtensions =
                request.getExtensions().stream().filter(e -> !OcdsValidatorConstants.EXTENSIONS.contains(e))
                        .collect(Collectors.toList());

        if (!unrecognizedExtensions.isEmpty()) {
            throw new RuntimeException("Unknown extensions by name: " + unrecognizedExtensions);
        }


    }

    public JsonNode readExtensionData(OcdsValidatorRequest request, String extensionName) {
        //reading meta
        JsonLoader.fromResource();
    }

    private JsonSchema getSchema(OcdsValidatorRequest request) {
        if (keySchema.containsKey(request.getKey())) {
            return keySchema.get(request.getKey());
        } else {
            JsonNode schemaNode = getUnmodifiedSchemaNode(request);
            //TODO: this is where we should apply extensions !

            try {
                JsonSchema schema = JsonSchemaFactory.newBuilder()
                        .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL)).freeze()
                        .getJsonSchema(schemaNode);
                keySchema.put(request.getKey(), schema);
                return schema;
            } catch (ProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    private void initSchemaNamePrefix() {
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RELEASE, OcdsValidatorConstants.SchemaPrefixes.RELEASE);
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RECORD_PACKAGE,
                OcdsValidatorConstants.SchemaPrefixes.RECORD_PACKAGE);
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RELEASE_PACKAGE,
                OcdsValidatorConstants.SchemaPrefixes.RELEASE_PACKAGE);
    }

    private void initExtensions() {

    }

    @PostConstruct
    private void init() {
        initSchemaNamePrefix();
    }


    public ProcessingReport validate(OcdsValidatorApiRequest request) {

        OcdsValidatorNodeRequest nodeRequest = convertApiRequestToNodeRequest(request);

        if (nodeRequest.getSchemaType().equals(OcdsValidatorConstants.Schemas.RELEASE)) {
            return validateRelease(nodeRequest);
        }

        return null;

    }

    private JsonNode getJsonNodeFromString(String json) {
        try {
            return JsonLoader.fromString(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JsonNode getJsonNodeFromUrl(String url) {
        try {
            return JsonLoader.fromURL(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates a release or an array of releases
     *
     * @param nodeRequest
     * @return
     */
    private ProcessingReport validateRelease(OcdsValidatorNodeRequest nodeRequest) {
        JsonSchema schema = getSchema(nodeRequest);
        try {
            return schema.validate(nodeRequest.getNode());
        } catch (ProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private OcdsValidatorNodeRequest convertApiRequestToNodeRequest(OcdsValidatorApiRequest request) {
        JsonNode node = null;
        if (!StringUtils.isEmpty(request.getJson())) {
            node = getJsonNodeFromString(request.getJson());
        }

        if (!StringUtils.isEmpty(request.getUrl())) {
            node = getJsonNodeFromUrl(request.getUrl());
        }

        return new OcdsValidatorNodeRequest(request, node);
    }


}
