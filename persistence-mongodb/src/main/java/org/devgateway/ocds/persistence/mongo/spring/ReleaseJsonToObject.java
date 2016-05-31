package org.devgateway.ocds.persistence.mongo.spring;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.devgateway.ocds.persistence.mongo.Release;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author idobre
 * @since 5/31/16
 */
public class ReleaseJsonToObject implements JsonToObject<Release> {
    private final ObjectMapper mapper;

    private final String jsonRelease;

    private Release release;

    public ReleaseJsonToObject(final String jsonRelease) {
        this.jsonRelease = jsonRelease;
        this.mapper = new ObjectMapper();

        // this are non-standard features that are disabled by default.
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // Note that enabling this feature will incur performance overhead
        // due to having to store and check additional information: this typically
        // adds 20-30% to execution time for basic parsing.
        mapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
    }

    public ReleaseJsonToObject(final InputStream inputStream) throws IOException {
        this(IOUtils.toString(inputStream, "UTF-8"));
    }

    public ReleaseJsonToObject(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    @Override
    public Release toObject() throws IOException {
        if (release == null) {
            // Transform JSON String to a Release Object
            release = mapper.readValue(jsonRelease, Release.class);
        }

        return release;
    }
}
