/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.test;

import java.net.URL;
import java.util.List;

import org.devgateway.toolkit.persistence.mongo.reader.BidPlansImporter;
import org.devgateway.toolkit.persistence.mongo.reader.ProcurementPlansImporter;
import org.devgateway.toolkit.persistence.mongo.reader.XExcelFileReader;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.spring.MongoPersistenceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mihai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MongoPersistenceApplication.class)
public class TestVietnamDataImportPrototypeDatabase {

	@Autowired
	ReleaseRepository releaseRepository;

	@Test
	public void testImportProcurmentPlans() throws Exception {
		URL fis = getClass().getResource("/Prototype_Database_OCDSCore.xlsx");

		XExcelFileReader reader = new XExcelFileReader(fis.getFile(), "ProcurementPlans");

		List<String[]> rows = null;
		ProcurementPlansImporter ppi = new ProcurementPlansImporter(releaseRepository, 3);
		while (!(rows = reader.readRows(1000)).isEmpty()) {
			ppi.importRows(rows);
		}

	}

	@Test
	public void testImportBidPlans() throws Exception {
		URL fis = getClass().getResource("/Prototype_Database_OCDSCore.xlsx");

		XExcelFileReader reader = new XExcelFileReader(fis.getFile(), "BidPlans");

		List<String[]> rows = null;
		BidPlansImporter i = new BidPlansImporter(releaseRepository, 3);
		while (!(rows = reader.readRows(1000)).isEmpty()) {
			i.importRows(rows);
		}

	}
}
