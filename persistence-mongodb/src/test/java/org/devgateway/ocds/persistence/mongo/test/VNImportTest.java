/**
 * 
 */
package org.devgateway.ocds.persistence.mongo.test;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.devgateway.ocds.persistence.mongo.spring.ExcelImportService;
import org.devgateway.ocvn.persistence.mongo.dao.ImportFileTypes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mihai
 *
 */
public class VNImportTest extends AbstractMongoTest {

	@Autowired
	private ExcelImportService vnExcelImportService;

	public byte[] loadResourceStreamAsByteArray(String name) throws IOException {
		return IOUtils.toByteArray(getClass().getResourceAsStream(name));
	}

	@Test
	public void testImport() throws IOException, InterruptedException {

		vnExcelImportService.importAllSheets(ImportFileTypes.ALL_FILE_TYPES,
				loadResourceStreamAsByteArray("/testImport/test_OCVN_June12016_Update_Template.xlsx"),
				loadResourceStreamAsByteArray("/testImport/test_Location_Table_Geocoded.xlsx"),
				loadResourceStreamAsByteArray("/testImport/test_UM_PUBINSTITU_SUPPLIERS_DQA.xlsx"), true, false);
	}

}
