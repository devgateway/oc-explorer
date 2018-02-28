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
import org.devgateway.ocds.persistence.mongo.merge.Merge;
import org.devgateway.ocds.persistence.mongo.merge.MergeStrategy;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Bid
 * <p>
 * For representing a bid in response to the tender or qualification stage in this contracting process.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "date",
        "status",
        "tenderers",
        "value",
        "documents"
})
public class Detail {

    /**
     * ID
     * <p>
     * A local identifier for this bid
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this bid")
    private String id;
    /**
     * Date
     * <p>
     * The date when this bid was received.
     */
    @JsonProperty("date")
    @JsonPropertyDescription("The date when this bid was received.")
    private Date date;
    /**
     * Status
     * <p>
     * The status of the bid, drawn from the bidStatus codelist
     */
    @JsonProperty("status")
    @JsonPropertyDescription("The status of the bid, drawn from the bidStatus codelist")
    private String status;
    /**
     * Tenderer
     * <p>
     * The party, or parties, responsible for this bid. This should provide a name and identifier,
     * cross-referenced to an entry in the parties array at the top level of the release.
     */
    @JsonProperty("tenderers")
    @JsonPropertyDescription("The party, or parties, responsible for this bid. This should provide a name and "
            + "identifier, cross-referenced to an entry in the parties array at the top level of the release.")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @Merge(MergeStrategy.ocdsVersion)
    private Set<Organization> tenderers = new LinkedHashSet<>();
    @JsonProperty("value")
    private Amount value;
    /**
     * Documents
     * <p>
     * All documents and attachments related to the bid and its evaluation.
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the bid and its evaluation.")
    private Set<Document> documents = new LinkedHashSet<Document>();

    /**
     * ID
     * <p>
     * A local identifier for this bid
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * A local identifier for this bid
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Date
     * <p>
     * The date when this bid was received.
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * Date
     * <p>
     * The date when this bid was received.
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Status
     * <p>
     * The status of the bid, drawn from the bidStatus codelist
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * Status
     * <p>
     * The status of the bid, drawn from the bidStatus codelist
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Tenderer
     * <p>
     * The party, or parties, responsible for this bid. This should provide a name and identifier, cross-referenced
     * to an entry in the parties array at the top level of the release.
     */
    @JsonProperty("tenderers")
    public Set<Organization> getTenderers() {
        return tenderers;
    }

    /**
     * Tenderer
     * <p>
     * The party, or parties, responsible for this bid. This should provide a name and identifier, cross-referenced
     * to an entry in the parties array at the top level of the release.
     */
    @JsonProperty("tenderers")
    public void setTenderers(Set<Organization> tenderers) {
        this.tenderers = tenderers;
    }

    @JsonProperty("value")
    public Amount getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Amount value) {
        this.value = value;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the bid and its evaluation.
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the bid and its evaluation.
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(date).append(status).append(tenderers).append(value)
                .append(documents).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Detail)) {
            return false;
        }
        Detail rhs = ((Detail) other);
        return new EqualsBuilder().append(id, rhs.id).append(date, rhs.date).append(status, rhs.status)
                .append(tenderers, rhs.tenderers).append(value, rhs.value).append(documents, rhs.documents
               ).isEquals();
    }


    /**
     * see https://github.com/open-contracting/ocds_bid_extension/blob/master/codelists/bidStatus.csv
     */
    public enum Status {

        invited("invited"),
        cancelled("pending"),
        valid("valid"),
        disqualified("disqualified"),
        withdrawn("withdrawn");

        private final String value;
        private static final Map<String, Detail.Status> CONSTANTS = new HashMap<String, Detail.Status>();

        static {
            for (Detail.Status c : values()) {
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
        public static Detail.Status fromValue(String value) {
            Detail.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
