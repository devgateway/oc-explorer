
package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Implementation
 * <p>
 * Information during the performance / implementation stage of the contract.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "transactions",
        "milestones",
        "documents"
})
public class Implementation {

    /**
     * Transactions
     * <p>
     * A list of the spending transactions made against this contract
     */
    @JsonProperty("transactions")
    @JsonDeserialize(as = LinkedHashSet.class)
    @ExcelExport
    @JsonPropertyDescription("A list of the spending transactions made against this contract")
    private Set<Transaction> transactions = new LinkedHashSet<Transaction>();
    /**
     * Milestones
     * <p>
     * As milestones are completed, milestone completions should be documented.
     */
    @JsonProperty("milestones")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("As milestones are completed, milestone completions should be documented.")
    private Set<Milestone> milestones = new LinkedHashSet<Milestone>();
    /**
     * Documents
     * <p>
     * Documents and reports that are part of the implementation phase e.g. audit and evaluation reports.
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("Documents and reports that are part of the implementation phase e.g. audit and "
            + "evaluation reports.")
    private Set<Document> documents = new LinkedHashSet<Document>();

    /**
     * Transactions
     * <p>
     * A list of the spending transactions made against this contract
     */
    @JsonProperty("transactions")
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Transactions
     * <p>
     * A list of the spending transactions made against this contract
     */
    @JsonProperty("transactions")
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Milestones
     * <p>
     * As milestones are completed, milestone completions should be documented.
     */
    @JsonProperty("milestones")
    public Set<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Milestones
     * <p>
     * As milestones are completed, milestone completions should be documented.
     */
    @JsonProperty("milestones")
    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }

    /**
     * Documents
     * <p>
     * Documents and reports that are part of the implementation phase e.g. audit and evaluation reports.
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * Documents and reports that are part of the implementation phase e.g. audit and evaluation reports.
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("transactions", transactions)
                .append("milestones", milestones)
                .append("documents", documents)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(transactions)
                .append(milestones)
                .append(documents)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Implementation)) {
            return false;
        }
        Implementation rhs = ((Implementation) other);
        return new EqualsBuilder()
                .append(transactions, rhs.transactions)
                .append(milestones, rhs.milestones)
                .append(documents, rhs.documents)
                .isEquals();
    }

}
