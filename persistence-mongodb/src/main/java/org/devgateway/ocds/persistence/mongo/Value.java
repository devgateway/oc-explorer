/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.math.BigDecimal;

/**
 * @author mihai
 * Value OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#value
 */
public class Value {
    private BigDecimal amount;

    private String currency;

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
