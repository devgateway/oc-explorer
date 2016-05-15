package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Location;
import org.devgateway.ocds.persistence.mongo.spring.VNImportService;
import org.devgateway.ocvn.persistence.mongo.dao.VNLocation;
import org.devgateway.ocvn.persistence.mongo.repository.VNLocationRepository;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

/**
 * Specific {@link RowImporter} for {@link Location}, in the custom Excel format
 * provided by Vietnam
 * 
 * @author mihai
 * @see Location
 */
public class LocationRowImporter extends RowImporter<VNLocation, VNLocationRepository> {

	public LocationRowImporter(final VNLocationRepository locationRepository, final VNImportService importService,
			final int skipRows) {
		super(locationRepository, importService, skipRows);
	}

	@Override
	public boolean importRow(final String[] row) throws ParseException {

		VNLocation location = repository.findByDescription(row[0]);
		if (location != null) {
			throw new RuntimeException("Duplicate location name " + row[0]);
		}

		location = new VNLocation();
		documents.add(location);

		location.setDescription(row[0]);

		GeoJsonPoint coordinates = new GeoJsonPoint(getDouble(row[2]), getDouble(row[1]));
		location.setGeometry(coordinates);
		location.setupGazetteer(row[3]);
		

		return true;
	}
}
