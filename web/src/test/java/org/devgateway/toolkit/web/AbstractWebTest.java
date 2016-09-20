package org.devgateway.toolkit.web;

import liquibase.servicelocator.LiquibaseService;
import org.apache.log4j.Logger;
import org.devgateway.toolkit.web.spring.WebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//@TestConfiguration
@RunWith(SpringRunner.class)
// @ActiveProfiles("integration")
// @LiquibaseService(skip=true)
@SpringBootTest(classes = {WebApplication.class},
        webEnvironment=WebEnvironment.RANDOM_PORT,
        properties={"classpath:test.properties"})
public class AbstractWebTest {
    protected static Logger logger = Logger.getLogger(AbstractWebTest.class);

    @Test
    public void test() {
        logger.error(">>>>");
    }
}
