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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    private Map<String, Integer> majorLatestFullVersion = new ConcurrentHashMap<>();


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
            logger.debug("No explicit schema extensions were requested.");
            return schemaNode;
        }

//        List<String> unrecognizedExtensions =
//                request.getExtensions().stream().filter(e -> !extensionMeta.containsKey(e))
//                        .collect(Collectors.toList());
//
//        if (!unrecognizedExtensions.isEmpty()) {
//            throw new RuntimeException("Unknown extensions by name: " + unrecognizedExtensions);
//        }

        JsonNode schemaResult = schemaNode;

        for (String ext : request.getExtensions()) {
            try {
                logger.debug("Applying schema extension " + ext);
                getExtensionMeta(ext); //TODO: check extension meta and see if they apply for the current standard
                schemaResult = getExtensionReleaseJson(ext).apply(schemaResult);
            } catch (JsonPatchException e) {
                throw new RuntimeException(e);
            }
        }

        return schemaResult;
    }

    private JsonNode getExtensionMeta(String id) {

        //check if preloaded as extension
        if (extensionMeta.containsKey(id)) {
            return extensionMeta.get(id);
        }

        //attempt load via URL
        try {
            JsonNode jsonNode = readExtensionMeta(new URL(id));
            extensionMeta.put(id, jsonNode);
            return jsonNode;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonMergePatch getExtensionReleaseJson(String id) {
        //check if preloaded as extension
        if (extensionReleaseJson.containsKey(id)) {
            return extensionReleaseJson.get(id);
        }

        //attempt load via URL
        try {
            String releaseUrl = id.replace(OcdsValidatorConstants.REMOTE_EXTENSION_META_POSTFIX,
                    OcdsValidatorConstants.EXTENSION_RELEASE_JSON);
            JsonMergePatch patch = readExtensionReleaseJson(new URL(releaseUrl));
            extensionReleaseJson.put(id, patch);
            return patch;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
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

    private JsonNode readExtensionMeta(URL url) {
        try {
            logger.debug("Reading extension metadata from URL " + url);
            return JsonLoader.fromURL(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonMergePatch readExtensionReleaseJson(URL url) {
        //reading meta
        try {
            logger.debug("Reading extension JSON contents for extension " + url);
            JsonNode jsonMergePatch = JsonLoader.fromURL(url);
            JsonMergePatch patch = JsonMergePatch.fromJson(jsonMergePatch);
            return patch;
        } catch (IOException | JsonPatchException e) {
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
        logger.debug("Initializing predefined schema extensions");
        OcdsValidatorConstants.EXTENSIONS.forEach(e -> {
            logger.debug("Initializing schema extension " + e);
            extensionMeta.put(e, readExtensionMeta(e));
            extensionReleaseJson.put(e, readExtensionReleaseJson(e));
        });
    }

    private void initMajorLatestFullVersion() {

        for (int i = 0; i < OcdsValidatorConstants.Versions.ALL.length; i++) {
            String[] versions = OcdsValidatorConstants.Versions.ALL[i].split("\\.");
            String majorMinor = versions[0] + "." + versions[1];

            if (majorLatestFullVersion.containsKey(majorMinor)) {
                if (majorLatestFullVersion.get(majorMinor) < Integer.parseInt(versions[2])) {
                    majorLatestFullVersion.put(majorMinor, Integer.parseInt(versions[2]));
                }
            } else {
                majorLatestFullVersion.put(majorMinor, Integer.parseInt(versions[2]));
            }

        }


    }

    @PostConstruct
    private void init() {
        initSchemaNamePrefix();
        initMajorLatestFullVersion();
        initExtensions();
    }


    public ProcessingReport validate(OcdsValidatorApiRequest request) {
        logger.debug("Running validation for api request for schema of type " + request.getSchemaType()
                + " and version " + request.getVersion());
        OcdsValidatorNodeRequest nodeRequest = convertApiRequestToNodeRequest(request);

        if (nodeRequest.getSchemaType().equals(OcdsValidatorConstants.Schemas.RELEASE)) {

            if (request.getVersion() == null) {
                throw new RuntimeException("Not allowed null version info for release validation!");
            }

            return validateRelease(nodeRequest);
        }

        if (nodeRequest.getSchemaType().equals(OcdsValidatorConstants.Schemas.RELEASE_PACKAGE)) {
            return validateReleasePackage(nodeRequest);
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

    private String constructFullVersion(String majorMinor, Integer bugfix) {
        return majorMinor + "." + bugfix;
    }

    /**
     * Validates a release package
     *
     * @param nodeRequest
     * @return
     */
    private ProcessingReport validateReleasePackage(OcdsValidatorNodeRequest nodeRequest) {

        if (nodeRequest.getVersion() == null) {
            //try autodetect using version in node
            if (nodeRequest.getNode().hasNonNull(OcdsValidatorConstants.VERSION_PROPERTY)) {
                String majorMinor = nodeRequest.getNode().get(OcdsValidatorConstants.VERSION_PROPERTY).asText();
                if (!majorLatestFullVersion.containsKey(majorMinor)) {
                    throw new RuntimeException("Unrecognized package release version " + majorMinor);
                }
                nodeRequest.setVersion(constructFullVersion(majorMinor, majorLatestFullVersion.get(majorMinor)));
            } else {
                throw new RuntimeException("Version schema property has to be present!");
            }
        }

        JsonSchema schema = getSchema(nodeRequest);
        try {
            ProcessingReport releasePackageReport = schema.validate(nodeRequest.getNode());
            if (!releasePackageReport.isSuccess()) {
                return releasePackageReport;
            }

            //get release package extensions
            if (nodeRequest.getNode().hasNonNull(OcdsValidatorConstants.EXTENSIONS_PROPERTY)) {
                for (JsonNode extension : nodeRequest.getNode().get(OcdsValidatorConstants.EXTENSIONS_PROPERTY)) {
                    nodeRequest.getExtensions().add(extension.asText());
                }
            }

            if (nodeRequest.getNode().hasNonNull(OcdsValidatorConstants.RELEASES_PROPERTY)) {
                for (JsonNode release : nodeRequest.getNode().get(OcdsValidatorConstants.RELEASES_PROPERTY)) {
                    OcdsValidatorNodeRequest releaseValidationRequest
                            = new OcdsValidatorNodeRequest(nodeRequest, release);
                    releaseValidationRequest.setSchemaType(OcdsValidatorConstants.Schemas.RELEASE);
                    releasePackageReport.mergeWith(validateRelease(releaseValidationRequest));
                }
            } else {
                throw new RuntimeException("No releases were found during release package validation!");
            }

            return releasePackageReport;

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
