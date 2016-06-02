package org.devgateway.ocds.persistence.mongo.spring.json2object;

import org.devgateway.ocds.persistence.mongo.Release;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that transform a JSON object representing a Release into a Release object
 *
 * @author idobre
 * @since 5/31/16
 */
public class ReleaseJsonToObject extends AbstractJsonToObject<Release> {
    private Release release;

    public ReleaseJsonToObject(String jsonObject) {
        super(jsonObject);
    }

    public ReleaseJsonToObject(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public ReleaseJsonToObject(File file) throws IOException {
        super(file);
    }

    @Override
    public Release toObject() throws IOException {
        if (release == null) {
            // Transform JSON String to a Release Object
            release = this.mapper.readValue(this.jsonObject, Release.class);
        }

        return release;
    }
}
