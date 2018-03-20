package org.devgateway.ocds.web.annotate;

import cz.jirutka.validator.collection.constraints.EachPattern;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by mpostelnicu
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Documented
@EachPattern(regexp = "^[a-zA-Z0-9\\-]*$", message = "Invalid organization id!")
@Constraint(validatedBy = {})
@NotEmpty
public @interface OrganizationIdValidation {

    String message() default "Organization Id is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
