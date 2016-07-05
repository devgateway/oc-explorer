package org.devgateway.ocds.persistence.mongo.reader;

import org.devgateway.ocds.persistence.mongo.spring.ImportService;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author idobre
 * @since 6/27/16
 */
public interface XMLFile extends ImportService {
    /**
     * Process an XML stream and map it to Release objects.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void process(final InputStream inputStream) throws IOException, SAXException;

    public void process(final File file) throws IOException, SAXException;

    /**
     * Save a particular release into database.
     *
     * @param obj
     */
    public void saveRelease(Object obj);

    /**
     * Returns a StringBuffer with import statistics
     *
     * @return StringBuffer
     */
    StringBuffer getMsgBuffer();
}
