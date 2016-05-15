/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.dao;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class ContrMethod {

	@Id
	private Integer id;

	private String details;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
