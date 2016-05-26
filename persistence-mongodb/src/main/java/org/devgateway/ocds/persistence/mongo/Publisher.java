package org.devgateway.ocds.persistence.mongo;

/**
 *
 * @author mihai
 * The publisher block is used in release and record packages to identify the source of a dataset.
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#publisher
 */
public class Publisher {
    /**
     * The name of the organisation or department responsible for publishing this data.
     */
    private String name;

    /**
     * The scheme that holds the unique identifiers used to identify the item being identified.
     */
    private String scheme;

    /**
     * The unique ID for this entity under the given ID scheme. Note the use of ‘uid’ rather than ‘id’.
     */
    private String uid;

    /**
     * A URI to identify the publisher.
     */
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
