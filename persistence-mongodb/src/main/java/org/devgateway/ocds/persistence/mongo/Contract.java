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
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Contract
 * <p>
 * Information regarding the signed contract between the buyer and supplier(s).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "awardID",
        "title",
        "description",
        "status",
        "period",
        "value",
        "items",
        "dateSigned",
        "documents",
        "implementation",
        "relatedProcesses",
        "milestones",
        "amendments",
        "amendment"
})
public class Contract {

    /**
     * Contract ID
     * <p>
     * The identifier for this contract. It must be unique and cannot change within its Open Contracting Process
     * (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    @ExcelExport
    @JsonPropertyDescription("The identifier for this contract. It must be unique and cannot change within its Open "
            + "Contracting Process (defined by a single ocid). See the [identifier guidance](http://standard"
            + ".open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;
    /**
     * Award ID
     * <p>
     * The award.id against which this contract is being issued.
     * (Required)
     */
    @JsonProperty("awardID")
    @ExcelExport
    @JsonPropertyDescription("The award.id against which this contract is being issued.")
    private String awardID;
    /**
     * Contract title
     * <p>
     * Contract title
     */
    @JsonProperty("title")
    @ExcelExport
    @JsonPropertyDescription("Contract title")
    private String title;
    /**
     * Contract description
     * <p>
     * Contract description
     */
    @JsonProperty("description")
    @ExcelExport
    @JsonPropertyDescription("Contract description")
    private String description;
    /**
     * Contract status
     * <p>
     * The current status of the contract. Drawn from the [contractStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#contract-status)
     */
    @JsonProperty("status")
    @ExcelExport
    @JsonPropertyDescription("The current status of the contract. Drawn from the [contractStatus codelist]"
            + "(http://standard.open-contracting.org/latest/en/schema/codelists/#contract-status)")
    private Status status;
    /**
     * Period
     * <p>
     */
    @JsonProperty("period")
    @ExcelExport
    @JsonPropertyDescription("    ")
    private TenderPeriod period;
    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    @ExcelExport
    private Amount value;
    /**
     * Items contracted
     * <p>
     * The goods, services, and any intangible outcomes in this contract. Note: If the items are the same as the
     * award do not repeat.
     */
    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods, services, and any intangible outcomes in this contract. Note: If the items "
            + "are the same as the award do not repeat.")
    @ExcelExport
    @ExcelExportSepareteSheet
    private Set<Item> items = new LinkedHashSet<Item>();
    /**
     * Date signed
     * <p>
     * The date the contract was signed. In the case of multiple signatures, the date of the last signature.
     */
    @JsonProperty("dateSigned")
    @ExcelExport
    @JsonPropertyDescription("The date the contract was signed. In the case of multiple signatures, the date of the "
            + "last signature.")
    private Date dateSigned;
    /**
     * Documents
     * <p>
     * All documents and attachments related to the contract, including any notices.
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the contract, including any notices.")
    private Set<Document> documents = new LinkedHashSet<Document>();
    /**
     * Implementation
     * <p>
     * Information during the performance / implementation stage of the contract.
     */
    @JsonProperty("implementation")
    @ExcelExport
    @JsonPropertyDescription("Information during the performance / implementation stage of the contract.")
    private Implementation implementation;
    /**
     * Related processes
     * <p>
     * If this process is followed by one or more contracting processes, represented under a separate open
     * contracting identifier (ocid) then details of the related process can be provided here. This is commonly used
     * to point to subcontracts, or to renewal and replacement processes for this contract.
     */
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("If this process is followed by one or more contracting processes, represented under a "
            + "separate open contracting identifier (ocid) then details of the related process can be provided here. "
            + "This is commonly used to point to subcontracts, or to renewal and replacement processes for this "
            + "contract.")
    private Set<RelatedProcess> relatedProcesses = new LinkedHashSet<RelatedProcess>();
    /**
     * Contract milestones
     * <p>
     * A list of milestones associated with the finalization of this contract.
     */
    @JsonProperty("milestones")
    @JsonPropertyDescription("A list of milestones associated with the finalization of this contract.")
    private List<Milestone> milestones = new ArrayList<Milestone>();
    /**
     * Amendments
     * <p>
     * A contract amendment is a formal change to, or extension of, a contract, and generally involves the
     * publication of a new contract notice/release, or some other documents detailing the change. The rationale and
     * a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    @JsonPropertyDescription("A contract amendment is a formal change to, or extension of, a contract, and generally "
            + "involves the publication of a new contract notice/release, or some other documents detailing the "
            + "change. The rationale and a description of the changes made can be provided here.")
    private List<Amendment> amendments = new ArrayList<Amendment>();
    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    private Amendment amendment;


    /**
     * Contract ID
     * <p>
     * The identifier for this contract. It must be unique and cannot change within its Open Contracting Process
     * (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Contract ID
     * <p>
     * The identifier for this contract. It must be unique and cannot change within its Open Contracting Process
     * (defined by a single ocid). See the [identifier guidance](http://standard.open-contracting
     * .org/latest/en/schema/identifiers/) for further details.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Award ID
     * <p>
     * The award.id against which this contract is being issued.
     * (Required)
     */
    @JsonProperty("awardID")
    public String getAwardID() {
        return awardID;
    }

    /**
     * Award ID
     * <p>
     * The award.id against which this contract is being issued.
     * (Required)
     */
    @JsonProperty("awardID")
    public void setAwardID(String awardID) {
        this.awardID = awardID;
    }

    /**
     * Contract title
     * <p>
     * Contract title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Contract title
     * <p>
     * Contract title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Contract description
     * <p>
     * Contract description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Contract description
     * <p>
     * Contract description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Contract status
     * <p>
     * The current status of the contract. Drawn from the [contractStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#contract-status)
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Contract status
     * <p>
     * The current status of the contract. Drawn from the [contractStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#contract-status)
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("period")
    public TenderPeriod getPeriod() {
        return period;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("period")
    public void setPeriod(TenderPeriod period) {
        this.period = period;
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
     * Items contracted
     * <p>
     * The goods, services, and any intangible outcomes in this contract. Note: If the items are the same as the
     * award do not repeat.
     */
    @JsonProperty("items")
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Items contracted
     * <p>
     * The goods, services, and any intangible outcomes in this contract. Note: If the items are the same as the
     * award do not repeat.
     */
    @JsonProperty("items")
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Date signed
     * <p>
     * The date the contract was signed. In the case of multiple signatures, the date of the last signature.
     */
    @JsonProperty("dateSigned")
    public Date getDateSigned() {
        return dateSigned;
    }

    /**
     * Date signed
     * <p>
     * The date the contract was signed. In the case of multiple signatures, the date of the last signature.
     */
    @JsonProperty("dateSigned")
    public void setDateSigned(Date dateSigned) {
        this.dateSigned = dateSigned;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the contract, including any notices.
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the contract, including any notices.
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    /**
     * Implementation
     * <p>
     * Information during the performance / implementation stage of the contract.
     */
    @JsonProperty("implementation")
    public Implementation getImplementation() {
        return implementation;
    }

    /**
     * Implementation
     * <p>
     * Information during the performance / implementation stage of the contract.
     */
    @JsonProperty("implementation")
    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    /**
     * Related processes
     * <p>
     * If this process is followed by one or more contracting processes, represented under a separate open
     * contracting identifier (ocid) then details of the related process can be provided here. This is commonly used
     * to point to subcontracts, or to renewal and replacement processes for this contract.
     */
    @JsonProperty("relatedProcesses")
    public Set<RelatedProcess> getRelatedProcesses() {
        return relatedProcesses;
    }

    /**
     * Related processes
     * <p>
     * If this process is followed by one or more contracting processes, represented under a separate open
     * contracting identifier (ocid) then details of the related process can be provided here. This is commonly used
     * to point to subcontracts, or to renewal and replacement processes for this contract.
     */
    @JsonProperty("relatedProcesses")
    public void setRelatedProcesses(Set<RelatedProcess> relatedProcesses) {
        this.relatedProcesses = relatedProcesses;
    }

    /**
     * Contract milestones
     * <p>
     * A list of milestones associated with the finalization of this contract.
     */
    @JsonProperty("milestones")
    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Contract milestones
     * <p>
     * A list of milestones associated with the finalization of this contract.
     */
    @JsonProperty("milestones")
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

    /**
     * Amendments
     * <p>
     * A contract amendment is a formal change to, or extension of, a contract, and generally involves the
     * publication of a new contract notice/release, or some other documents detailing the change. The rationale and
     * a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    public List<Amendment> getAmendments() {
        return amendments;
    }

    /**
     * Amendments
     * <p>
     * A contract amendment is a formal change to, or extension of, a contract, and generally involves the
     * publication of a new contract notice/release, or some other documents detailing the change. The rationale and
     * a description of the changes made can be provided here.
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



    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("awardID", awardID)
                .append("title", title)
                .append("description", description)
                .append("status", status)
                .append("period", period)
                .append("value", value)
                .append("items", items)
                .append("dateSigned", dateSigned)
                .append("documents", documents)
                .append("implementation", implementation)
                .append("relatedProcesses", relatedProcesses)
                .append("milestones", milestones)
                .append("amendments", amendments)
                .append("amendment", amendment)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(awardID)
                .append(amendment)
                .append(period)
                .append(documents)
                .append(relatedProcesses)
                .append(implementation)
                .append(description)
                .append(amendments)
                .append(title)
                .append(id)
                .append(dateSigned)
                .append(milestones)
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
        if (!(other instanceof Contract)) {
            return false;
        }
        Contract rhs = ((Contract) other);
        return new EqualsBuilder().append(awardID, rhs.awardID)
                .append(amendment, rhs.amendment)
                .append(period, rhs.period)
                .append(documents, rhs.documents)
                .append(relatedProcesses, rhs.relatedProcesses)
                .append(implementation, rhs.implementation)
                .append(description, rhs.description)
                .append(amendments, rhs.amendments)
                .append(title, rhs.title)
                .append(id, rhs.id)
                .append(dateSigned, rhs.dateSigned)
                .append(milestones, rhs.milestones)
                .append(value, rhs.value)
                .append(items, rhs.items)
                .append(status, rhs.status)
                .isEquals();
    }

    public enum Status {

        pending("pending"),
        active("active"),
        cancelled("cancelled"),
        terminated("terminated");
        private final String value;
        private static final Map<String, Status> CONSTANTS = new HashMap<String, Status>();

        static {
            for (Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
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
