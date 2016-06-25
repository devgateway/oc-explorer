package org.devgateway.ocds.web.rest.controller.test;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.devgateway.ocds.persistence.mongo.spring.ExcelImportService;
import org.devgateway.ocds.web.rest.controller.AverageNumberOfTenderersController;
import org.devgateway.ocds.web.rest.controller.CostEffectivenessVisualsController;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.GroupingFilterPagingRequest;
import org.devgateway.ocvn.persistence.mongo.dao.ImportFileTypes;
import org.devgateway.toolkit.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mongodb.DBObject;

/**
 * @author mihai
 *
 */
@WebAppConfiguration
public class VNImportAndEndpointsTest extends AbstractMongoTest {

	@Autowired
	private ExcelImportService vnExcelImportService;

	@Autowired
	private CostEffectivenessVisualsController costEffectivenessVisualsController;
	
	@Autowired	
	private AverageNumberOfTenderersController averageNumberOfTenderersController;

	private static boolean initialized = false;

	public byte[] loadResourceStreamAsByteArray(String name) throws IOException {
		return IOUtils.toByteArray(getClass().getResourceAsStream(name));
	}

	@Before
	public void importTestData() throws IOException, InterruptedException {

		if (initialized) {
			return;
		}
		vnExcelImportService.importAllSheets(ImportFileTypes.ALL_FILE_TYPES,
				loadResourceStreamAsByteArray("/testImport/test_egp_Jun21_Import.xlsx"),
				loadResourceStreamAsByteArray("/testImport/test_Location_Table_Geocoded.xlsx"),
				loadResourceStreamAsByteArray("/testImport/test_UM_PUBINSTITU_SUPPLIERS_DQA.xlsx"), true, false);
	}

	@Test
	public void testCostEffectivenessAwardAmount() {
		List<DBObject> costEffectivenessAwardAmount = costEffectivenessVisualsController
				.costEffectivenessAwardAmount(new DefaultFilterPagingRequest());
		DBObject root = costEffectivenessAwardAmount.get(0);
		int year = (int) root.get("_id");
		Assert.assertEquals(2012, year);

		double totalAwardAmount = (double) root.get("totalAwardAmount");
		Assert.assertEquals(2000, totalAwardAmount, 0);

	}
	
	@Test
	public void testCostEffectivenessTenderAmount() {
		List<DBObject> costEffectivenessTenderAmount = costEffectivenessVisualsController
				.costEffectivenessTenderAmount(new GroupingFilterPagingRequest());
		DBObject root = costEffectivenessTenderAmount.get(0);
		int year = (int) root.get("_id");
		Assert.assertEquals(2013, year);

		double totalAwardAmount = (double) root.get("totalTenderAmount");
		Assert.assertEquals(1500, totalAwardAmount, 0);

	}
	
	@Test
	public void testAverageNumberOfTenderersController() {
		List<DBObject> averageNumberOfTenderers = averageNumberOfTenderersController.
				averageNumberOfTenderers(new DefaultFilterPagingRequest());
				
		DBObject root = averageNumberOfTenderers.get(0);
		int year = (int) root.get("year");
		Assert.assertEquals(2012, year);

		double averageNoTenderers = (double) root.get("averageNoTenderers");
		Assert.assertEquals(2, averageNoTenderers, 0);
	}
	
	

}
