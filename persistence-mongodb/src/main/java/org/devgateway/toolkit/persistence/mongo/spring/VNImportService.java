/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.mongo.dao.DBConstants;
import org.devgateway.toolkit.persistence.mongo.dao.ImportFileTypes;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

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

	@Autowired
	private MongoTemplate mongoTemplate;

	private StringBuffer msgBuffer = new StringBuffer();

	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	public static final String LOCATIONS_FILE_NAME = "locations";
	public static final String ORGS_FILE_NAME = "orgs";
	public static final String DATABASE_FILE_NAME = "database";

	URL prototypeDatabaseFile = getClass().getResource("/Prototype_Database_OCDSCore.xlsx");
	URL organizationFile = getClass().getResource("/UM_PUBINSTITU_SUPPLIERS_DQA.xlsx");
	URL locationFile = getClass().getResource("/Location_Table_SO.xlsx");

	private void importSheet(URL fileUrl, String sheetName, RowImporter<?, ?> importer) throws Exception {
		importSheet(fileUrl, sheetName, importer, DBConstants.IMPORT_ROW_BATCH);
	}

	/**
	 * Delete all data without dropping indexes
	 */
	private void purgeDatabase() {
		logMessage("Purging database...");
		ScriptOperations scriptOps = mongoTemplate.scriptOps();
		ExecutableMongoScript echoScript = new ExecutableMongoScript(
				"db.release.remove({});db.location.remove({});db.organization.remove({});");
		scriptOps.execute(echoScript);
		logMessage("Database purged.");
	}

	/**
	 * Log the message to logger but also to a stringbuffer to display online if
	 * needed
	 * 
	 * @param message
	 */
	public void logMessage(String message) {
		logger.info(message);
		msgBuffer.append(message).append("\r\n");
	}

	private void importSheet(URL fileUrl, String sheetName, RowImporter<?, ?> importer, int importRowBatch)
			throws Exception {
		logMessage("Importing " + sheetName + " using " + importer.getClass().getSimpleName());

		XExcelFileReader reader = new XExcelFileReader(fileUrl.getFile(), sheetName);

		List<String[]> rows = null;
		long startTime = System.currentTimeMillis();
		long rowNo = 0;
		while (!(rows = reader.readRows(importRowBatch)).isEmpty()) {
			importer.importRows(rows);
			rowNo += importRowBatch;
			if (rowNo % 10000 == 0)
				logMessage("Import Speed " + rowNo * 1000 / (System.currentTimeMillis() - startTime)
						+ " rows per second.");
		}
	}

	private String saveSourceFilesToTempDir(VietnamImportSourceFiles files) throws FileNotFoundException, IOException {
		File tempDir = Files.createTempDir();
		Set<FileMetadata> prototypeDatabaseFile = files.getPrototypeDatabaseFile();
		FileOutputStream prototypeDatabaseOutputStream = new FileOutputStream(new File(tempDir, DATABASE_FILE_NAME));
		prototypeDatabaseOutputStream.write(prototypeDatabaseFile.iterator().next().getContent().getBytes());
		prototypeDatabaseOutputStream.close();

		Set<FileMetadata> locationsFile = files.getLocationsFile();
		FileOutputStream locationsOutputStream = new FileOutputStream(new File(tempDir, LOCATIONS_FILE_NAME));
		locationsOutputStream.write(locationsFile.iterator().next().getContent().getBytes());
		locationsOutputStream.close();

		Set<FileMetadata> publicInstitutionsSuppliersFile = files.getPublicInstitutionsSuppliersFile();
		FileOutputStream publicInstitutionsSuppliersOutputStream = new FileOutputStream(
				new File(tempDir, ORGS_FILE_NAME));
		publicInstitutionsSuppliersOutputStream
				.write(publicInstitutionsSuppliersFile.iterator().next().getContent().getBytes());
		publicInstitutionsSuppliersOutputStream.close();

		return tempDir.toURI().toURL().toString();
	}


	public void importAllSheets(List<String> fileTypes, VietnamImportSourceFiles files, Boolean purgeDatabase) throws InterruptedException  {

		String tempDirPath = null;
		try {
			newMsgBuffer();
			if (purgeDatabase)
				purgeDatabase();

			tempDirPath = saveSourceFilesToTempDir(files);

			if (fileTypes.contains(ImportFileTypes.LOCATIONS))
				importSheet(new URL(tempDirPath + LOCATIONS_FILE_NAME), "Sheet1",
						new LocationRowImporter(locationRepository,this, 1), 1);

			if (fileTypes.contains(ImportFileTypes.PUBLIC_INSTITUTIONS))
				importSheet(new URL(tempDirPath + ORGS_FILE_NAME), "UM_PUB_INSTITU_MAST",
						new PublicInstitutionRowImporter(organizationRepository,this, 2), 1);

			if (fileTypes.contains(ImportFileTypes.SUPPLIERS))
				importSheet(new URL(tempDirPath + ORGS_FILE_NAME), "UM_SUPPLIER_ENTER_MAST",
						new SupplierRowImporter(organizationRepository,this, 2), 1);

			if (fileTypes.contains(ImportFileTypes.PROCUREMENT_PLANS))
				importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "ProcurementPlans",
						new ProcurementPlansRowImporter(releaseRepository, this,locationRepository, 2));

			if (fileTypes.contains(ImportFileTypes.BID_PLANS))
				importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "BidPlans",
						new BidPlansRowImporter(releaseRepository, this, 2));

			if (fileTypes.contains(ImportFileTypes.TENDERS))
				importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "Tender",
						new TenderRowImporter(releaseRepository, this,organizationRepository, classificationRepository, 2));

			if (fileTypes.contains(ImportFileTypes.EBID_AWARDS))
				importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "eBid_Award",
						new EBidAwardRowImporter(releaseRepository, this, organizationRepository, 2));

			if (fileTypes.contains(ImportFileTypes.OFFLINE_AWARDS))
				importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "Offline_Award",
						new OfflineAwardRowImporter(releaseRepository, this, organizationRepository, 2));

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (tempDirPath != null)
				try {
					FileUtils.deleteDirectory(new File(tempDirPath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public StringBuffer getMsgBuffer() {
		return msgBuffer;
	}
	
	public void newMsgBuffer() {
		msgBuffer = new StringBuffer();
	}

}
