/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import cz.jirutka.validator.collection.constraints.EachPattern;
import cz.jirutka.validator.collection.constraints.EachRange;

/**
 * @author mihai
 *
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

}
