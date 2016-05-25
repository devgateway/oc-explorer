
package org.devgateway.ocds.generated.persistence.mongo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "scheme",
    "id",
    "legalName",
    "uri"
})
public class Identifier {

    /**
     * Organization identifiers be drawn from an existing identification scheme. This field is used to indicate the scheme or codelist in which the identifier will be found. This value should be drawn from the [Organization Identifier Scheme](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#organization-identifier-scheme).
     * 
     */
    @JsonProperty("scheme")
    private String scheme;
    /**
     * The identifier of the organization in the selected scheme.
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * The legally registered name of the organization.
     * 
     */
    @JsonProperty("legalName")
    private String legalName;
    /**
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the website of the organization: that can be done through the url field of the Organization contact point.
     * 
     */
    @JsonProperty("uri")
    private URI uri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Organization identifiers be drawn from an existing identification scheme. This field is used to indicate the scheme or codelist in which the identifier will be found. This value should be drawn from the [Organization Identifier Scheme](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#organization-identifier-scheme).
     * 
     * @return
     *     The scheme
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * Organization identifiers be drawn from an existing identification scheme. This field is used to indicate the scheme or codelist in which the identifier will be found. This value should be drawn from the [Organization Identifier Scheme](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#organization-identifier-scheme).
     * 
     * @param scheme
     *     The scheme
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * The identifier of the organization in the selected scheme.
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * The identifier of the organization in the selected scheme.
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The legally registered name of the organization.
     * 
     * @return
     *     The legalName
     */
    @JsonProperty("legalName")
    public String getLegalName() {
        return legalName;
    }

    /**
     * The legally registered name of the organization.
     * 
     * @param legalName
     *     The legalName
     */
    @JsonProperty("legalName")
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the website of the organization: that can be done through the url field of the Organization contact point.
     * 
     * @return
     *     The uri
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the website of the organization: that can be done through the url field of the Organization contact point.
     * 
     * @param uri
     *     The uri
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scheme).append(id).append(legalName).append(uri).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Identifier) == false) {
            return false;
        }
        Identifier rhs = ((Identifier) other);
        return new EqualsBuilder().append(scheme, rhs.scheme).append(id, rhs.id).append(legalName, rhs.legalName).append(uri, rhs.uri).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
