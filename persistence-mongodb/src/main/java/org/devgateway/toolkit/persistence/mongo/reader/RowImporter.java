package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;

public abstract class RowImporter {

	ReleaseRepository releaseRepository;
	DecimalFormat df;
	int skipRows;
	int impRowNo = 1;
	List<Release> releases;

	public RowImporter(ReleaseRepository releaseRepository, int skipRows) {
		this.releaseRepository = releaseRepository;
		df = new DecimalFormat();
		df.setParseBigDecimal(true);
		releases = new ArrayList<>(1001);
		this.skipRows = skipRows;
	}

	public boolean importRows(List<String[]> rows) throws ParseException {
		releases.clear();

		for (String[] row : rows) {
			if (impRowNo++ < skipRows)
				continue;

			importRow(row);
		}

		releaseRepository.save(releases);
		System.out.println("Imported " + impRowNo);
		return true;
	}

	public abstract boolean importRow(String[] row) throws ParseException;

}
