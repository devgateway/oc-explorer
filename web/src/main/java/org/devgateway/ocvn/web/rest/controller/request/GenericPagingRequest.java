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

	@Min(0)
	Integer pageNumber;

	@Range(min = 1, max = 1000)
	Integer pageSize;

	public GenericPagingRequest() {
		pageNumber = 0;
		pageSize = 100;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer page) {
		this.pageNumber = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer size) {
		this.pageSize = size;
	}
}
