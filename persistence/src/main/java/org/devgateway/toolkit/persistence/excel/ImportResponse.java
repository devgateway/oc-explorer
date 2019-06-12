package org.devgateway.toolkit.persistence.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 26/03/2018
 *
 * Class used to wrap together an Excel Import {@link List} of {@link Object} and a {@link Map} with validation errors.
 */
public class ImportResponse implements Serializable {
    private final List<Object> objects;

    private final Map<Integer, List<String>> errors;

    public ImportResponse() {
        this.objects = new ArrayList<>();
        this.errors = new HashMap<>();
    }

    public List<Object> getObjects() {
        return objects;
    }

    public Map<Integer, List<String>> getErrors() {
        return errors;
    }
}
