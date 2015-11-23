/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.List;

/**
 * @author mihai
 *
 */
public class Planning {
	Budget budget;
	String rationale;
	List<Document> documents;

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
