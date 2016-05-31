package org.devgateway.ocds.persistence.mongo.spring;

import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

/**
 * @author idobre
 * @since 5/31/16
 */
@Transactional
public class ReleaseJsonImport implements JsonImport<Release> {
    private static Logger logger = Logger.getLogger(ReleaseJsonImport.class);

    private final ReleaseRepository releaseRepository;

    private final JsonToObject releaseJsonToObject;

    public ReleaseJsonImport(final ReleaseRepository releaseRepository, final String jsonRelease) {
        this.releaseRepository = releaseRepository;
        this.releaseJsonToObject = new ReleaseJsonToObject(jsonRelease);
    }

    public ReleaseJsonImport(final ReleaseRepository releaseRepository, final File file) throws IOException {
        this.releaseRepository = releaseRepository;
        this.releaseJsonToObject = new ReleaseJsonToObject(file);
    }

    @Override
    public Release importObject() throws IOException {
        Release release = (Release) releaseJsonToObject.toObject();

        if (release.getId() == null) {
            release = releaseRepository.insert(release);
        } else {
            release = releaseRepository.save(release);
        }

        return release;
    }

    @Override
    public void logMessage(String message) {

    }
}
