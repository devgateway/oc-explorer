/**
 *
 */
package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.devgateway.ocds.web.annotate.OrganizationIdValidation;

import java.util.TreeSet;

/**
 * @author mpostelnicu
 *
 */
public class WinningTupleYearFilterPagingRequest extends YearFilterPagingRequest {

    @OrganizationIdValidation
    @ApiModelProperty(value = "This is a tuple of tenderers among which one was part of a winning award")
    private TreeSet<String> tendererTuple;

    public TreeSet<String> getTendererTuple() {
        return tendererTuple;
    }

    public void setTendererTuple(TreeSet<String> tendererTuple) {
        this.tendererTuple = tendererTuple;
    }
}
