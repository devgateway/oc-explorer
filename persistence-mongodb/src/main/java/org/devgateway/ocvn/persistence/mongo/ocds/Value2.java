/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

/**
 * @author mihai
 * Copy of Value, this is created to work around Spring Data MongoDB bugs related with referencing of values
 * as types and properties 
 * @see {@link BigDecimal2}
 * @see {@link Value}
 */
public class Value2 {
	
	BigDecimal2 amount;
	String currency;

	public BigDecimal2 getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal2 amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
