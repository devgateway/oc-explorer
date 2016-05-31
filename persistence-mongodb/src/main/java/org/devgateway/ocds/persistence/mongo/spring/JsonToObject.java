package org.devgateway.ocds.persistence.mongo.spring;

import java.io.IOException;

/**
 * @author idobre
 * @since 5/31/16
 */
public interface JsonToObject<T> {

    /**
     * Just transform a JSON String to a T object
     *
     * @return a T object
     */
    T toObject() throws IOException;
}
