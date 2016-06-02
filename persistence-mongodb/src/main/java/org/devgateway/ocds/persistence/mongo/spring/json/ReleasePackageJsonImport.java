package org.devgateway.ocds.persistence.mongo.spring.json;

import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.ReleasePackage;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.json2object.JsonToObject;
import org.devgateway.ocds.persistence.mongo.spring.json2object.ReleasePackageJsonToObject;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author idobre
 * @since 6/1/16
 */
public class ReleasePackageJsonImport implements JsonImportPackage<ReleasePackage, Release> {
    private static Logger logger = Logger.getLogger(ReleaseJsonImport.class);

    private final ReleaseRepository releaseRepository;

    private final JsonToObject releasePackageJsonToObject;

    public ReleasePackageJsonImport(final ReleaseRepository releaseRepository,
                                    final String releasePackageJsonToObject) {
        this.releaseRepository = releaseRepository;
        this.releasePackageJsonToObject = new ReleasePackageJsonToObject(releasePackageJsonToObject);
    }

    public ReleasePackageJsonImport(final ReleaseRepository releaseRepository, final File file) throws IOException {
        this.releaseRepository = releaseRepository;
        this.releasePackageJsonToObject = new ReleasePackageJsonToObject(file);
    }

    @Override
    public Collection<Release> importObjects() throws IOException {
        ReleasePackage releasePackage = (ReleasePackage) releasePackageJsonToObject.toObject();
        Collection<Release> savedReleases = new LinkedHashSet<>();

        if (!releasePackage.getReleases().isEmpty()) {
            Set<Release> releases = releasePackage.getReleases();
            for (Release release : releases) {
                Release savedRelease = releaseRepository.save(release);
                savedReleases.add(savedRelease);
            }
        }

        return savedReleases;
    }

    @Override
    public void logMessage(String message) {

    }
}
