package org.devgateway.toolkit.persistence.test;

import org.devgateway.toolkit.persistence.spring.PersistenceApplication;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 */

/**
 * @author mpostelnicu
 *
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceApplication.class })
@ActiveProfiles("integration")
public abstract class AbstractPersistenceTest {

}
