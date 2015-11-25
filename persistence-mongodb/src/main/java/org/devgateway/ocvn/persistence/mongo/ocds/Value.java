/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.math.BigDecimal;

/**
 * @author mihai
 *
 */
public class Value {
	BigDecimal amount;
	String currency;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
