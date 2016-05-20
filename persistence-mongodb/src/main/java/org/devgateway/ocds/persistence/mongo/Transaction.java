/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.Date;

/**
 * @author mihai
 * Transaction OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#transaction
 */
public class Transaction {
    private String id;

    private String source;

    private Date date;

    private Value amount;

    private Identifier providerOrganization;

    private Identifier receiverOrganization;

    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Value getAmount() {
        return amount;
    }

    public void setAmount(Value amount) {
        this.amount = amount;
    }

    public Identifier getProviderOrganization() {
        return providerOrganization;
    }

    public void setProviderOrganization(Identifier providerOrganization) {
        this.providerOrganization = providerOrganization;
    }

    public Identifier getReceiverOrganization() {
        return receiverOrganization;
    }

    public void setReceiverOrganization(Identifier receiverOrganization) {
        this.receiverOrganization = receiverOrganization;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
