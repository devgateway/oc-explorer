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
 * Contact point
 * <p>
 * An person, contact point or department to contact in relation to this contracting process.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "email",
        "telephone",
        "faxNumber",
        "url"
})
public class ContactPoint {

    /**
     * Name
     * <p>
     * The name of the contact person, department, or contact point, for correspondence relating to this contracting
     * process.
     */
    @JsonProperty("name")
    @ExcelExport
    @JsonPropertyDescription("The name of the contact person, department, or contact point, for correspondence "
            + "relating to this contracting process.")
    private String name;
    /**
     * Email
     * <p>
     * The e-mail address of the contact point/person.
     */
    @JsonProperty("email")
    @ExcelExport
    @JsonPropertyDescription("The e-mail address of the contact point/person.")
    private String email;
    /**
     * Telephone
     * <p>
     * The telephone number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("telephone")
    @ExcelExport
    @JsonPropertyDescription("The telephone number of the contact point/person. This should include the international"
            + " dialing code.")
    private String telephone;
    /**
     * Fax number
     * <p>
     * The fax number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("faxNumber")
    @ExcelExport
    @JsonPropertyDescription("The fax number of the contact point/person. This should include the international "
            + "dialing code.")
    private String faxNumber;
    /**
     * URL
     * <p>
     * A web address for the contact point/person.
     */
    @JsonProperty("url")
    @ExcelExport
    @JsonPropertyDescription("A web address for the contact point/person.")
    private URI url;

    /**
     * Name
     * <p>
     * The name of the contact person, department, or contact point, for correspondence relating to this contracting
     * process.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Name
     * <p>
     * The name of the contact person, department, or contact point, for correspondence relating to this contracting
     * process.
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Email
     * <p>
     * The e-mail address of the contact point/person.
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * Email
     * <p>
     * The e-mail address of the contact point/person.
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Telephone
     * <p>
     * The telephone number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("telephone")
    public String getTelephone() {
        return telephone;
    }

    /**
     * Telephone
     * <p>
     * The telephone number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Fax number
     * <p>
     * The fax number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("faxNumber")
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Fax number
     * <p>
     * The fax number of the contact point/person. This should include the international dialing code.
     */
    @JsonProperty("faxNumber")
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * URL
     * <p>
     * A web address for the contact point/person.
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * URL
     * <p>
     * A web address for the contact point/person.
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("email", email)
                .append("telephone", telephone)
                .append("faxNumber", faxNumber)
                .append("url", url)

                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(faxNumber)
                .append(telephone)
                .append(email)
                .append(url)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ContactPoint)) {
            return false;
        }
        ContactPoint rhs = ((ContactPoint) other);
        return new EqualsBuilder().append(name, rhs.name)
                .append(faxNumber, rhs.faxNumber)
                .append(telephone, rhs.telephone)
                .append(email, rhs.email)
                .append(url, rhs.url)
                .isEquals();
    }

}
