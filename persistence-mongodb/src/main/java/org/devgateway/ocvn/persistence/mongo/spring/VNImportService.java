/**
 *
 */
package org.devgateway.ocvn.persistence.mongo.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.reader.RowImporter;
import org.devgateway.ocds.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.ocds.persistence.mongo.repository.OrganizationRepository;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.ExcelImportService;
import org.devgateway.ocds.persistence.mongo.spring.OcdsSchemaValidation;
import org.devgateway.ocvn.persistence.mongo.dao.ImportFileTypes;
import org.devgateway.ocvn.persistence.mongo.reader.BidPlansRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.EBidAwardRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.LocationRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.OfflineAwardRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.ProcurementPlansRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.PublicInstitutionRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.SupplierRowImporter;
import org.devgateway.ocvn.persistence.mongo.reader.TenderRowImporter;
import org.devgateway.ocvn.persistence.mongo.repository.ContrMethodRepository;
import org.devgateway.ocvn.persistence.mongo.repository.VNLocationRepository;
import org.devgateway.toolkit.persistence.mongo.reader.XExcelFileReader;
import org.devgateway.toolkit.persistence.mongo.spring.MongoTemplateConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

/**
 * @author mihai Service that imports Excel sheets from given import file in
 *         Vietnam input data format
 */
@Service
@Transactional
public class VNImportService implements ExcelImportService {

    private static final int MS_IN_SECOND = 1000;

    private static final int LOG_IMPORT_EVERY = 10000;

    private static final int VALIDATION_BATCH = 5000;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private ContrMethodRepository contrMethodRepository;

    @Autowired
    private VNLocationRepository locationRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoTemplateConfiguration mongoTemplateConfiguration;

    @Autowired
    private OcdsSchemaValidation validationService;
    
	@Autowired(required = false)
	private CacheManager cacheManager;

    private StringBuffer msgBuffer = new StringBuffer();

    private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

    public static final String LOCATIONS_FILE_NAME = "locations";
    public static final String ORGS_FILE_NAME = "orgs";
    public static final String DATABASE_FILE_NAME = "database";

    // TODO: remove these
    // URL prototypeDatabaseFile =
    // getClass().getResource("/Prototype_Database_OCDSCore.xlsx");
    // URL organizationFile =
    // getClass().getResource("/UM_PUBINSTITU_SUPPLIERS_DQA.xlsx");
    // URL locationFile = getClass().getResource("/Location_Table_SO.xlsx");

    private void importSheet(final URL fileUrl, final String sheetName, final RowImporter<?, ?> importer)
            throws Exception {
        importSheet(fileUrl, sheetName, importer, MongoConstants.IMPORT_ROW_BATCH);
    }

    /**
     * Delete all data without dropping indexes
     */
    private void purgeDatabase() {
        logMessage("Purging database...");

        ScriptOperations scriptOps = mongoTemplate.scriptOps();
        ExecutableMongoScript echoScript = new ExecutableMongoScript("db.dropDatabase()");
        scriptOps.execute(echoScript);

        logMessage("Database purged.");

        // //recreate inline indexes
        // mongoTemplate.setApplicationContext(applicationContext);
        //

        // create indexes that affect import performance
        mongoTemplateConfiguration.createMandatoryImportIndexes();

    }

    /**
     * This is invoked if the database has been purged
     */
    private void postImportStage() {
        // post-init indexes
        mongoTemplateConfiguration.createPostImportStructures();
    }

    /**
     * Log the message to logger but also to a stringbuffer to display online if
     * needed
     *
     * @param message
     */
    public void logMessage(final String message) {
        logger.info(message);
        msgBuffer.append(message).append("\r\n");
    }

    private void importSheet(final URL fileUrl, final String sheetName, final RowImporter<?, ?> importer,
                             final int importRowBatch) {
        logMessage("<b>Importing " + sheetName + " using " + importer.getClass().getSimpleName() + "</b>");

        XExcelFileReader reader = null;
        try {
            reader = new XExcelFileReader(fileUrl.getFile(), sheetName);

            List<String[]> rows;
            long startTime = System.currentTimeMillis();
            long rowNo = 0;
            rows = reader.readRows(importRowBatch);
            while (!rows.isEmpty()) {
                importer.importRows(rows);
                rowNo += importRowBatch;
                if (rowNo % LOG_IMPORT_EVERY == 0) {
                    logMessage("Import Speed " + rowNo * MS_IN_SECOND / (System.currentTimeMillis() - startTime)
                            + " rows per second.");
                }
                rows = reader.readRows(importRowBatch);
            }

        } catch (Exception e) {
            logMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    
	/**
	 * Simple method that gets all cache names and invokes {@link Cache#clear()}
	 * on all
	 */
	public void clearAllCaches() {
		if (cacheManager != null) {
			cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
		}
	}
    
    /**
     * Extracts the files from the given {@link VietnamImportSourceFiles}
     * object, creates a temp dir and drops them there.
     *
     * @param files
     * @return the path of the temp dir created, that contains the files save
     *         from {@link VietnamImportSourceFiles}
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String saveSourceFilesToTempDir(final byte[] prototypeDatabase, final byte[] locations,
                                            final byte[] publicInstitutionsSuppliers)
            throws FileNotFoundException, IOException {
        File tempDir = Files.createTempDir();
        FileOutputStream prototypeDatabaseOutputStream = new FileOutputStream(new File(tempDir, DATABASE_FILE_NAME));
        prototypeDatabaseOutputStream.write(prototypeDatabase);
        prototypeDatabaseOutputStream.close();

        FileOutputStream locationsOutputStream = new FileOutputStream(new File(tempDir, LOCATIONS_FILE_NAME));
        locationsOutputStream.write(locations);
        locationsOutputStream.close();

        FileOutputStream publicInstitutionsSuppliersOutputStream = new FileOutputStream(
                new File(tempDir, ORGS_FILE_NAME));
        publicInstitutionsSuppliersOutputStream.write(publicInstitutionsSuppliers);
        publicInstitutionsSuppliersOutputStream.close();

        return tempDir.toURI().toURL().toString();
    }

    @Async
    public void importAllSheets(final List<String> fileTypes, final byte[] prototypeDatabase, final byte[] locations,
                                final byte[] publicInstitutionsSuppliers,
                                final Boolean purgeDatabase, final Boolean validateData)
            throws InterruptedException {

        String tempDirPath = null;
        
        clearAllCaches();
        
        try {
            newMsgBuffer();
            if (purgeDatabase) {
                purgeDatabase();
            }

            tempDirPath = saveSourceFilesToTempDir(prototypeDatabase, locations, publicInstitutionsSuppliers);

            if (fileTypes.contains(ImportFileTypes.LOCATIONS)) {
                importSheet(new URL(tempDirPath + LOCATIONS_FILE_NAME), "Sheet1",
                        new LocationRowImporter(locationRepository, this, 1), 1);
            }

            if (fileTypes.contains(ImportFileTypes.PUBLIC_INSTITUTIONS)) {
                importSheet(new URL(tempDirPath + ORGS_FILE_NAME), "UM_PUB_INSTITU_MAST",
                        new PublicInstitutionRowImporter(organizationRepository, this, 2), 1);
            }

            if (fileTypes.contains(ImportFileTypes.SUPPLIERS)) {
                importSheet(new URL(tempDirPath + ORGS_FILE_NAME), "UM_SUPPLIER_ENTER_MAST",
                        new SupplierRowImporter(organizationRepository, this, 2), 1);
            }

            if (fileTypes.contains(ImportFileTypes.PROCUREMENT_PLANS)) {
                importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "ProcurementPlans",
                        new ProcurementPlansRowImporter(releaseRepository, this, locationRepository, 1));
            }

            if (fileTypes.contains(ImportFileTypes.BID_PLANS)) {
                importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "BidPlans",
                        new BidPlansRowImporter(releaseRepository, this, 1));
            }

            if (fileTypes.contains(ImportFileTypes.TENDERS)) {
                importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "Tender",
                        new TenderRowImporter(releaseRepository, this, organizationRepository, classificationRepository,
                                contrMethodRepository, locationRepository, 1));
            }

            if (fileTypes.contains(ImportFileTypes.EBID_AWARDS)) {
                importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "eBid_Awards",
                        new EBidAwardRowImporter(releaseRepository, this, organizationRepository, 1));
            }

            if (fileTypes.contains(ImportFileTypes.OFFLINE_AWARDS)) {
                importSheet(new URL(tempDirPath + DATABASE_FILE_NAME), "Offline_Awards",
                        new OfflineAwardRowImporter(releaseRepository, this, organizationRepository, 1));
            }

            if (purgeDatabase) {
                postImportStage();
            }

            if (validateData) {
                validateData();
            }

            logMessage("<b>IMPORT PROCESS COMPLETED.</b>");

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (tempDirPath != null) {
                try {
                    FileUtils.deleteDirectory(Paths.get(new URL(tempDirPath).toURI()).toFile());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void validateData() {

        logMessage("<b>RUNNING SCHEMA VALIDATION.</b>");

        int pageNumber = 0;
        int processedCount = 0;

        Page<Release> page;
        do {
            page = releaseRepository.findAll(new PageRequest(pageNumber++, VALIDATION_BATCH));
            page.getContent().parallelStream().map(rel -> validationService.validate(rel))
                    .filter(r -> !r.getReport().isSuccess()).forEach(r -> logMessage(
                    "<font style='color:red'>OCDS Validation Failed: " + r.toString() + "</font>"));
            processedCount += page.getNumberOfElements();
            logMessage("Validated " + processedCount + " releases");
        } while (!page.isLast());

        logMessage("<b>SCHEMA VALIDATION COMPLETE.</b>");
    }

    public StringBuffer getMsgBuffer() {
        return msgBuffer;
    }

    public void newMsgBuffer() {
        msgBuffer = new StringBuffer();
    }

    public OcdsSchemaValidation getValidationService() {
        return validationService;
    }

}
