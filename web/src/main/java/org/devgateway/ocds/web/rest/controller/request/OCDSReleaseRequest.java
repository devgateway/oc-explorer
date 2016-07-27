package org.devgateway.ocds.web.rest.controller.request;

import cz.jirutka.validator.collection.constraints.EachRange;

import java.util.List;

/**
 *
 * @author mihai * bidPlanProjectDateApproveYear a multiparameter , the years
 *         that will filter {@link VNPlanning#getBidPlanProjectDateApprove()}
 *         pageNumber the pageNumber number, zero indexed pageSize the
 *         pageNumber pageSize, should be between 1 and 1000
 *
 */
public class OCDSReleaseRequest extends GenericPagingRequest {

    public OCDSReleaseRequest() {
        super();
    }

    @EachRange(min = MIN_REQ_YEAR, max = MAX_REQ_YEAR)
    protected List<Integer> bidPlanProjectDateApproveYear;

    public List<Integer> getBidPlanProjectDateApproveYear() {
        return bidPlanProjectDateApproveYear;
    }

    public void setBidPlanProjectDateApproveYear(final List<Integer> bidPlanProjectDateApproveYear) {
        this.bidPlanProjectDateApproveYear = bidPlanProjectDateApproveYear;
    }

}