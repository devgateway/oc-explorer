/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

import javax.validation.constraints.Size;

/**
 * @author mihai
 *
 */
public class ProcuringEntitySearchRequest extends GenericPagingRequest {

	@Size(min=3, max=30)	
	String text;
	
	public ProcuringEntitySearchRequest() {
		super();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}
