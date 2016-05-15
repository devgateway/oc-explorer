/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai Award OCDS entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         award
 */
public class Award {

	String id;
	String title;
	String desription;

	String status;


	Date date;
	Value value;

	List<Organization> suppliers = new ArrayList<>();
	List<Item> items = new ArrayList<>();
	Period contractPeriod;
	List<Document> documents = new ArrayList<>();
	Amendment amendment;

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

	public String getDesription() {
		return desription;
	}

	public void setDesription(final String desription) {
		this.desription = desription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(final Value value) {
		this.value = value;
	}

	public List<Organization> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(final List<Organization> suppliers) {
		this.suppliers = suppliers;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(final List<Item> items) {
		this.items = items;
	}

	public Period getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(final Period contractPeriod) {
		this.contractPeriod = contractPeriod;
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
}
