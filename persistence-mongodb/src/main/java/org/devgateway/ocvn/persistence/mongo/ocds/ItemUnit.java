package org.devgateway.ocvn.persistence.mongo.ocds;

/**
 * 
 * @author mihai
 * Unit OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#unit
 */
public class ItemUnit {
	String name;
	Value2 value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Value2 getValue() {
		return value;
	}

	public void setValue(Value2 value) {
		this.value = value;
	}
}
