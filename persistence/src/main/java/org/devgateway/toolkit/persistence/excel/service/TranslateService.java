package org.devgateway.toolkit.persistence.excel.service;

import java.lang.reflect.Field;

/**
 * @author idobre
 * @since 09/05/2018
 */
public interface TranslateService {
    String getTranslation(Class clazz, Field field);
}
