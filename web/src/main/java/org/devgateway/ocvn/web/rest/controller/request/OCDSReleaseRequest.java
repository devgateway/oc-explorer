package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;

import cz.jirutka.validator.collection.constraints.EachRange;

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

	@EachRange(min=1900,max=2200)
	List<Integer> bidPlanProjectDateApproveYear;

	public List<Integer> getBidPlanProjectDateApproveYear() {
		return bidPlanProjectDateApproveYear;
	}

	public void setBidPlanProjectDateApproveYear(List<Integer> bidPlanProjectDateApproveYear) {
		this.bidPlanProjectDateApproveYear = bidPlanProjectDateApproveYear;
	}




}