package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.toolkit.persistence.mongo.dao.Location;
import org.devgateway.toolkit.persistence.mongo.repository.LocationRepository;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class LocationRowImporter extends RowImporter<Location, LocationRepository> {

	public LocationRowImporter(LocationRepository locationRepository, int skipRows) {
		super(locationRepository, skipRows);
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		Location location = repository.findByName(row[0]);
		if (location != null)
			throw new RuntimeException("Duplicate location name " + row[0]);

		location = new Location();
		documents.add(location);

		location.setName(row[0]);

		GeoJsonPoint coordinates = new GeoJsonPoint(getDouble(row[2]), getDouble(row[1]));
		location.setCoordinates(coordinates);
		location.setId(row[3]);

		return true;
	}
}
