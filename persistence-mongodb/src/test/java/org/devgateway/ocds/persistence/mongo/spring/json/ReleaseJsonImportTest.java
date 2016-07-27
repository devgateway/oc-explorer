package org.devgateway.ocds.persistence.mongo.spring.json;

import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author idobre
 * @since 5/31/16
 */
public class ReleaseJsonImportTest extends AbstractMongoTest {
    @Autowired
    private ReleaseRepository releaseRepository;

    @Test
    public void importObject() throws Exception {
        String jsonRelease = "{\n" +
                "    tag: [\"tender\"],\n" +
                "    planning: {\n" +
                "        budget: {\n" +
                "            description: \"budget desc...\",\n" +
                "            amount: {\n" +
                "                amount: 10000,\n" +
                "                currency: \"USD\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JsonImport releaseJsonImport = new ReleaseJsonImport(releaseRepository, jsonRelease);
        Release release = (Release) releaseJsonImport.importObject();
        Release releaseById = releaseRepository.findById(release.getId());

        Assert.assertNotNull("Check if we have something in the database", releaseById);
        Assert.assertEquals("Check if the releases are the same", release, releaseById);
    }
}
