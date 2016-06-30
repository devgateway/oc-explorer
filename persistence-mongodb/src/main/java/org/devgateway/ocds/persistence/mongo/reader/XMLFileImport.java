package org.devgateway.ocds.persistence.mongo.reader;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author idobre
 * @since 6/27/16
 */
public abstract class XMLFileImport implements XMLFile {
    private final ReleaseRepository releaseRepository;

    private final InputStream inputStream;

    public XMLFileImport(final ReleaseRepository releaseRepository, final InputStream inputStream) {
        this.releaseRepository = releaseRepository;
        this.inputStream = inputStream;
    }

    public XMLFileImport(final ReleaseRepository releaseRepository, final File file) throws IOException {
        this(releaseRepository, new FileInputStream(file));
    }

    public void process() throws IOException, SAXException {
        DigesterLoader digesterLoader = DigesterLoader.newLoader(getAbstractRulesModule());
        Digester digester = digesterLoader.newDigester();

        // Push this object onto Digester's stack to handle object save operation (call saveRelease method)
        digester.push(this);
        digester.parse(inputStream);
    }

    /**
     * This function should be called on 'end' event when we have a complete Release object.
     *
     * @param obj
     */
    public void saveRelease(Object obj) {
        if (obj instanceof Release) {
            Release release = processRelease((Release) obj);

            if (release.getId() == null) {
                releaseRepository.insert(release);
            } else {
                releaseRepository.save(release);
            }
        }
    }

    /**
     * Function used to post-process a release in case we need to append new information, like ocid.
     *
     * @param release
     * @return
     */
    protected abstract Release processRelease(final Release release);

    protected abstract AbstractRulesModule getAbstractRulesModule();
}

