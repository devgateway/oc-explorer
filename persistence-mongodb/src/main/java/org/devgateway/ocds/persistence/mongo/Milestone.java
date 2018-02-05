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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Milestone
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "type",
        "description",
        "code",
        "dueDate",
        "dateMet",
        "dateModified",
        "status",
        "documents"
})
public class Milestone {

    /**
     * ID
     * <p>
     * A local identifier for this milestone, unique within this block. This field is used to keep track of multiple
     * revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this milestone, unique within this block. This field is used to "
            + "keep track of multiple revisions of a milestone through the compilation from release to record "
            + "mechanism.")
    private String id;
    /**
     * Title
     * <p>
     * Milestone title
     */
    @JsonProperty("title")
    @JsonPropertyDescription("Milestone title")
    private String title;
    /**
     * Milestone type
     * <p>
     * The type of milestone, drawn from an extended [milestoneType codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#milestone-type).
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The type of milestone, drawn from an extended [milestoneType codelist](http://standard"
            + ".open-contracting.org/latest/en/schema/codelists/#milestone-type).")
    private String type;
    /**
     * Description
     * <p>
     * A description of the milestone.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A description of the milestone.")
    private String description;
    /**
     * Milestone code
     * <p>
     * Milestone codes can be used to track specific events that take place for a particular kind of contracting
     * process. For example, a code of 'approvalLetter' could be used to allow applications to understand this
     * milestone represents the date an approvalLetter is due or signed. Milestone codes is an open codelist, and
     * codes should be agreed among data producers and the applications using that data.
     */
    @JsonProperty("code")
    @JsonPropertyDescription("Milestone codes can be used to track specific events that take place for a particular "
            + "kind of contracting process. For example, a code of 'approvalLetter' could be used to allow "
            + "applications to understand this milestone represents the date an approvalLetter is due or signed. "
            + "Milestone codes is an open codelist, and codes should be agreed among data producers and the "
            + "applications using that data.")
    private String code;
    /**
     * Due date
     * <p>
     * The date the milestone is due.
     */
    @JsonProperty("dueDate")
    @JsonPropertyDescription("The date the milestone is due.")
    private Date dueDate;
    /**
     * Date met
     * <p>
     * The date on which the milestone was met.
     */
    @JsonProperty("dateMet")
    @JsonPropertyDescription("The date on which the milestone was met.")
    private Date dateMet;
    /**
     * Date modified
     * <p>
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     */
    @JsonProperty("dateModified")
    @JsonPropertyDescription("The date the milestone was last reviewed or modified and the status was altered or "
            + "confirmed to still be correct.")
    private Date dateModified;
    /**
     * Status
     * <p>
     * The status that was realized on the date provided in dateModified, drawn from the [milestoneStatus codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#milestone-status).
     */
    @JsonProperty("status")
    @JsonPropertyDescription("The status that was realized on the date provided in dateModified, drawn from the "
            + "[milestoneStatus codelist](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#milestone-status).")
    private Status status;
    /**
     * Documents
     * <p>
     * List of documents associated with this milestone (Deprecated in 1.1).
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("List of documents associated with this milestone (Deprecated in 1.1).")
    private Set<Document> documents = new LinkedHashSet<Document>();

    /**
     * ID
     * <p>
     * A local identifier for this milestone, unique within this block. This field is used to keep track of multiple
     * revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * A local identifier for this milestone, unique within this block. This field is used to keep track of multiple
     * revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Title
     * <p>
     * Milestone title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Title
     * <p>
     * Milestone title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Milestone type
     * <p>
     * The type of milestone, drawn from an extended [milestoneType codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#milestone-type).
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Milestone type
     * <p>
     * The type of milestone, drawn from an extended [milestoneType codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#milestone-type).
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Description
     * <p>
     * A description of the milestone.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * A description of the milestone.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Milestone code
     * <p>
     * Milestone codes can be used to track specific events that take place for a particular kind of contracting
     * process. For example, a code of 'approvalLetter' could be used to allow applications to understand this
     * milestone represents the date an approvalLetter is due or signed. Milestone codes is an open codelist, and
     * codes should be agreed among data producers and the applications using that data.
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * Milestone code
     * <p>
     * Milestone codes can be used to track specific events that take place for a particular kind of contracting
     * process. For example, a code of 'approvalLetter' could be used to allow applications to understand this
     * milestone represents the date an approvalLetter is due or signed. Milestone codes is an open codelist, and
     * codes should be agreed among data producers and the applications using that data.
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Due date
     * <p>
     * The date the milestone is due.
     */
    @JsonProperty("dueDate")
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Due date
     * <p>
     * The date the milestone is due.
     */
    @JsonProperty("dueDate")
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Date met
     * <p>
     * The date on which the milestone was met.
     */
    @JsonProperty("dateMet")
    public Date getDateMet() {
        return dateMet;
    }

    /**
     * Date met
     * <p>
     * The date on which the milestone was met.
     */
    @JsonProperty("dateMet")
    public void setDateMet(Date dateMet) {
        this.dateMet = dateMet;
    }

    /**
     * Date modified
     * <p>
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     */
    @JsonProperty("dateModified")
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * Date modified
     * <p>
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     */
    @JsonProperty("dateModified")
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * Status
     * <p>
     * The status that was realized on the date provided in dateModified, drawn from the [milestoneStatus codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#milestone-status).
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Status
     * <p>
     * The status that was realized on the date provided in dateModified, drawn from the [milestoneStatus codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#milestone-status).
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Documents
     * <p>
     * List of documents associated with this milestone (Deprecated in 1.1).
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * List of documents associated with this milestone (Deprecated in 1.1).
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("title", title)
                .append("type", type)
                .append("description", description)
                .append("code", code)
                .append("dueDate", dueDate)
                .append("dateMet", dateMet)
                .append("dateModified", dateModified)
                .append("status", status)
                .append("documents", documents)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code)
                .append(documents)
                .append(dueDate)
                .append(dateMet)
                .append(description)
                .append(dateModified)
                .append(id)
                .append(title)
                .append(type)
                .append(status)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Milestone)) {
            return false;
        }
        Milestone rhs = ((Milestone) other);
        return new EqualsBuilder().append(code, rhs.code)
                .append(documents, rhs.documents)
                .append(dueDate, rhs.dueDate)
                .append(dateMet, rhs.dateMet)
                .append(description, rhs.description)
                .append(dateModified, rhs.dateModified)
                .append(id, rhs.id)
                .append(title, rhs.title)
                .append(type, rhs.type)
                .append(status, rhs.status)
                .isEquals();
    }

    public enum Status {

        SCHEDULED("scheduled"),
        MET("met"),
        NOT_MET("notMet"),
        PARTIALLY_MET("partiallyMet");
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
