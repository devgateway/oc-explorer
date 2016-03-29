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
	
	/**
	 * This parameter will invert (negate) all existing filtering parameters. So
	 * A IN B turns into A NOT IN B. A IN B AND AN IN C turns into A NOT IN B
	 * AND A NOT IN C. So this is NOT exactly a logical *not*, the correct way
	 * would be !(A && B) = !A || !B. Which is not what we do here, but we
	 * actually dont use multiple parameters anywhere, so it should not matter
	 * now
	 */
	Boolean invert=false;
	

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


	public Boolean getInvert() {
		return invert;
	}


	public void setInvert(Boolean invert) {
		this.invert = invert;
	}

}
