/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.Date;

import org.devgateway.ocvn.persistence.mongo.ocds.Planning;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author mihai Extension of {@link Planning} to allow extra Vietnam-specific
 *         fields
 */
public class VNPlanning extends Planning {

	Date bidPlanProjectDateIssue;
	
	String bidPlanProjectCompanyIssue;
	
	Integer bidPlanProjectFund;

	@Indexed
	Date bidPlanProjectDateApprove;



	@Indexed()
	String bidNo;

	public Date getBidPlanProjectDateIssue() {
		return bidPlanProjectDateIssue;
	}

	public void setBidPlanProjectDateIssue(final Date bidPlanProjectDateIssue) {
		this.bidPlanProjectDateIssue = bidPlanProjectDateIssue;
	}

	

	public String getBidPlanProjectCompanyIssue() {
		return bidPlanProjectCompanyIssue;
	}

	public void setBidPlanProjectCompanyIssue(final String bidPlanProjectCompanyIssue) {
		this.bidPlanProjectCompanyIssue = bidPlanProjectCompanyIssue;
	}

	

	public Integer getBidPlanProjectFund() {
		return bidPlanProjectFund;
	}

	public void setBidPlanProjectFund(final Integer bidPlanProjectFund) {
		this.bidPlanProjectFund = bidPlanProjectFund;
	}

	public Date getBidPlanProjectDateApprove() {
		return bidPlanProjectDateApprove;
	}

	public void setBidPlanProjectDateApprove(final Date bidPlanProjectDateApprove) {
		this.bidPlanProjectDateApprove = bidPlanProjectDateApprove;
	}



	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(final String bidNo) {
		this.bidNo = bidNo;
	}

	
}
