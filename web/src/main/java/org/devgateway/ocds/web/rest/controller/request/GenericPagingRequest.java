/**
 *
 */
package org.devgateway.ocds.web.rest.controller.request;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author mihai
 *
 */
public class GenericPagingRequest {

    public static final int DEFAULT_PAGE_SIZE = 100;

    public static final int MAX_PAGE_SIZE = 1000;

    public static final int MAX_REQ_YEAR = 2200;

    public static final int MIN_REQ_YEAR = 1900;

    @Min(0)
	@ApiModelProperty(value = "This is the page number to be displayed. "
			+ "If unspecified it starts with the first page which is page 0")
    protected Integer pageNumber;

    @Range(min = 1, max = MAX_PAGE_SIZE)
	@ApiModelProperty(value = "This defines how many elements to display on each page. It defaults to "
			+ DEFAULT_PAGE_SIZE + " .You can have a maximum of " + MAX_PAGE_SIZE + " elements on each page."
			+ "Larger values are not allowed because they can crash your browser.")
    protected Integer pageSize;

    public GenericPagingRequest() {
        pageNumber = 0;
        pageSize = DEFAULT_PAGE_SIZE;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Integer page) {
        this.pageNumber = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Integer size) {
        this.pageSize = size;
    }

    public Integer getSkip() {
        return pageNumber * pageSize;
    }
}
