package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Organization
 * <p>
 * A party (organization)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "identifier",
        "additionalIdentifiers",
        "address",
        "contactPoint",
        "roles",
        "details"
})
public class Organization implements Identifiable {

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    @ExcelExport
    @JsonPropertyDescription("A common name for this organization or other participant in the contracting process. "
            + "The identifier object provides an space for the formal legal name, and so this may either repeat that "
            + "value, or could provide the common name by which this organization or entity is known. This field may "
            + "also include details of the department or sub-unit involved in this contracting process.")
    private String name;
    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    @ExcelExport
    @JsonPropertyDescription("The ID used for cross-referencing to this party from other sections of the release. "
            + "This field may be built with the following structure {identifier.scheme}-{identifier.id}"
            + "(-{department-identifier}).")
    private String id;
    /**
     * Identifier
     * <p>
     */
    @JsonProperty("identifier")
    @ExcelExport
    private Identifier identifier;
    /**
     * Additional identifiers
     * <p>
     * A list of additional / supplemental identifiers for the organization or participant, using the [organization
     * identifier guidance](http://standard.open-contracting.org/latest/en/schema/identifiers/). This could be used
     * to provide an internally used identifier for this organization in addition to the primary legal entity
     * identifier.
     */
    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list of additional / supplemental identifiers for the organization or participant, "
            + "using the [organization identifier guidance](http://standard.open-contracting"
            + ".org/latest/en/schema/identifiers/). This could be used to provide an internally used identifier for "
            + "this organization in addition to the primary legal entity identifier.")
    private Set<Identifier> additionalIdentifiers = new LinkedHashSet<Identifier>();
    /**
     * Address
     * <p>
     * An address. This may be the legally registered address of the organization, or may be a correspondence address
     * for this particular contracting process.
     */
    @JsonProperty("address")
    @JsonPropertyDescription("An address. This may be the legally registered address of the organization, or may be a"
            + " correspondence address for this particular contracting process.")
    @ExcelExport
    private Address address;
    /**
     * Contact point
     * <p>
     * An person, contact point or department to contact in relation to this contracting process.
     */
    @JsonProperty("contactPoint")
    @JsonPropertyDescription("An person, contact point or department to contact in relation to this contracting "
            + "process.")
    @ExcelExport
    private ContactPoint contactPoint;
    /**
     * Party roles
     * <p>
     * The party's role(s) in the contracting process. Role(s) should be taken from the [partyRole codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). Values from the provided
     * codelist should be used wherever possible, though extended values can be provided if the codelist does not
     * have a relevant code.
     */
    @JsonProperty("roles")
    @JsonPropertyDescription("The party's role(s) in the contracting process. Role(s) should be taken from the "
            + "[partyRole codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). "
            + "Values from the provided codelist should be used wherever possible, though extended values can be "
            + "provided if the codelist does not have a relevant code.")
    private List<String> roles = new ArrayList<String>();
    /**
     * Details
     * <p>
     * Additional classification information about parties can be provided using partyDetail extensions that define
     * particular properties and classification schemes.
     */
    @JsonProperty("details")
    @JsonPropertyDescription("Additional classification information about parties can be provided using partyDetail "
            + "extensions that define particular properties and classification schemes. ")
    private Details details;

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Identifier
     * <p>
     */
    @JsonProperty("identifier")
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Identifier
     * <p>
     */
    @JsonProperty("identifier")
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * Additional identifiers
     * <p>
     * A list of additional / supplemental identifiers for the organization or participant, using the [organization
     * identifier guidance](http://standard.open-contracting.org/latest/en/schema/identifiers/). This could be used
     * to provide an internally used identifier for this organization in addition to the primary legal entity
     * identifier.
     */
    @JsonProperty("additionalIdentifiers")
    public Set<Identifier> getAdditionalIdentifiers() {
        return additionalIdentifiers;
    }

    /**
     * Additional identifiers
     * <p>
     * A list of additional / supplemental identifiers for the organization or participant, using the [organization
     * identifier guidance](http://standard.open-contracting.org/latest/en/schema/identifiers/). This could be used
     * to provide an internally used identifier for this organization in addition to the primary legal entity
     * identifier.
     */
    @JsonProperty("additionalIdentifiers")
    public void setAdditionalIdentifiers(Set<Identifier> additionalIdentifiers) {
        this.additionalIdentifiers = additionalIdentifiers;
    }

    /**
     * Address
     * <p>
     * An address. This may be the legally registered address of the organization, or may be a correspondence address
     * for this particular contracting process.
     */
    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    /**
     * Address
     * <p>
     * An address. This may be the legally registered address of the organization, or may be a correspondence address
     * for this particular contracting process.
     */
    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Contact point
     * <p>
     * An person, contact point or department to contact in relation to this contracting process.
     */
    @JsonProperty("contactPoint")
    public ContactPoint getContactPoint() {
        return contactPoint;
    }

    /**
     * Contact point
     * <p>
     * An person, contact point or department to contact in relation to this contracting process.
     */
    @JsonProperty("contactPoint")
    public void setContactPoint(ContactPoint contactPoint) {
        this.contactPoint = contactPoint;
    }

    /**
     * Party roles
     * <p>
     * The party's role(s) in the contracting process. Role(s) should be taken from the [partyRole codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). Values from the provided
     * codelist should be used wherever possible, though extended values can be provided if the codelist does not
     * have a relevant code.
     */
    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Party roles
     * <p>
     * The party's role(s) in the contracting process. Role(s) should be taken from the [partyRole codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). Values from the provided
     * codelist should be used wherever possible, though extended values can be provided if the codelist does not
     * have a relevant code.
     */
    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Details
     * <p>
     * Additional classification information about parties can be provided using partyDetail extensions that define
     * particular properties and classification schemes.
     */
    @JsonProperty("details")
    public Details getDetails() {
        return details;
    }

    /**
     * Details
     * <p>
     * Additional classification information about parties can be provided using partyDetail extensions that define
     * particular properties and classification schemes.
     */
    @JsonProperty("details")
    public void setDetails(Details details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("id", id)
                .append("identifier", identifier)
                .append("additionalIdentifiers", additionalIdentifiers)
                .append("address", address)
                .append("contactPoint", contactPoint)
                .append("roles", roles)
                .append("details", details)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(identifier)
                .append(address)
                .append(contactPoint)
                .append(roles)
                .append(name)
                .append(additionalIdentifiers)
                .append(details)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Organization)) {
            return false;
        }
        Organization rhs = ((Organization) other);
        return new EqualsBuilder().append(identifier, rhs.identifier)
                .append(address, rhs.address)
                .append(contactPoint, rhs.contactPoint)
                .append(roles, rhs.roles)
                .append(name, rhs.name)
                .append(additionalIdentifiers, rhs.additionalIdentifiers)
                .append(details, rhs.details)
                .append(id, rhs.id)
                .isEquals();
    }

    @Override
    public Serializable getIdProperty() {
        return id;
    }


    @Deprecated
    public enum OrganizationType {
        procuringEntity("procuringEntity"),

        buyer("buyer"),

        supplier("supplier");

        private final String value;

        private static final Map<String, OrganizationType> CONSTANTS = new HashMap<String, OrganizationType>();

        static {
            for (OrganizationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        OrganizationType(final String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static OrganizationType fromValue(final String value) {
            OrganizationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


}
