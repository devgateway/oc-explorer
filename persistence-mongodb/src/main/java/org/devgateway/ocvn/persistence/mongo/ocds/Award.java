/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
public class Award {
	@Id
	String id;
	String title;
	String desription;
	String status;
	Date date;
	Value value;
	List<Organization> suppliers=new ArrayList<>();
	List<Item> items=new ArrayList<>();
	Period contractPeriod;
	List<Document> documents=new ArrayList<>();
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
	public String getDesription() {
		return desription;
	}
	public void setDesription(String desription) {
		this.desription = desription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public List<Organization> getSuppliers() {
		return suppliers;
	}
	public void setSuppliers(List<Organization> suppliers) {
		this.suppliers = suppliers;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Period getContractPeriod() {
		return contractPeriod;
	}
	public void setContractPeriod(Period contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public Amendment getAmendment() {
		return amendment;
	}
	public void setAmendment(Amendment amendment) {
		this.amendment = amendment;
	}
}
