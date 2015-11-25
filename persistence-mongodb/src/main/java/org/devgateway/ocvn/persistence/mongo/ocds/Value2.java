/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import org.springframework.format.annotation.NumberFormat;

/**
 * @author mihai
 *
 */
public class Value2 {
	@NumberFormat
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
