/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

/**
 * @author mihai
 * Address OCDS entity http://standard.open-contracting.org/latest/en/schema/reference/#address
 */
public class Address {
    private String streetAddress;

    private String locality;

    private String region;

    private String postalCode;

    private String countryName;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}
