package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.ArrayList;
import java.util.List;


/**
 * Planning
 * <p>
 * Information from the planning phase of the contracting process. Note that many other fields may be filled in a
 * planning release, in the appropriate fields in other schema sections, these would likely be estimates at this
 * stage e.g. totalValue in tender
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rationale",
        "budget",
        "documents",
        "milestones"
})
public class Planning {

    /**
     * Rationale
     * <p>
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     */
    @JsonProperty("rationale")
    @JsonPropertyDescription("The rationale for the procurement provided in free text. More detail can be provided in"
            + " an attached document.")
    @ExcelExport
    private String rationale;
    /**
     * Budget information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this
     * contracting process is funded. It draws upon data model of the [Fiscal Data Package](http://fiscal
     * .dataprotocols.org/), and should be used to cross-reference to more detailed information held using a Budget
     * Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a
     * user to manually or automatically cross-reference with another published source of budget and project
     * information.
     */
    @JsonProperty("budget")
    @JsonPropertyDescription("This section contain information about the budget line, and associated projects, "
            + "through which this contracting process is funded. It draws upon data model of the [Fiscal Data "
            + "Package](http://fiscal.dataprotocols.org/), and should be used to cross-reference to more detailed "
            + "information held using a Budget Data Package, or, where no linked Budget Data Package is available, to"
            + " provide enough information to allow a user to manually or automatically cross-reference with another "
            + "published source of budget and project information.")
    @ExcelExport
    private Budget budget;
    /**
     * Documents
     * <p>
     * A list of documents related to the planning process.
     */
    @JsonProperty("documents")
    @JsonPropertyDescription("A list of documents related to the planning process.")
    private List<Document> documents = new ArrayList<Document>();
    /**
     * Planning milestones
     * <p>
     * A list of milestones associated with the planning stage.
     */
    @JsonProperty("milestones")
    @JsonPropertyDescription("A list of milestones associated with the planning stage.")
    private List<Milestone> milestones = new ArrayList<Milestone>();

    /**
     * Rationale
     * <p>
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     */
    @JsonProperty("rationale")
    public String getRationale() {
        return rationale;
    }

    /**
     * Rationale
     * <p>
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     */
    @JsonProperty("rationale")
    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    /**
     * Budget information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this
     * contracting process is funded. It draws upon data model of the [Fiscal Data Package](http://fiscal
     * .dataprotocols.org/), and should be used to cross-reference to more detailed information held using a Budget
     * Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a
     * user to manually or automatically cross-reference with another published source of budget and project
     * information.
     */
    @JsonProperty("budget")
    public Budget getBudget() {
        return budget;
    }

    /**
     * Budget information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this
     * contracting process is funded. It draws upon data model of the [Fiscal Data Package](http://fiscal
     * .dataprotocols.org/), and should be used to cross-reference to more detailed information held using a Budget
     * Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a
     * user to manually or automatically cross-reference with another published source of budget and project
     * information.
     */
    @JsonProperty("budget")
    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    /**
     * Documents
     * <p>
     * A list of documents related to the planning process.
     */
    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * A list of documents related to the planning process.
     */
    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * Planning milestones
     * <p>
     * A list of milestones associated with the planning stage.
     */
    @JsonProperty("milestones")
    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Planning milestones
     * <p>
     * A list of milestones associated with the planning stage.
     */
    @JsonProperty("milestones")
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("rationale", rationale)
                .append("budget", budget)
                .append("documents", documents)
                .append("milestones", milestones)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(milestones)
                .append(rationale)
                .append(documents)
                .append(budget)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Planning)) {
            return false;
        }
        Planning rhs = ((Planning) other);
        return new EqualsBuilder()
                .append(milestones, rhs.milestones)
                .append(rationale, rhs.rationale)
                .append(documents, rhs.documents)
                .append(budget, rhs.budget)
                .isEquals();
    }

}
