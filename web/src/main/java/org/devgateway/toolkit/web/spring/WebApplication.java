package org.devgateway.toolkit.web.spring;

import org.devgateway.toolkit.web.Base;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * @author mpostelnicu
 *
 */

@SpringBootApplication
@Import({ org.devgateway.toolkit.persistence.spring.PersistenceApplication.class })
@PropertySource("classpath:/org/devgateway/toolkit/web/application.properties")
@ComponentScan(basePackageClasses=Base.class)
public class WebApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}