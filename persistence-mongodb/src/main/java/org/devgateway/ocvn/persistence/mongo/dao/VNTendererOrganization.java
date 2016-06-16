/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.dao;

import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

/**
 * @author mihai
 *
 */
public class VNTendererOrganization extends VNOrganization {
    @ExcelExport
	private Amount bidValue;

    public VNTendererOrganization() {

    }

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
