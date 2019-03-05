package org.devgateway.toolkit.persistence.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that indicates if a field should be exported to Excel Export.
 *
 * {@link #name}           - field name that will be used in the Excel header
 * {@link #separateSheet}  - parameter that indicates if an object should be exported in a separate Excel Sheet
 * {@link #justExport}     - just export the Object usually using {@link #toString} method (without exporting it's
 *                           children)
 * {@link #useTranslation}  - parameter that indicates if we should try to get the field translation for the header
 * {@link #onlyForClass}   - in case the class is extended and we want to Import this field only for a particular child
 *
 * @author idobre
 * @since 10/11/2017
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {
    String value() default "";

    String name() default "";

    boolean separateSheet() default false;

    boolean useTranslation() default false;

    boolean justExport() default false;

    Class[] onlyForClass() default void.class;
}

