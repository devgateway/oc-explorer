package org.devgateway.toolkit.persistence.spring;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Octavian Ciubotaru
 */
public class SpringLiquibaseRunner implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SpringLiquibaseRunner.class);

    private final SpringLiquibase springLiquibase;

    public SpringLiquibaseRunner(final SpringLiquibase springLiquibase) {
        this.springLiquibase = springLiquibase;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Attempting to run liquibase second time");
        springLiquibase.afterPropertiesSet();
    }

    @Override
    public String toString() {
        return getClass().getName() + " (" + springLiquibase + ")";
    }
}