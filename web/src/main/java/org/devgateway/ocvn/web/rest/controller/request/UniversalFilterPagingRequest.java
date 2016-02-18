/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import java.util.List;

import cz.jirutka.validator.collection.constraints.EachRange;

/**
 * @author mihai
 *
 */
public class UniversalFilterPagingRequest extends GenericPagingRequest {


	@EachRange(min=1900,max=2200)
	List<Integer> year;
	
	
	
	public UniversalFilterPagingRequest() {
		super();
	}

}
