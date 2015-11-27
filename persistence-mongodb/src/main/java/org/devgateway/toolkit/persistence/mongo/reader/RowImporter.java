package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.toolkit.persistence.mongo.dao.DBConstants;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RowImporter {

	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	ReleaseRepository releaseRepository;
	DecimalFormat df;
	int skipRows;
	int impRowNo = 1;
	List<Release> releases;

	public RowImporter(ReleaseRepository releaseRepository, int skipRows) {
		this.releaseRepository = releaseRepository;
		df = new DecimalFormat();
		df.setParseBigDecimal(true);
		releases = new ArrayList<>(DBConstants.IMPORT_ROW_BATCH);
		this.skipRows = skipRows;
	}

	public boolean importRows(List<String[]> rows) throws ParseException {
		releases.clear();

		for (String[] row : rows) {
			if (impRowNo++ < skipRows)
				continue;

			try {
				importRow(row);
			} catch (Exception e) {
				logger.error("Error importing row " + impRowNo,e);			
				//throw e; we do not stop
			}
		}

		releaseRepository.save(releases);
		logger.info("Imported " + impRowNo);
		return true;
	}

	public abstract boolean importRow(String[] row) throws ParseException;

}
