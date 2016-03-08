/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.net.URL;
import java.util.List;

import org.devgateway.toolkit.persistence.mongo.dao.DBConstants;
import org.devgateway.toolkit.persistence.mongo.reader.BidPlansRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.EBidAwardRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.LocationRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.OfflineAwardRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.ProcurementPlansRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.PublicInstitutionRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.RowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.SupplierRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.TenderRowImporter;
import org.devgateway.toolkit.persistence.mongo.reader.XExcelFileReader;
import org.devgateway.toolkit.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.LocationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;
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
	
	@Autowired
	private VNOrganizationRepository organizationRepository;
	
	@Autowired
	private ClassificationRepository classificationRepository;

	@Autowired
	private LocationRepository locationRepository;
		
	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	URL prototypeDatabaseFile = getClass().getResource("/Prototype_Database_OCDSCore.xlsx");
	URL organizationFile = getClass().getResource("/UM_PUBINSTITU_SUPPLIERS_DQA.xlsx");
	URL locationFile = getClass().getResource("/Location_Table_SO.xlsx");

	
	private void importSheet(URL fileUrl, String sheetName, RowImporter<?,?> importer) throws Exception {
		logger.info("Importing " + sheetName + " using " + importer.getClass().getSimpleName());

		XExcelFileReader reader = new XExcelFileReader(fileUrl.getFile(), sheetName);

		List<String[]> rows = null;
		long startTime=System.currentTimeMillis();
		long rowNo=0;
		while (!(rows = reader.readRows(DBConstants.IMPORT_ROW_BATCH)).isEmpty()) {
			importer.importRows(rows);
			rowNo+=DBConstants.IMPORT_ROW_BATCH;
			if(rowNo%5000 ==0 ) 
				logger.info("Import Speed "+ rowNo*1000/(System.currentTimeMillis()-startTime)+" rows per second.");
		}
	}

	public void importAllSheets() throws Exception {
		importSheet(locationFile, "Sheet1", new LocationRowImporter(locationRepository, 1));
		importSheet(organizationFile, "UM_PUB_INSTITU_MAST", new PublicInstitutionRowImporter(organizationRepository, 2));
		importSheet(organizationFile, "UM_SUPPLIER_ENTER_MAST", new SupplierRowImporter(organizationRepository, 2));
		importSheet(prototypeDatabaseFile, "ProcurementPlans", new ProcurementPlansRowImporter(releaseRepository,locationRepository, 2));
		importSheet(prototypeDatabaseFile, "BidPlans", new BidPlansRowImporter(releaseRepository, 2));
		importSheet(prototypeDatabaseFile, "Tender", new TenderRowImporter(releaseRepository,organizationRepository,classificationRepository, 2));
		importSheet(prototypeDatabaseFile, "eBid_Award", new EBidAwardRowImporter(releaseRepository,organizationRepository, 2));
		importSheet(prototypeDatabaseFile, "Offline_Award", new OfflineAwardRowImporter(releaseRepository,organizationRepository, 2));
	}

}
