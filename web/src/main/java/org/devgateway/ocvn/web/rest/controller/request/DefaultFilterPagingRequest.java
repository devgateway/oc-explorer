/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import cz.jirutka.validator.collection.constraints.EachPattern;

/**
 * @author mihai
 * Filtering bean applied to all endpoints
 */
public class DefaultFilterPagingRequest extends GenericPagingRequest {



	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	List<String> bidTypeId;

	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	List<String> procuringEntityId;
	
	List<String> bidSelectionMethod;
	
	

	public DefaultFilterPagingRequest() {
		super();
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
