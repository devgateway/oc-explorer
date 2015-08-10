package org.devgateway.toolkit.web.rest.entity;


/**
 * 
 * @author mihai
 *
 */
public class Dummy {

	private final long id;
	private final String content;

	public Dummy(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}