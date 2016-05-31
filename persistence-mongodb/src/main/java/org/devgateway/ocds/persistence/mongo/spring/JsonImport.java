package org.devgateway.ocds.persistence.mongo.spring;

import org.devgateway.ocds.persistence.mongo.Identifiable;

import java.io.IOException;

/**
 * @author idobre
 * @since 5/31/16
 */
public interface JsonImport<T extends Identifiable> extends ImportService {
    /**
     * Imports a Document from a JSON and returns the imported object
     *
     * @return imported object after was saved/updated into database (it should contain an id/_id)
     */
    T importObject() throws IOException;
}
