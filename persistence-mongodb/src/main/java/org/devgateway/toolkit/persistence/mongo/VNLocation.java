/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class VNLocation {
	@Id
	String id;
	
	String name;
	
	
}
