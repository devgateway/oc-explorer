package org.devgateway.toolkit.persistence.mongo.test;

import org.devgateway.toolkit.persistence.mongo.spring.MongoPersistenceApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MongoPersistenceApplication.class })
@ActiveProfiles("integration")
@TestPropertySource("classpath:test.properties")
public abstract class AbstractMongoTest {
	
	protected boolean testDataInitialized = false;

}
