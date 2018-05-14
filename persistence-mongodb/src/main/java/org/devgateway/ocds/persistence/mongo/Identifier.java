package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.net.URI;


/**
 * Identifier
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "legalName",
        "uri"
})
public class Identifier {

    /**
     * Scheme
     * <p>
     * Organization identifiers should be drawn from an existing organization identifier list. The scheme field is
     * used to indicate the list or register from which the identifier is drawn. This value should be drawn from the
     * [Organization Identifier Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.
     */
    @JsonProperty("scheme")
    @ExcelExport
    @JsonPropertyDescription("Organization identifiers should be drawn from an existing organization identifier list."
            + " The scheme field is used to indicate the list or register from which the identifier is drawn. This "
            + "value should be drawn from the [Organization Identifier Scheme](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.")
    private String scheme;
    /**
     * ID
     * <p>
     * The identifier of the organization in the selected scheme.
     */
    @JsonProperty("id")
    @ExcelExport
    @JsonPropertyDescription("The identifier of the organization in the selected scheme.")
    private String id;
    /**
     * Legal Name
     * <p>
     * The legally registered name of the organization.
     */
    @JsonProperty("legalName")
    @ExcelExport
    @JsonPropertyDescription("The legally registered name of the organization.")
    private String legalName;
    /**
     * URI
     * <p>
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com)
     * or some other relevant URI provider. This is not for listing the website of the organization: that can be done
     * through the URL field of the Organization contact point.
     */
    @JsonProperty("uri")
    @ExcelExport
    @JsonPropertyDescription("A URI to identify the organization, such as those provided by [Open Corporates]"
            + "(http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the "
            + "website of the organization: that can be done through the URL field of the Organization contact point.")
    private URI uri;

    /**
     * Scheme
     * <p>
     * Organization identifiers should be drawn from an existing organization identifier list. The scheme field is
     * used to indicate the list or register from which the identifier is drawn. This value should be drawn from the
     * [Organization Identifier Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * Scheme
     * <p>
     * Organization identifiers should be drawn from an existing organization identifier list. The scheme field is
     * used to indicate the list or register from which the identifier is drawn. This value should be drawn from the
     * [Organization Identifier Scheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * ID
     * <p>
     * The identifier of the organization in the selected scheme.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * The identifier of the organization in the selected scheme.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Legal Name
     * <p>
     * The legally registered name of the organization.
     */
    @JsonProperty("legalName")
    public String getLegalName() {
        return legalName;
    }

    /**
     * Legal Name
     * <p>
     * The legally registered name of the organization.
     */
    @JsonProperty("legalName")
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * URI
     * <p>
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com)
     * or some other relevant URI provider. This is not for listing the website of the organization: that can be done
     * through the URL field of the Organization contact point.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * URI
     * <p>
     * A URI to identify the organization, such as those provided by [Open Corporates](http://www.opencorporates.com)
     * or some other relevant URI provider. This is not for listing the website of the organization: that can be done
     * through the URL field of the Organization contact point.
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scheme", scheme)
                .append("id", id)
                .append("legalName", legalName)
                .append("uri", uri)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(legalName)
                .append(id)
                .append(scheme)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Identifier)) {
            return false;
        }
        Identifier rhs = ((Identifier) other);
        return new EqualsBuilder().append(legalName, rhs.legalName)
                .append(id, rhs.id)
                .append(scheme, rhs.scheme)
                .append(uri, rhs.uri)
                .isEquals();
    }

}
