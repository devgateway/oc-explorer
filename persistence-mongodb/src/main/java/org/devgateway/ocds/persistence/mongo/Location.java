/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;

public abstract class Location<T extends GeoJson<?>> implements Identifiable {

	@Id
	private String id;
	
	private Gazetteer gazetteer = new Gazetteer();

	private String description;

	private String uri;

	
	@Override
	public Serializable getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}


	public void setupGazetteer(String id) {
		getGazetteer().getIdentifiers().add(id);
		uri = getGazetteerPrefix() + id;
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

	public Gazetteer getGazetteer() {
		return gazetteer;
	}

	public void setGazetteer(Gazetteer gazetteer) {
		this.gazetteer = gazetteer;
	}

	public abstract T getGeometry();

	public abstract void setGeometry(T geometry);

	public abstract String getGazetteerPrefix();
	
	
	
}
