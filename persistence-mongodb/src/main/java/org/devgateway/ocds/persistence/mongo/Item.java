/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai Item OCDS Entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#item
 */
public class Item {

	String id;
	String description;
	Classification classification;
	List<Classification> additionalClassifications = new ArrayList<>();
	Integer quantity;
	ItemUnit unit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public List<Classification> getAdditionalClassifications() {
		return additionalClassifications;
	}

	public void setAdditionalClassifications(List<Classification> additionalClassifications) {
		this.additionalClassifications = additionalClassifications;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ItemUnit getUnit() {
		return unit;
	}

	public void setUnit(ItemUnit unit) {
		this.unit = unit;
	}

}
