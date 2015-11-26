/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.net.URL;
import java.util.List;

import org.devgateway.toolkit.persistence.mongo.reader.BidPlansRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.ProcurementPlansRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.RowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.TenderRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.XExcelFileReader;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mihai Service that imports Excel sheets from given import file in
 *         Vietnam input data format
 */
@Service
public class VNImportService {

	@Autowired
	private ReleaseRepository releaseRepository;

	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	URL fis = getClass().getResource("/Prototype_Database_OCDSCore.xlsx");

	private void importSheet(String sheetName, RowImporter importer) throws Exception {
		logger.info("Importing " + sheetName + " using " + importer.getClass().getSimpleName());
		XExcelFileReader reader = new XExcelFileReader(fis.getFile(), sheetName);

		List<String[]> rows = null;
		while (!(rows = reader.readRows(1000)).isEmpty()) {
			importer.importRows(rows);
		}
	}

	public void importAllSheets() throws Exception {
		importSheet("ProcurementPlans", new ProcurementPlansRowImporter(releaseRepository, 3));
		importSheet("BidPlans", new BidPlansRowImporter(releaseRepository, 3));
		importSheet("Tender", new TenderRowImporter(releaseRepository, 3));
	}

}
