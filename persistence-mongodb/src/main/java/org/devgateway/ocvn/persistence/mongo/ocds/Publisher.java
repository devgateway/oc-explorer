package org.devgateway.ocvn.persistence.mongo.ocds;

/**
 * 
 * @author mihai
 * Publisher OCDS entity http://standard.open-contracting.org/latest/en/schema/reference/#publisher
 */
public class Publisher {

	String name;
	String scheme;
	String uid;
	String uri;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
