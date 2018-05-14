package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;


/**
 * Address
 * <p>
 * An address. This may be the legally registered address of the organization, or may be a correspondence address for
 * this particular contracting process.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "streetAddress",
        "locality",
        "region",
        "postalCode",
        "countryName"
})
public class Address {

    /**
     * Street address
     * <p>
     * The street address. For example, 1600 Amphitheatre Pkwy.
     */
    @JsonProperty("streetAddress")
    @ExcelExport
    @JsonPropertyDescription("The street address. For example, 1600 Amphitheatre Pkwy.")
    private String streetAddress;
    /**
     * Locality
     * <p>
     * The locality. For example, Mountain View.
     */
    @JsonProperty("locality")
    @ExcelExport
    @JsonPropertyDescription("The locality. For example, Mountain View.")
    private String locality;
    /**
     * Region
     * <p>
     * The region. For example, CA.
     */
    @JsonProperty("region")
    @ExcelExport
    @JsonPropertyDescription("The region. For example, CA.")
    private String region;
    /**
     * Postal code
     * <p>
     * The postal code. For example, 94043.
     */
    @JsonProperty("postalCode")
    @ExcelExport
    @JsonPropertyDescription("The postal code. For example, 94043.")
    private String postalCode;
    /**
     * Country name
     * <p>
     * The country name. For example, United States.
     */
    @JsonProperty("countryName")
    @ExcelExport
    @JsonPropertyDescription("The country name. For example, United States.")
    private String countryName;

    /**
     * Street address
     * <p>
     * The street address. For example, 1600 Amphitheatre Pkwy.
     */
    @JsonProperty("streetAddress")
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Street address
     * <p>
     * The street address. For example, 1600 Amphitheatre Pkwy.
     */
    @JsonProperty("streetAddress")
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Locality
     * <p>
     * The locality. For example, Mountain View.
     */
    @JsonProperty("locality")
    public String getLocality() {
        return locality;
    }

    /**
     * Locality
     * <p>
     * The locality. For example, Mountain View.
     */
    @JsonProperty("locality")
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * Region
     * <p>
     * The region. For example, CA.
     */
    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    /**
     * Region
     * <p>
     * The region. For example, CA.
     */
    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Postal code
     * <p>
     * The postal code. For example, 94043.
     */
    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Postal code
     * <p>
     * The postal code. For example, 94043.
     */
    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Country name
     * <p>
     * The country name. For example, United States.
     */
    @JsonProperty("countryName")
    public String getCountryName() {
        return countryName;
    }

    /**
     * Country name
     * <p>
     * The country name. For example, United States.
     */
    @JsonProperty("countryName")
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("streetAddress", streetAddress)
                .append("locality", locality)
                .append("region", region)
                .append("postalCode", postalCode)
                .append("countryName", countryName)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(streetAddress)
                .append(postalCode)
                .append(locality)
                .append(countryName)
                .append(region)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Address)) {
            return false;
        }
        Address rhs = ((Address) other);
        return new EqualsBuilder().append(streetAddress, rhs.streetAddress)
                .append(postalCode, rhs.postalCode)
                .append(locality, rhs.locality)
                .append(countryName, rhs.countryName)
                .append(region, rhs.region)
                .isEquals();
    }

}
