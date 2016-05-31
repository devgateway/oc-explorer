package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Award
 * <p>
 * An award for the given procurement. There may be more than one award per contracting process
 * e.g. because the contract is split amongst different providers, or because it is a standing offer.
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#award
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "date",
        "value",
        "suppliers",
        "items",
        "contractPeriod",
        "documents",
        "amendment"
})
public class Award {
    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process
     * it is part of (defined by a single ocid).
     *
     * See the [identifier guidance](http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/)
     * for further details.
     * (Required)
     *
     */
    @JsonProperty("id")
    private String id;

    /**
     * Award title
     *
     */
    @JsonProperty("title")
    private String title;

    /**
     * Award description
     *
     */
    @JsonProperty("description")
    private String description;

    /**
     * Award Status
     * <p>
     * The current status of the award drawn from the
     *  [awardStatus codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#award-status)
     *
     */
    @JsonProperty("status")
    private Status status;

    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     *
     */
    @JsonProperty("date")
    private Date date;

    @JsonProperty("value")
    private Amount value;

    /**
     * The suppliers awarded this award. If different suppliers have been awarded different items of values,
     * these should be split into separate award blocks.
     *
     */
    @JsonProperty("suppliers")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Organization> suppliers = new LinkedHashSet<Organization>();

    /**
     * Items Awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible.
     * Items should not be duplicated, but the quantity specified instead.
     *
     */
    @JsonProperty("items")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Item> items = new LinkedHashSet<Item>();

    /**
     * Period
     * <p>
     *
     *
     */
    @JsonProperty("contractPeriod")
    private Period contractPeriod;

    /**
     * All documents and attachments related to the award, including any notices.
     *
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Document> documents = new LinkedHashSet<Document>();

    /**
     * Amendment information
     * <p>
     *
     *
     */
    @JsonProperty("amendment")
    private Amendment amendment;

    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process
     * it is part of (defined by a single ocid).
     *
     * See the [identifier guidance](http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/)
     * for further details.
     * (Required)
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process
     * it is part of (defined by a single ocid).
     *
     * See the [identifier guidance](http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/)
     * for further details.
     * (Required)
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Award title
     *
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Award title
     *
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Award description
     *
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Award description
     *
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Award Status
     * <p>
     * The current status of the award drawn from the
     * [awardStatus codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#award-status)
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Award Status
     * <p>
     * The current status of the award drawn from the
     *  [awardStatus codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists/#award-status)
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     *
     * @return
     *     The date
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     *
     * @param date
     *     The date
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     *     The value
     */
    @JsonProperty("value")
    public Amount getValue() {
        return value;
    }

    /**
     *
     * @param value
     *     The value
     */
    @JsonProperty("value")
    public void setValue(Amount value) {
        this.value = value;
    }

    /**
     * The suppliers awarded this award. If different suppliers have been awarded different items of values,
     * these should be split into separate award blocks.
     *
     * @return
     *     The suppliers
     */
    @JsonProperty("suppliers")
    public Set<Organization> getSuppliers() {
        return suppliers;
    }

    /**
     * The suppliers awarded this award. If different suppliers have been awarded different items of values,
     * these should be split into separate award blocks.
     *
     * @param suppliers
     *     The suppliers
     */
    @JsonProperty("suppliers")
    public void setSuppliers(Set<Organization> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * Items Awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible.
     * Items should not be duplicated, but the quantity specified instead.
     *
     * @return
     *     The items
     */
    @JsonProperty("items")
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Items Awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible.
     * Items should not be duplicated, but the quantity specified instead.
     *
     * @param items
     *     The items
     */
    @JsonProperty("items")
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Period
     * <p>
     *
     *
     * @return
     *     The contractPeriod
     */
    @JsonProperty("contractPeriod")
    public Period getContractPeriod() {
        return contractPeriod;
    }

    /**
     * Period
     * <p>
     *
     *
     * @param contractPeriod
     *     The contractPeriod
     */
    @JsonProperty("contractPeriod")
    public void setContractPeriod(Period contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    /**
     * All documents and attachments related to the award, including any notices.
     *
     * @return
     *     The documents
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * All documents and attachments related to the award, including any notices.
     *
     * @param documents
     *     The documents
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    /**
     * Amendment information
     * <p>
     *
     *
     * @return
     *     The amendment
     */
    @JsonProperty("amendment")
    public Amendment getAmendment() {
        return amendment;
    }

    /**
     * Amendment information
     * <p>
     *
     *
     * @param amendment
     *     The amendment
     */
    @JsonProperty("amendment")
    public void setAmendment(Amendment amendment) {
        this.amendment = amendment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(id).
                append(title).
                append(description).
                append(status).
                append(date).
                append(value).
                append(suppliers).
                append(items).
                append(contractPeriod).
                append(documents).
                append(amendment).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Award)) {
            return false;
        }
        Award rhs = ((Award) other);
        return new EqualsBuilder().
                append(id, rhs.id).
                append(title, rhs.title).
                append(description, rhs.description).
                append(status, rhs.status).
                append(date, rhs.date).
                append(value, rhs.value).
                append(suppliers, rhs.suppliers).
                append(items, rhs.items).
                append(contractPeriod, rhs.contractPeriod).
                append(documents, rhs.documents).
                append(amendment, rhs.amendment).
                isEquals();
    }

    public enum Status {
        PENDING("pending"),

        ACTIVE("active"),

        CANCELLED("cancelled"),

        UNSUCCESSFUL("unsuccessful");

        private final String value;

        private static final Map<String, Status> CONSTANTS = new HashMap<String, Status>();

        static {
            for (Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Status fromValue(String value) {
            Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
}