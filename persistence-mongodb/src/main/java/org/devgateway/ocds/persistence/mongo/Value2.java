/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

/**
 * @author mihai
 * Copy of Value, this is created to work around Spring Data MongoDB bugs related with referencing of values
 * as types and properties 
 * @see {@link BigDecimal2}
 * @see {@link Value}
 */
public class Value2 {
    private BigDecimal2 amount;

    private String currency;

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
