package org.devgateway.ocds.web.rest.controller;

import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.json.JsonImportPackage;
import org.devgateway.ocds.persistence.mongo.spring.json.ReleasePackageJsonImport;
import org.devgateway.toolkit.web.AbstractWebTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author idobre
 * @since 9/9/16
 */
public class EndpointControllerTest extends AbstractWebTest {
    private static Logger logger = Logger.getLogger(EndpointControllerTest.class);

    @Autowired
    private ReleaseRepository releaseRepository;

    @Before
    public final void setUp() {
        releaseRepository.deleteAll();
    }

    @After
    public final void tearDown() {
        releaseRepository.deleteAll();
    }

    @Test
    public void test() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();

        final File file = new File(classLoader.getResource("json/endpoint-data-test.json").getFile());
        final JsonImportPackage releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);

        final Collection<Release> releases = releasePackageJsonImport.importObjects();
        final List<Release> importedRelease = releaseRepository.findAll();

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Assert.assertEquals(1, importedRelease.size());
    }
}
