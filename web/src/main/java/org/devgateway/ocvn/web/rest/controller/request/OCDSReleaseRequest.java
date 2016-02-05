package org.devgateway.ocvn.web.rest.controller.request;

import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;

/**
 * 
 * @author mihai * bidPlanProjectDateApproveYear a multiparameter , the years
 *         that will filter {@link VNPlanning#getBidPlanProjectDateApprove()}
 *         pageNumber the pageNumber number, zero indexed pageSize the pageNumber pageSize, should be
 *         between 1 and 1000
 * 
 */
public class OCDSReleaseRequest extends GenericPagingRequest {

	public OCDSReleaseRequest() {
		super();
	}

	Integer[] bidPlanProjectDateApproveYear;


	public Integer[] getBidPlanProjectDateApproveYear() {
		return bidPlanProjectDateApproveYear;
	}

	public void setBidPlanProjectDateApproveYear(Integer[] bidPlanProjectDateApproveYear) {
		this.bidPlanProjectDateApproveYear = bidPlanProjectDateApproveYear;
	}



}