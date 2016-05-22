/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai
 * Implementation OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#implementation
 */
public class Implementation {
    private List<Transaction> transactions = new ArrayList<>();

    private List<Milestone> milestones = new ArrayList<>();

    private List<Document> documents = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
