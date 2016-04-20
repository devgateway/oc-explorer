/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

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
	protected Integer pageNumber;

	@Range(min = 1, max = MAX_PAGE_SIZE)
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
