/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai Planning OCDS ENtity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         planning
 */
public class Planning {
    private Budget budget;

    private String rationale;

    private List<Document> documents = new ArrayList<>();

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
