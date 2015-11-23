/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.List;

/**
 * @author mihai
 *
 */
public class Implementation {

	List<Transaction> transactions;
	List<Milestone> milestones;
	List<Document> documents;

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
