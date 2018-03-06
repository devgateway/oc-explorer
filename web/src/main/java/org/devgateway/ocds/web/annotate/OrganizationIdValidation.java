package org.devgateway.ocds.web.annotate;

import cz.jirutka.validator.collection.constraints.EachPattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mpostelnicu
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@EachPattern(regexp = "^[a-zA-Z0-9\\-]*$", message = "Invalid organization id!")
public @interface OrganizationIdValidation {
}
