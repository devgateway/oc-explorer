/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai Planning OCDS ENtity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         planning
 */
@Document
public class Planning {
	Budget budget;
	String rationale;
	List<Document> documents = new ArrayList<>();

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(final Budget budget) {
		this.budget = budget;
	}

	public String getRationale() {
		return rationale;
	}

	public void setRationale(final String rationale) {
		this.rationale = rationale;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(final List<Document> documents) {
		this.documents = documents;
	}
}
