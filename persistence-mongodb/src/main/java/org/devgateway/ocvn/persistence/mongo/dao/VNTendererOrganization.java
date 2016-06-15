/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.dao;

import org.devgateway.ocds.persistence.mongo.Amount;

/**
 * @author mihai
 *
 */
public class VNTendererOrganization extends VNOrganization {

	private Amount bidValue;

	public VNTendererOrganization(VNOrganization organization) {
		this.setId(organization.getId());
		this.setAddress(organization.getAddress());
		this.setContactPoint(organization.getContactPoint());
		this.setIdentifier(organization.getIdentifier());
		this.setName(organization.getName());
		this.setProcuringEntity(organization.getProcuringEntity());
	}

	public Amount getBidValue() {
		return bidValue;
	}

	public void setBidValue(Amount bidValue) {
		this.bidValue = bidValue;
	}

}
