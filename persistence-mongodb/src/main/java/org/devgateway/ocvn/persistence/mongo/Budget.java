/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class Budget {

	String source;
	
	@Id
	String id;
	
	String description;
	
	Value amount;
	
	String project;
	
	String projectID;
	
	String uri;
	
	
}
