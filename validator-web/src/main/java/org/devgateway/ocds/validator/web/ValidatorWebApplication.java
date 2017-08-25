package org.devgateway.ocds.validator.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by mpostelnicu on 7/4/17.
 */
@SpringBootApplication
@PropertySource("classpath:/org/devgateway/ocds/validator/web/application.properties")
@ComponentScan("org.devgateway.ocds.validator")
public class ValidatorWebApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ValidatorWebApplication.class, args);
    }
}
