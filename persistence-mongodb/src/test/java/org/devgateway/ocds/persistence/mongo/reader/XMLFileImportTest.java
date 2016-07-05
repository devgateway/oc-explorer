package org.devgateway.ocds.persistence.mongo.reader;

import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author idobre
 * @since 6/27/16
 */
public class XMLFileImportTest extends AbstractMongoTest {
    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    @Qualifier("XMLFileImportDefault")
    private XMLFile xmlFile;

    @Test
    public void process() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("xml/release.xml").getFile());
        xmlFile.process(file);

        List<Release> releases = releaseRepository.findAll();
        Assert.assertNotNull(releases);

        Release release = releaseRepository.findById("xmlimport-123");
        Assert.assertNotNull(release);
        Assert.assertEquals("check field", release.getLanguage(), "en");
    }
}

@Service
@Transactional
class XMLFileImportDefault extends XMLFileImport {
    @Override
    protected Release processRelease(Release release) {
        return release;
    }

    @Override
    protected AbstractRulesModule getAbstractRulesModule() {
        return new TestRules();
    }

    @Override
    public StringBuffer getMsgBuffer() {
        return null;
    }

    @Override
    public void logMessage(String message) {

    }

    class TestRules extends AbstractRulesModule {
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
}
