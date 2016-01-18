/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document
public class Tender {

	@Id
	String id;
	String title;
	String description;
	String status;
	List<Item> items=new ArrayList<>();
	Value minValue;
	Value value;
	@Indexed
	String procurementMethod;
	String procurementMethodRationale;
	String awardCriteria;
	String awardCriteriaDetails;
	List<String> submissionMethod=new ArrayList<>();
	String submissionMethodDetails;
	@Indexed
	Period tenderPeriod;
	Period enquiryPeriod;
	Boolean hasEnquiries;
	String eligibilityCriteria;
	Period awardPeriod;
	Integer numberOfTenders;
	List<Organization> tenderers=new ArrayList<>();
	Organization procuringEntity;
	List<Document> documents=new ArrayList<>();
	List<Milestone> milestone=new ArrayList<>();
	Amendment amendment;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Value getMinValue() {
		return minValue;
	}
	public void setMinValue(Value minValue) {
		this.minValue = minValue;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getProcurementMethod() {
		return procurementMethod;
	}
	public void setProcurementMethod(String procurementMethod) {
		this.procurementMethod = procurementMethod;
	}
	public String getProcurementMethodRationale() {
		return procurementMethodRationale;
	}
	public void setProcurementMethodRationale(String procurementMethodRationale) {
		this.procurementMethodRationale = procurementMethodRationale;
	}
	public String getAwardCriteria() {
		return awardCriteria;
	}
	public void setAwardCriteria(String awardCriteria) {
		this.awardCriteria = awardCriteria;
	}
	public String getAwardCriteriaDetails() {
		return awardCriteriaDetails;
	}
	public void setAwardCriteriaDetails(String awardCriteriaDetails) {
		this.awardCriteriaDetails = awardCriteriaDetails;
	}
	public List<String> getSubmissionMethod() {
		return submissionMethod;
	}
	public void setSubmissionMethod(List<String> submissionMethod) {
		this.submissionMethod = submissionMethod;
	}
	public String getSubmissionMethodDetails() {
		return submissionMethodDetails;
	}
	public void setSubmissionMethodDetails(String submissionMethodDetails) {
		this.submissionMethodDetails = submissionMethodDetails;
	}
	public Period getTenderPeriod() {
		return tenderPeriod;
	}
	public void setTenderPeriod(Period tenderPeriod) {
		this.tenderPeriod = tenderPeriod;
	}
	public Period getEnquiryPeriod() {
		return enquiryPeriod;
	}
	public void setEnquiryPeriod(Period enquiryPeriod) {
		this.enquiryPeriod = enquiryPeriod;
	}
	public Boolean getHasEnquiries() {
		return hasEnquiries;
	}
	public void setHasEnquiries(Boolean hasEnquiries) {
		this.hasEnquiries = hasEnquiries;
	}
	public String getEligibilityCriteria() {
		return eligibilityCriteria;
	}
	public void setEligibilityCriteria(String eligibilityCriteria) {
		this.eligibilityCriteria = eligibilityCriteria;
	}
	public Period getAwardPeriod() {
		return awardPeriod;
	}
	public void setAwardPeriod(Period awardPeriod) {
		this.awardPeriod = awardPeriod;
	}
	public Integer getNumberOfTenders() {
		return numberOfTenders;
	}
	public void setNumberOfTenders(Integer numberOfTenders) {
		this.numberOfTenders = numberOfTenders;
	}
	public List<Organization> getTenderers() {
		return tenderers;
	}
	public void setTenderers(List<Organization> tenderers) {
		this.tenderers = tenderers;
	}
	public Organization getProcuringEntity() {
		return procuringEntity;
	}
	public void setProcuringEntity(Organization procuringEntity) {
		this.procuringEntity = procuringEntity;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public List<Milestone> getMilestone() {
		return milestone;
	}
	public void setMilestone(List<Milestone> milestone) {
		this.milestone = milestone;
	}
	public Amendment getAmendment() {
		return amendment;
	}
	public void setAmendment(Amendment amendment) {
		this.amendment = amendment;
	}
	
	
	
}
