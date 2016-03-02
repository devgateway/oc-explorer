/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.Date;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.ocvn.persistence.mongo.ocds.Tender;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document(collection="tender")
public class VNTender extends Tender {
	Integer bidMethod;
	Integer contrMethod;
	Organization orderIntituCd;
	String approveState;
	String cancelYN;
	String modYn;
	
	Date bidOpenDt;
	

	public Integer getBidMethod() {
		return bidMethod;
	}

	public void setBidMethod(Integer bidMethod) {
		this.bidMethod = bidMethod;
	}

	public Integer getContrMethod() {
		return contrMethod;
	}

	public void setContrMethod(Integer contrMethod) {
		this.contrMethod = contrMethod;
	}

	public Organization getOrderIntituCd() {
		return orderIntituCd;
	}

	public void setOrderIntituCd(Organization orderIntituCd) {
		this.orderIntituCd = orderIntituCd;
	}

	public String getApproveState() {
		return approveState;
	}

	public void setApproveState(String approveState) {
		this.approveState = approveState;
	}

	public String getCancelYN() {
		return cancelYN;
	}

	public void setCancelYN(String cancelYN) {
		this.cancelYN = cancelYN;
	}

	public String getModYn() {
		return modYn;
	}

	public void setModYn(String modYn) {
		this.modYn = modYn;
	}

	public Date getBidOpenDt() {
		return bidOpenDt;
	}

	public void setBidOpenDt(Date bidOpenDt) {
		this.bidOpenDt = bidOpenDt;
	}


}
