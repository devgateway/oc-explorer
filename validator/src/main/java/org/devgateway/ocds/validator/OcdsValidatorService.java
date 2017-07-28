package org.devgateway.ocds.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListReportProvider;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by mpostelnicu on 7/5/17.
 */
@Service
public class OcdsValidatorService {

    private final Logger logger = LoggerFactory.getLogger(OcdsValidatorService.class);

    private Map<String, JsonSchema> keySchema = new ConcurrentHashMap<>();

    private Map<String, String> schemaNamePrefix = new ConcurrentHashMap<>();

    private Map<String, JsonNode> extensionMeta = new ConcurrentHashMap<>();

    private Map<String, JsonMergePatch> extensionReleaseJson = new ConcurrentHashMap<>();


    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private JsonNode getUnmodifiedSchemaNode(OcdsValidatorRequest request) {
        try {
            logger.debug("Loading unmodified schema node of type " + request.getSchemaType() + " version "
                    + request.getVersion());
            return JsonLoader.fromResource(schemaNamePrefix.get(request.getSchemaType()) + request.getVersion()
                    + OcdsValidatorConstants.SCHEMA_POSTFIX);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JsonNode applyExtensions(JsonNode schemaNode, OcdsValidatorRequest request) {
        if (ObjectUtils.isEmpty(request.getExtensions())) {
            logger.debug("No schema extensions are requested.");
            return schemaNode;
        }

        List<String> unrecognizedExtensions =
                request.getExtensions().stream().filter(e -> !OcdsValidatorConstants.EXTENSIONS.contains(e))
                        .collect(Collectors.toList());

        if (!unrecognizedExtensions.isEmpty()) {
            throw new RuntimeException("Unknown extensions by name: " + unrecognizedExtensions);
        }

        //TODO: check extension meta and see if they apply for the current standard

        JsonNode schemaResult = null;

        for (String ext : request.getExtensions()) {
            try {
                logger.debug("Applying schema extension " + ext);
                schemaResult = extensionReleaseJson.get(ext).apply(schemaNode);
            } catch (JsonPatchException e) {
                throw new RuntimeException(e);
            }
        }

        return schemaResult;
    }

    private JsonNode readExtensionMeta(String extensionName) {
        //reading meta
        try {
            logger.debug("Reading extension metadata for extension " + extensionName);
            return JsonLoader.fromResource(OcdsValidatorConstants.EXTENSIONS_PREFIX + extensionName + File
                    .separator + OcdsValidatorConstants
                    .EXTENSION_META);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonMergePatch readExtensionReleaseJson(String extensionName) {
        //reading meta
        try {
            logger.debug("Reading extension JSON contents for extension " + extensionName);
            JsonNode jsonMergePatch = JsonLoader.fromResource(OcdsValidatorConstants.EXTENSIONS_PREFIX
                    + extensionName + File.separator + OcdsValidatorConstants
                    .EXTENSION_RELEASE_JSON);
            JsonMergePatch patch = JsonMergePatch.fromJson(jsonMergePatch);
            return patch;
        } catch (IOException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonSchema getSchema(OcdsValidatorRequest request) {
        if (keySchema.containsKey(request.getKey())) {
            logger.debug("Returning cached schema with extensions " + request.getKey());
            return keySchema.get(request.getKey());
        } else {
            JsonNode schemaNode = getUnmodifiedSchemaNode(request);
            schemaNode = applyExtensions(schemaNode, request);
            try {
                JsonSchema schema = JsonSchemaFactory.newBuilder()
                        .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL)).freeze()
                        .getJsonSchema(schemaNode);
                logger.debug("Saving to cache schema with extensions " + request.getKey());
                keySchema.put(request.getKey(), schema);
                return schema;
            } catch (ProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    private void initSchemaNamePrefix() {
        logger.debug("Initializing prefixes for all available schemas");
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RELEASE, OcdsValidatorConstants.SchemaPrefixes.RELEASE);
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RECORD_PACKAGE,
                OcdsValidatorConstants.SchemaPrefixes.RECORD_PACKAGE);
        schemaNamePrefix.put(OcdsValidatorConstants.Schemas.RELEASE_PACKAGE,
                OcdsValidatorConstants.SchemaPrefixes.RELEASE_PACKAGE);
    }

    private void initExtensions() {
        logger.debug("Initializing schema extensions");
        OcdsValidatorConstants.EXTENSIONS.forEach(e -> {
            logger.debug("Initializing schema extension " + e);
            extensionMeta.put(e, readExtensionMeta(e));
            extensionReleaseJson.put(e, readExtensionReleaseJson(e));
        });
    }

    @PostConstruct
    private void init() {
        initSchemaNamePrefix();
        initExtensions();
    }


    public ProcessingReport validate(OcdsValidatorApiRequest request) {
        logger.debug("Running validation for api request for schema of type " + request.getSchemaType()
                +
" and version " + request.getVersion());
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
