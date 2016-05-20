/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai Tender OCDS Entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         tender
 */
public class Tender {
    private String id;

    private String title;

    private String description;

    private String status;

    private List<Item> items = new ArrayList<>();

    private Value minValue;

    private Value value;

    private String procurementMethod;

    private String procurementMethodRationale;

    private String awardCriteria;

    private String awardCriteriaDetails;

    private List<String> submissionMethod = new ArrayList<>();

    private String submissionMethodDetails;

    private Period tenderPeriod;

    private Period enquiryPeriod;

    private Boolean hasEnquiries;

    private String eligibilityCriteria;

    private Period awardPeriod;

    private Integer numberOfTenderers;

    private List<Organization> tenderers = new ArrayList<>();

    private Organization procuringEntity;

    private List<Document> documents = new ArrayList<>();

    private List<Milestone> milestone = new ArrayList<>();

    private Amendment amendment;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    public Value getMinValue() {
        return minValue;
    }

    public void setMinValue(final Value minValue) {
        this.minValue = minValue;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(final Value value) {
        this.value = value;
    }

    public String getProcurementMethod() {
        return procurementMethod;
    }

    public void setProcurementMethod(final String procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public String getProcurementMethodRationale() {
        return procurementMethodRationale;
    }

    public void setProcurementMethodRationale(final String procurementMethodRationale) {
        this.procurementMethodRationale = procurementMethodRationale;
    }

    public String getAwardCriteria() {
        return awardCriteria;
    }

    public void setAwardCriteria(final String awardCriteria) {
        this.awardCriteria = awardCriteria;
    }

    public String getAwardCriteriaDetails() {
        return awardCriteriaDetails;
    }

    public void setAwardCriteriaDetails(final String awardCriteriaDetails) {
        this.awardCriteriaDetails = awardCriteriaDetails;
    }

    public List<String> getSubmissionMethod() {
        return submissionMethod;
    }

    public void setSubmissionMethod(final List<String> submissionMethod) {
        this.submissionMethod = submissionMethod;
    }

    public String getSubmissionMethodDetails() {
        return submissionMethodDetails;
    }

    public void setSubmissionMethodDetails(final String submissionMethodDetails) {
        this.submissionMethodDetails = submissionMethodDetails;
    }

    public Period getTenderPeriod() {
        return tenderPeriod;
    }

    public void setTenderPeriod(final Period tenderPeriod) {
        this.tenderPeriod = tenderPeriod;
    }

    public Period getEnquiryPeriod() {
        return enquiryPeriod;
    }

    public void setEnquiryPeriod(final Period enquiryPeriod) {
        this.enquiryPeriod = enquiryPeriod;
    }

    public Boolean getHasEnquiries() {
        return hasEnquiries;
    }

    public void setHasEnquiries(final Boolean hasEnquiries) {
        this.hasEnquiries = hasEnquiries;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public void setEligibilityCriteria(final String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public Period getAwardPeriod() {
        return awardPeriod;
    }

    public void setAwardPeriod(final Period awardPeriod) {
        this.awardPeriod = awardPeriod;
    }

    public Integer getNumberOfTenderers() {
        return numberOfTenderers;
    }

    public void setNumberOfTenderers(final Integer numberOfTenderers) {
        this.numberOfTenderers = numberOfTenderers;
    }

    public List<Organization> getTenderers() {
        return tenderers;
    }

    public void setTenderers(final List<Organization> tenderers) {
        this.tenderers = tenderers;
    }

    public Organization getProcuringEntity() {
        return procuringEntity;
    }

    public void setProcuringEntity(final Organization procuringEntity) {
        this.procuringEntity = procuringEntity;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<Document> documents) {
        this.documents = documents;
    }

    public List<Milestone> getMilestone() {
        return milestone;
    }

    public void setMilestone(final List<Milestone> milestone) {
        this.milestone = milestone;
    }

    public Amendment getAmendment() {
        return amendment;
    }

    public void setAmendment(final Amendment amendment) {
        this.amendment = amendment;
    }

}
