package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Award
 * <p>
 * An award for the given procurement. There may be more than one award per contracting process e.g. because the
 * contract is split among different providers, or because it is a standing offer.
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
        "amendments",
        "amendment"
})
public class Award {

    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process it is
     * part of (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this award. It must be unique and cannot change within the Open "
            + "Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]"
            + "(http://standard.open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;
    /**
     * Title
     * <p>
     * Award title
     */
    @JsonProperty("title")
    @JsonPropertyDescription("Award title")
    private String title;
    /**
     * Description
     * <p>
     * Award description
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Award description")
    private String description;
    /**
     * Award status
     * <p>
     * The current status of the award drawn from the [awardStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#award-status)
     */
    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the award drawn from the [awardStatus codelist](http://standard"
            + ".open-contracting.org/latest/en/schema/codelists/#award-status)")
    private Status status;
    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     */
    @JsonProperty("date")
    @JsonPropertyDescription("The date of the contract award. This is usually the date on which a decision to award "
            + "was made.")

    private Date date;
    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    private Amount value;
    /**
     * Suppliers
     * <p>
     * The suppliers awarded this award. If different suppliers have been awarded different items of values, these
     * should be split into separate award blocks.
     */
    @JsonProperty("suppliers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The suppliers awarded this award. If different suppliers have been awarded different "
            + "items of values, these should be split into separate award blocks.")
    private Set<Buyer> suppliers = new LinkedHashSet<Buyer>();
    /**
     * Items awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible. Items should not be
     * duplicated, but the quantity specified instead.
     */
    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services awarded in this award, broken into line items wherever possible."
            + " Items should not be duplicated, but the quantity specified instead.")
    private Set<Item> items = new LinkedHashSet<Item>();
    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    @JsonPropertyDescription("    ")
    private TenderPeriod contractPeriod;
    /**
     * Documents
     * <p>
     * All documents and attachments related to the award, including any notices.
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the award, including any notices.")
    private Set<Document> documents = new LinkedHashSet<Document>();
    /**
     * Amendments
     * <p>
     * An award amendment is a formal change to the details of the award, and generally involves the publication of a
     * new award notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    @JsonPropertyDescription("An award amendment is a formal change to the details of the award, and generally "
            + "involves the publication of a new award notice/release. The rationale and a description of the changes"
            + " made can be provided here.")
    private List<Amendment> amendments = new ArrayList<Amendment>();
    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    private Amendment amendment;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process it is
     * part of (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Award ID
     * <p>
     * The identifier for this award. It must be unique and cannot change within the Open Contracting Process it is
     * part of (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Title
     * <p>
     * Award title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Title
     * <p>
     * Award title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Description
     * <p>
     * Award description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * Award description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Award status
     * <p>
     * The current status of the award drawn from the [awardStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#award-status)
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Award status
     * <p>
     * The current status of the award drawn from the [awardStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#award-status)
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * Award date
     * <p>
     * The date of the contract award. This is usually the date on which a decision to award was made.
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
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
     * Suppliers
     * <p>
     * The suppliers awarded this award. If different suppliers have been awarded different items of values, these
     * should be split into separate award blocks.
     */
    @JsonProperty("suppliers")
    public Set<Buyer> getSuppliers() {
        return suppliers;
    }

    /**
     * Suppliers
     * <p>
     * The suppliers awarded this award. If different suppliers have been awarded different items of values, these
     * should be split into separate award blocks.
     */
    @JsonProperty("suppliers")
    public void setSuppliers(Set<Buyer> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * Items awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible. Items should not be
     * duplicated, but the quantity specified instead.
     */
    @JsonProperty("items")
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Items awarded
     * <p>
     * The goods and services awarded in this award, broken into line items wherever possible. Items should not be
     * duplicated, but the quantity specified instead.
     */
    @JsonProperty("items")
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    public TenderPeriod getContractPeriod() {
        return contractPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    public void setContractPeriod(TenderPeriod contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the award, including any notices.
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the award, including any notices.
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    /**
     * Amendments
     * <p>
     * An award amendment is a formal change to the details of the award, and generally involves the publication of a
     * new award notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    public List<Amendment> getAmendments() {
        return amendments;
    }

    /**
     * Amendments
     * <p>
     * An award amendment is a formal change to the details of the award, and generally involves the publication of a
     * new award notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    public void setAmendments(List<Amendment> amendments) {
        this.amendments = amendments;
    }

    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    public Amendment getAmendment() {
        return amendment;
    }

    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    public void setAmendment(Amendment amendment) {
        this.amendment = amendment;
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
        return new ToStringBuilder(this).append("id", id)
                .append("title", title)
                .append("description", description)
                .append("status", status)
                .append("date", date)
                .append("value", value)
                .append("suppliers", suppliers)
                .append("items", items)
                .append("contractPeriod", contractPeriod)
                .append("documents", documents)
                .append("amendments", amendments)
                .append("amendment", amendment)
                .append("additionalProperties", additionalProperties)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(date)
                .append(amendment)
                .append(suppliers)
                .append(documents)
                .append(description)
                .append(amendments)
                .append(title)
                .append(contractPeriod)
                .append(id)
                .append(additionalProperties)
                .append(value)
                .append(items)
                .append(status)
                .toHashCode();
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
        return new EqualsBuilder().append(date, rhs.date)
                .append(amendment, rhs.amendment)
                .append(suppliers, rhs.suppliers)
                .append(documents, rhs.documents)
                .append(description, rhs.description)
                .append(amendments, rhs.amendments)
                .append(title, rhs.title)
                .append(contractPeriod, rhs.contractPeriod)
                .append(id, rhs.id)
                .append(additionalProperties, rhs.additionalProperties)
                .append(value, rhs.value)
                .append(items, rhs.items)
                .append(status, rhs.status)
                .isEquals();
    }

    public enum Status {

        PENDING("pending"),
        ACTIVE("active"),
        CANCELLED("cancelled"),
        UNSUCCESSFUL("unsuccessful");
        private static final Map<String, Status> CONSTANTS = new HashMap<String, Status>();

        static {
            for (Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        Status(String value) {
            this.value = value;
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

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

    }

}
