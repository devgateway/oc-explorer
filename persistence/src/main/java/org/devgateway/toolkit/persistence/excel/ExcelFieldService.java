package org.devgateway.toolkit.persistence.excel;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.excel.info.ClassFields;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsDefault;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsExcelExport;
import org.devgateway.toolkit.persistence.excel.service.TranslateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 10/11/2017
 */
public final class ExcelFieldService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFieldService.class);

    private static Map<Field, FieldType> fieldsTypeCache;

    private static Map<Field, Class> fieldsClassCache;

    private static Map<Class, List<Field>> fieldsCache;


    static {
        fieldsTypeCache = new HashMap<>();
        fieldsClassCache = new HashMap<>();
        fieldsCache = new HashMap<>();
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private ExcelFieldService() {

    }

    /**
     * Return the {@link FieldType} of a Field
     * This is used to determine the writing strategy for this particular field.
     *
     * @param field
     * @return {@link FieldType}
     */
    public static FieldType getFieldType(final Field field) {
        FieldType fieldType;

        if (fieldsTypeCache.get(field) != null) {
            fieldType = fieldsTypeCache.get(field);
        } else {
            fieldType = FieldType.basic;        // default field type
            final Class fieldClass = getFieldClass(field);

            // first we check if we have a basic type
            if (FieldType.BASICTYPES.contains(fieldClass) || fieldClass.isEnum()) {
                fieldType = FieldType.basic;
            } else {
                final ExcelExport excelExport = field.getAnnotation(ExcelExport.class);
                if (excelExport != null) {
                    fieldType = FieldType.object;

                    // if we just want to export a field (toString) we consider it a basic type.
                    if (excelExport.justExport()) {
                        fieldType = FieldType.basic;
                    }

                    // check if we want to export the object in a separate sheet
                    if (excelExport.separateSheet()) {
                        fieldType = FieldType.objectSeparateSheet;
                    }
                }
            }

            fieldsTypeCache.put(field, fieldType);
        }

        return fieldType;
    }


    /**
     * Return the Class of a field.
     *
     * @param field
     * @return {@link Class}
     */
    public static Class getFieldClass(final Field field) {
        Class fieldClass = null;

        if (fieldsClassCache.get(field) != null) {
            fieldClass = fieldsClassCache.get(field);
        } else {
            if (isCollection(field)) {
                final ParameterizedType genericListType = (ParameterizedType) field.getGenericType();
                try {
                    fieldClass = Class.forName(genericListType.getActualTypeArguments()[0].getTypeName());
                } catch (ClassNotFoundException e) {
                    logger.error("getFieldClass Error!", e);
                }
            } else {
                fieldClass = field.getType();
            }

            fieldsClassCache.put(field, fieldClass);
        }

        return fieldClass;
    }

    /**
     * Returns an Iterator with the Fields of a Class.
     * The fields are filtered with the {@link ClassFieldsExcelExport} class.
     *
     * @param clazz
     * @return {@link Iterator}
     */
    public static Iterator<Field> getFields(final Class clazz) {
        final Iterator<Field> fields;

        if (fieldsCache.get(clazz) != null) {
            final List<Field> fieldsList = fieldsCache.get(clazz);
            fields = fieldsList.iterator();
        } else {
            final ClassFields classFields = new ClassFieldsExcelExport(new ClassFieldsDefault(clazz, true));
            fields = classFields.getFields();

            fieldsCache.put(clazz, Lists.newArrayList(classFields.getFields()));
        }

        return fields;
    }

    /**
     * Get the ID for an Entity - {@link Persistable}.
     *
     * Return -1 if everything wrong happened.
     *
     * @param object
     * @return Entity ID
     */
    public static Long getObjectID(final Object object) {
        Long objectId = Long.valueOf(-1);

        if (object == null || !(Persistable.class.isAssignableFrom(object.getClass()))) {
            return Long.valueOf(-1);
        }

        try {
            final Method idMethod = object.getClass().getMethod("getId");
            if (idMethod != null) {
                objectId = (Long) idMethod.invoke(object);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("getObjectID error", e);
        }

        return objectId;
    }

    /**
     * Returns the name that we should use as a header for this {@link Field}.
     *
     * @param field
     */
    public static String getFieldName(final Class clazz, final Field field, final TranslateService translateService) {
        final ExcelExport excelExport = field.getAnnotation(ExcelExport.class);
        if (excelExport != null) {
            String label = null;
            if (excelExport.useTranslation()) {
                label = translateService.getTranslation(clazz, field);
            }
            if (!excelExport.name().isEmpty()) {
                if (StringUtils.isEmpty(label)) {
                    return excelExport.name();
                } else {
                    return excelExport.name() + " - " + label;
                }

            }
        }

        return field.getName();
    }

    /**
     * Returns the onlyForClass prop of the {@link Field}.
     *
     * @param field
     */
    public static Class[] getFieldClazz(final Field field) {
        final ExcelExport excelExport = field.getAnnotation(ExcelExport.class);
        if (excelExport != null) {
            if (excelExport.onlyForClass().length != 0
                    && !excelExport.onlyForClass()[0].getSimpleName().equals("void")) {
                return excelExport.onlyForClass();
            }
        }

        return null;
    }

    /**
     * Check if a {@link Field} Type is a Collection.
     *
     * @param field
     * @return {@link Boolean}
     */
    public static Boolean isCollection(final Field field) {
        return field.getType().equals(java.util.Collection.class)
                || field.getType().equals(java.util.Set.class)
                || field.getType().equals(java.util.List.class);
    }
}
