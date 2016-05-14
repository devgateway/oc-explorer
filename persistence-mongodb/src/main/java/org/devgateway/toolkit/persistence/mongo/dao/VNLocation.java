/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document(collection = "location")
public class VNLocation extends Location<GeoJsonPoint> {

	public static final String GEONAMES_URI_PREFIX = "http://www.geonames.org/";
	public static final String GEONAMES_SCHEME = "GEONAMES";

	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	private GeoJsonPoint geometry;

	public VNLocation() {
		super();
		this.getGazetteer().setScheme(GEONAMES_SCHEME);
	}

	@Override
	public GeoJsonPoint getGeometry() {
		return geometry;
	}

	@Override
	public void setGeometry(GeoJsonPoint geometry) {
		this.geometry = geometry;
	}

	@Override
	public String getGazetteerPrefix() {
		return GEONAMES_URI_PREFIX;
	}
}
