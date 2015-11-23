/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class Classification {

	String scheme;

	@Id
	String id;

	String description;

	String uri;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
