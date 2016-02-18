/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import cz.jirutka.validator.collection.constraints.EachPattern;
import cz.jirutka.validator.collection.constraints.EachRange;

/**
 * @author mihai
 * Filtering bean applied to all endpoints
 */
public class UniversalFilterPagingRequest extends GenericPagingRequest {

	@EachRange(min = 1900, max = 2200)
	List<Integer> year;

	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	List<String> bidTypeId;

	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	List<String> procuringEntityId;
	
	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	List<String> bidSelectionMethod;

	public UniversalFilterPagingRequest() {
		super();
	}

	public List<Integer> getYear() {
		return year;
	}

	public void setYear(List<Integer> year) {
		this.year = year;
	}

	public List<String> getBidTypeId() {
		return bidTypeId;
	}

	public void setBidTypeId(List<String> bidTypeId) {
		this.bidTypeId = bidTypeId;
	}

	public List<String> getProcuringEntityId() {
		return procuringEntityId;
	}

	public void setProcuringEntityId(List<String> procuringEntityId) {
		this.procuringEntityId = procuringEntityId;
	}

	public List<String> getBidSelectionMethod() {
		return bidSelectionMethod;
	}

	public void setBidSelectionMethod(List<String> bidSelectionMethod) {
		this.bidSelectionMethod = bidSelectionMethod;
	}

	
	
}
