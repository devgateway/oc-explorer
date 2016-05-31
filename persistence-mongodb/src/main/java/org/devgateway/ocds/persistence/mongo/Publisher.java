package org.devgateway.ocds.persistence.mongo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Information to uniquely identify the publisher of this package
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "scheme",
        "uid",
        "uri"
})
public class Publisher {
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("name")
    private String name;
    /**
     * The scheme that holds the unique identifiers used to identify the item being identified.
     *
     */
    @JsonProperty("scheme")
    private String scheme;
    /**
     * The unique ID for this entity under the given ID scheme.
     *
     */
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("uri")
    private String uri;

    /**
     *
     * (Required)
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * (Required)
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The scheme that holds the unique identifiers used to identify the item being identified.
     *
     * @return
     *     The scheme
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * The scheme that holds the unique identifiers used to identify the item being identified.
     *
     * @param scheme
     *     The scheme
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * The unique ID for this entity under the given ID scheme.
     *
     * @return
     *     The uid
     */
    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    /**
     * The unique ID for this entity under the given ID scheme.
     *
     * @param uid
     *     The uid
     */
    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @return
     *     The uri
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    /**
     *
     * @param uri
     *     The uri
     */
    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(name).
                append(scheme).
                append(uid).
                append(uri).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Publisher)) {
            return false;
        }
        Publisher rhs = ((Publisher) other);
        return new EqualsBuilder().
                append(name, rhs.name).
                append(scheme, rhs.scheme).
                append(uid, rhs.uid).
                append(uri, rhs.uri).isEquals();
    }

}
