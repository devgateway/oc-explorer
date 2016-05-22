/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mihai Milestone OCDS entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         milestone
 */
public class Milestone {
    private String id;

    private String title;

    private String description;

    private Date dueDate;

    private Date dateModified;

    private String status;

    private List<Document> documents = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(final Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<Document> documents) {
        this.documents = documents;
    }
}
