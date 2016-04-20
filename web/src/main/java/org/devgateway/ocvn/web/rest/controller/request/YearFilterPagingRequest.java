/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import cz.jirutka.validator.collection.constraints.EachRange;

/**
 * @author mpostelnicu
 *
 */
public class YearFilterPagingRequest extends DefaultFilterPagingRequest {

	@EachRange(min = MIN_REQ_YEAR, max = MAX_REQ_YEAR)
	protected List<Integer> year;

	/**
	 * 
	 */
	public YearFilterPagingRequest() {
		super();
	}

	public List<Integer> getYear() {
		return year;
	}

	public void setYear(final List<Integer> year) {
		this.year = year;
	}

}
