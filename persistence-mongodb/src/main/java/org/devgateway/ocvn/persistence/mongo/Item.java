/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class Item {
	@Id
	String id;
	String description;
	Classification classification;
	List<Classification> additionalClassifications;
	Integer quantity;
	Unit unit;

	public class Unit {
		String name;
		Value value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Value getValue() {
			return value;
		}

		public void setValue(Value value) {
			this.value = value;
		}
	}

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

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
