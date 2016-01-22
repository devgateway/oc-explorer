package org.devgateway.ocvn.web.rest.controller.request;

import javax.validation.constraints.Min;

import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.hibernate.validator.constraints.Range;

/**
 * 
 * @author mihai * bidPlanProjectDateApproveYear a multiparameter , the years
 *         that will filter {@link VNPlanning#getBidPlanProjectDateApprove()}
 *         page the page number, zero indexed size the page size, should be
 *         between 1 and 1000
 * 
 */
public class OCDSReleaseRequest {

	public OCDSReleaseRequest() {
		page = 0;
		size = 100;
	}

	Integer[] bidPlanProjectDateApproveYear;

	@Min(0)
	Integer page;

	@Range(min = 1, max = 1000)
	Integer size;

	public Integer[] getBidPlanProjectDateApproveYear() {
		return bidPlanProjectDateApproveYear;
	}

	public void setBidPlanProjectDateApproveYear(Integer[] bidPlanProjectDateApproveYear) {
		this.bidPlanProjectDateApproveYear = bidPlanProjectDateApproveYear;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}