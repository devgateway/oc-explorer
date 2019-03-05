package org.devgateway.toolkit.persistence.excel.annotation;

import org.devgateway.toolkit.persistence.dao.categories.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that indicates if a field should be exported to Excel Export.
 *
 * {@link #name}           - field name that is used in the Excel header - can be the same used in {@link ExcelExport}
 * {@link #importByProp}   - parameter that indicates that we need to find this object from the database. It's values
 *                           can be: id/value/label/etc
 * {@link #importBean}     - repository class that will be used to fetch the value from the database. This is used when
 *                           we don't have a unique repository bean to be used, for example {@link Category}
 * {@link #onlyForClass}   - in case the class is extended and we want to Import this field only for a particular child
 *
 * @author idobre
 * @since 19/03/2018
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {
    String value() default "";

    String name() default "";

    String importByProp() default "";

    Class importBean() default void.class;

    Class[] onlyForClass() default void.class;
}
