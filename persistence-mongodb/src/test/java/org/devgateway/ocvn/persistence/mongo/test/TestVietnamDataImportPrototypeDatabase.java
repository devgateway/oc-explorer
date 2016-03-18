/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.test;

import org.devgateway.toolkit.persistence.mongo.spring.MongoPersistenceApplication;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;
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
	private VNImportService importService;

	@Test
	public void testImport() throws Exception {

	//	importService.importAllSheets();
	}
}
