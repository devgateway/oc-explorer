package org.devgateway.ocds.persistence.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Gazetteer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scheme;

    private List<String> identifiers = new ArrayList<>();

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}