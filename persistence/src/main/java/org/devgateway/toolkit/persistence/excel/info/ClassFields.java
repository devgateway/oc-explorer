package org.devgateway.toolkit.persistence.excel.info;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Returns all the fields of a Class.
 *
 * @author idobre
 * @since 10/11/2017
 */
public interface ClassFields {
    Iterator<Field> getFields();

    Class getClazz();
}
