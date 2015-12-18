/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document
public class Location {

	@Id
	String id;

	@Indexed
	String name;

	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	GeoJsonPoint coordinates;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoJsonPoint getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GeoJsonPoint coordinates) {
		this.coordinates = coordinates;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
