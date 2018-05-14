package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URI;


/**
 * Unit
 * <p>
 * A description of the unit in which the supplies, services or works are provided (e.g. hours, kilograms) and the
 * unit-price. For comparability, an established list of units can be used.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "name",
        "value",
        "uri"
})
public class Unit {

    /**
     * Scheme
     * <p>
     * The list from which units of measure identifiers are taken. This should be an entry from the options available
     * in the [unitClassificationScheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#unit-classification-scheme) codelist. Use of the scheme 'UNCEFACT' for the
     * UN/CEFACT Recommendation 20 list of 'Codes for Units of Measure Used in International Trade' is recommended,
     * although other options are available.
     */
    @JsonProperty("scheme")
    @JsonPropertyDescription("The list from which units of measure identifiers are taken. This should be an entry "
            + "from the options available in the [unitClassificationScheme](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#unit-classification-scheme) codelist. Use of the scheme 'UNCEFACT' "
            + "for the UN/CEFACT Recommendation 20 list of 'Codes for Units of Measure Used in International Trade' "
            + "is recommended, although other options are available.")
    private String scheme;
    /**
     * ID
     * <p>
     * The identifier from the codelist referenced in the scheme property. Check the codelist for details of how to
     * find and use identifiers from the scheme in use.
     */
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier from the codelist referenced in the scheme property. Check the codelist "
            + "for details of how to find and use identifiers from the scheme in use.")
    private String id;
    /**
     * Name
     * <p>
     * Name of the unit.
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Name of the unit.")
    private String name;
    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    private Amount value;
    /**
     * URI
     * <p>
     * If the scheme used provide a machine-readable URI for this unit of measure, this can be given.
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("If the scheme used provide a machine-readable URI for this unit of measure, this can be"
            + " given.")
    private URI uri;

    /**
     * Scheme
     * <p>
     * The list from which units of measure identifiers are taken. This should be an entry from the options available
     * in the [unitClassificationScheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#unit-classification-scheme) codelist. Use of the scheme 'UNCEFACT' for the
     * UN/CEFACT Recommendation 20 list of 'Codes for Units of Measure Used in International Trade' is recommended,
     * although other options are available.
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * Scheme
     * <p>
     * The list from which units of measure identifiers are taken. This should be an entry from the options available
     * in the [unitClassificationScheme](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#unit-classification-scheme) codelist. Use of the scheme 'UNCEFACT' for the
     * UN/CEFACT Recommendation 20 list of 'Codes for Units of Measure Used in International Trade' is recommended,
     * although other options are available.
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * ID
     * <p>
     * The identifier from the codelist referenced in the scheme property. Check the codelist for details of how to
     * find and use identifiers from the scheme in use.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * The identifier from the codelist referenced in the scheme property. Check the codelist for details of how to
     * find and use identifiers from the scheme in use.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Name
     * <p>
     * Name of the unit.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Name
     * <p>
     * Name of the unit.
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    public Amount getValue() {
        return value;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    public void setValue(Amount value) {
        this.value = value;
    }

    /**
     * URI
     * <p>
     * If the scheme used provide a machine-readable URI for this unit of measure, this can be given.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * URI
     * <p>
     * If the scheme used provide a machine-readable URI for this unit of measure, this can be given.
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scheme", scheme)
                .append("id", id)
                .append("name", name)
                .append("value", value)
                .append("uri", uri)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scheme)
                .append(name)
                .append(id)
                .append(value)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Unit)) {
            return false;
        }
        Unit rhs = ((Unit) other);
        return new EqualsBuilder().append(scheme, rhs.scheme)
                .append(name, rhs.name)
                .append(id, rhs.id)
                .append(value, rhs.value)
                .append(uri, rhs.uri)
                .isEquals();
    }

}
