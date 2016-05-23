package org.devgateway.ocds.persistence.mongo;

/**
 * @author mihai Identifier OCDS entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         identifier
 */
public class Identifier {
    private String scheme;

    private String id;

    private String legalName;

    private String uri;

    @Override
    public String toString() {
        return id;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
