package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * Classification
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "description",
        "uri"
})
public class Classification implements Identifiable {

    /**
     * Scheme
     * <p>
     * An classification should be drawn from an existing scheme or list of codes. This field is used to indicate the
     * scheme/codelist from which the classification is drawn. For line item classifications, this value should
     * represent an known [Item Classification Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#item-classification-scheme) wherever possible.
     */
    @JsonProperty("scheme")
    @JsonPropertyDescription("An classification should be drawn from an existing scheme or list of codes. This field "
            + "is used to indicate the scheme/codelist from which the classification is drawn. For line item "
            + "classifications, this value should represent an known [Item Classification Scheme](http://standard"
            + ".open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) wherever possible.")
    @ExcelExport
    private String scheme;
    /**
     * ID
     * <p>
     * The classification code drawn from the selected scheme.
     */
    @JsonProperty("id")
    @JsonPropertyDescription("The classification code drawn from the selected scheme.")
    @ExcelExport
    private String id;
    /**
     * Description
     * <p>
     * A textual description or title for the code.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A textual description or title for the code.")
    @ExcelExport
    private String description;
    /**
     * URI
     * <p>
     * A URI to identify the code. In the event individual URIs are not available for items in the identifier scheme
     * this value should be left blank.
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("A URI to identify the code. In the event individual URIs are not available for items in"
            + " the identifier scheme this value should be left blank.")
    private URI uri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Scheme
     * <p>
     * An classification should be drawn from an existing scheme or list of codes. This field is used to indicate the
     * scheme/codelist from which the classification is drawn. For line item classifications, this value should
     * represent an known [Item Classification Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#item-classification-scheme) wherever possible.
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * Scheme
     * <p>
     * An classification should be drawn from an existing scheme or list of codes. This field is used to indicate the
     * scheme/codelist from which the classification is drawn. For line item classifications, this value should
     * represent an known [Item Classification Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#item-classification-scheme) wherever possible.
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * ID
     * <p>
     * The classification code drawn from the selected scheme.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * The classification code drawn from the selected scheme.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Description
     * <p>
     * A textual description or title for the code.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * A textual description or title for the code.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * URI
     * <p>
     * A URI to identify the code. In the event individual URIs are not available for items in the identifier scheme
     * this value should be left blank.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * URI
     * <p>
     * A URI to identify the code. In the event individual URIs are not available for items in the identifier scheme
     * this value should be left blank.
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
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
    public String toString() {
        return new ToStringBuilder(this).append("scheme", scheme)
                .append("id", id)
                .append("description", description)
                .append("uri", uri)
                .append("additionalProperties", additionalProperties)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(description)
                .append(id)
                .append(additionalProperties)
                .append(scheme)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Classification)) {
            return false;
        }
        Classification rhs = ((Classification) other);
        return new EqualsBuilder().append(description, rhs.description)
                .append(id, rhs.id)
                .append(additionalProperties, rhs.additionalProperties)
                .append(scheme, rhs.scheme)
                .append(uri, rhs.uri)
                .isEquals();
    }

    @Override
    public Serializable getIdProperty() {
        return id;
    }
}
