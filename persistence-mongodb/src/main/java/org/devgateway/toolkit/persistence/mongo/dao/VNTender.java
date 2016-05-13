/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.Date;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.ocvn.persistence.mongo.ocds.Tender;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai Extension of {@link Tender} to allow extra Vietnam-specific
 *         fields
 */
@Document(collection = "tender")
public class VNTender extends Tender {
	private Integer bidMethod;

	@Indexed
	private Integer contrMethod;
	private String contrMethodDetails;
	private Organization orderIntituCd;
	private String approveState;
	private String cancelYN;
	private String modYn;
	private String procurementMethodDetails;

	
	public String getProcurementMethodDetails() {
		return procurementMethodDetails;
	}

	public void setProcurementMethodDetails(String procurementMethodDetails) {
		this.procurementMethodDetails = procurementMethodDetails;
	}

	public String getContrMethodDetails() {
		return contrMethodDetails;
	}

	public void setContrMethodDetails(String contrMethodDetails) {
		this.contrMethodDetails = contrMethodDetails;
	}

	private Date bidOpenDt;

	public Integer getBidMethod() {
		return bidMethod;
	}

	public void setBidMethod(final Integer bidMethod) {
		this.bidMethod = bidMethod;
	}

	public Integer getContrMethod() {
		return contrMethod;
	}

	public void setContrMethod(final Integer contrMethod) {
		this.contrMethod = contrMethod;
	}

	public Organization getOrderIntituCd() {
		return orderIntituCd;
	}

	public void setOrderIntituCd(final Organization orderIntituCd) {
		this.orderIntituCd = orderIntituCd;
	}

	public String getApproveState() {
		return approveState;
	}

	public void setApproveState(final String approveState) {
		this.approveState = approveState;
	}

	public String getCancelYN() {
		return cancelYN;
	}

	public void setCancelYN(final String cancelYN) {
		this.cancelYN = cancelYN;
	}

	public String getModYn() {
		return modYn;
	}

	public void setModYn(final String modYn) {
		this.modYn = modYn;
	}

	public Date getBidOpenDt() {
		return bidOpenDt;
	}

	public void setBidOpenDt(final Date bidOpenDt) {
		this.bidOpenDt = bidOpenDt;
	}

}
