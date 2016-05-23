/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mihai Contract OCDS entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         contract
 */

public class Contract {
    private String id;

    private String awardID;

    private String title;

    private String description;

    private String status;

    private Period period;

    private Value value;

    private List<Item> items = new ArrayList<>();

    private Date dateSigned;

    private List<Document> documents = new ArrayList<>();

    private Amendment amendment;

    private Implementation implementation;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAwardID() {
        return awardID;
    }

    public void setAwardID(final String awardID) {
        this.awardID = awardID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(final Value value) {
        this.value = value;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    public Date getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(final Date dateSigned) {
        this.dateSigned = dateSigned;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<Document> documents) {
        this.documents = documents;
    }

    public Amendment getAmendment() {
        return amendment;
    }

    public void setAmendment(final Amendment amendment) {
        this.amendment = amendment;
    }

    public Implementation getImplementation() {
        return implementation;
    }

    public void setImplementation(final Implementation implementation) {
        this.implementation = implementation;
    }

}
