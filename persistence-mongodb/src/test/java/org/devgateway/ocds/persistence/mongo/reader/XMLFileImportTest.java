package org.devgateway.ocds.persistence.mongo.reader;

import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author idobre
 * @since 6/27/16
 */
public class XMLFileImportTest extends AbstractMongoTest {
    @Autowired
    private ReleaseRepository releaseRepository;

    public class XMLFileImportDefault extends XMLFileImport {
        public XMLFileImportDefault(ReleaseRepository releaseRepository, File file) throws IOException {
            super(releaseRepository, file);
        }

        @Override
        protected Release processRelease(Release release) {
            return release;
        }

        @Override
        protected AbstractRulesModule getAbstractRulesModule() {
            return new TestRules();
        }
    }

    public class TestRules extends AbstractRulesModule {

        @Override
        protected void configure() {
            forPattern("test/release")
                    .createObject().ofType("org.devgateway.ocds.persistence.mongo.Release")
                    .then().setNext("saveRelease");

            forPattern("test/release/id").setBeanProperty().withName("id");

            forPattern("test/release/buyer").createObject().ofType("org.devgateway.ocds.persistence.mongo.Organization")
                    .then().setNext("setBuyer");

            forPattern("test/release/buyer/name").setBeanProperty().withName("name");

            forPattern("test/release/language").setBeanProperty().withName("language");
        }
    }

    @Test
    public void process() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("xml/release.xml").getFile());
        XMLFile xmlFile = new XMLFileImportDefault(releaseRepository, file);
        xmlFile.process();

        List<Release> releases = releaseRepository.findAll();
        Assert.assertEquals("number of releases", 2, releases.size());

        Release release = releaseRepository.findById("123");
        Assert.assertNotNull(release);
        Assert.assertEquals("check field", release.getLanguage(), "en");
    }
}
