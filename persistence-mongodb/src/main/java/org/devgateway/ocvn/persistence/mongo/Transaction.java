/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class Transaction {

	@Id
	String id;
	String source;
	Date date;
	Value amount;
	Identifier providerOrganization;
	Identifier receiverOrganization;
	String uri;
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
