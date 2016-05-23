/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 * Classification OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#classification
 */
@Document
public class Classification {

    private String scheme;

    private String id;

    private String description;

    private String uri;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
