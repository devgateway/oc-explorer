package org.devgateway.ocds.persistence.mongo.excel;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author idobre
 * @since 6/16/16
 */
public final class OCDSObjectUtil {
    private static final Logger LOGGER = Logger.getLogger(OCDSObjectUtil.class);

    private static Map<Field, Class> fieldsClassCache;

    static {
        fieldsClassCache = new HashMap<>();
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private OCDSObjectUtil() {

    }

    /**
     * Return the {@link FieldType} of a Field
     * This is used to determine the writing strategy for this particular field
     *
     * @param field
     * @return
     */
    public static int getFieldType(Field field) {
        final Class fieldClass = getFieldClass(field);

        if (FieldType.BASICTYPES.contains(fieldClass)) {
            return FieldType.BASIC_FIELD;
        }

        // check if this is an OCDS Object (an Object defined in our application)
        final String classPackage = fieldClass.getName().substring(0, fieldClass.getName().lastIndexOf('.'));
        if (classPackage.contains("org.devgateway")) {
            if (fieldClass.isEnum()) {
                return FieldType.BASIC_FIELD;
            }

            if (field.getAnnotation(ExcelExportSepareteSheet.class) != null) {
                return FieldType.OCDS_OBJECT_SEPARETE_SHEET_FIELD;
            }

            return FieldType.OCDS_OBJECT_FIELD;
        }

        LOGGER.error("We didn't get the field type for: '" + field.getName()
                + "', returning: " + FieldType.BASIC_FIELD);
        return FieldType.BASIC_FIELD;
    }

    /**
     * Return the Class of a field
     *
     * @param field
     * @return
     */
    public static Class getFieldClass(Field field) {
        Class fieldClass = null;

        if (fieldsClassCache.get(field) != null) {
            fieldClass = fieldsClassCache.get(field);
        } else {
            if (field.getType().equals(java.util.Set.class) || field.getType().equals(java.util.List.class)) {
                final ParameterizedType genericListType = (ParameterizedType) field.getGenericType();
                try {
                    fieldClass = Class.forName(genericListType.getActualTypeArguments()[0].getTypeName());
                } catch (ClassNotFoundException e) {
                    LOGGER.error(e);
                }
            } else {
                fieldClass = field.getType();
            }

            // check if the class was extended and return thew new type
            if (INHERITEDOCDSOBJECTS.get(fieldClass) != null) {
                fieldClass = INHERITEDOCDSOBJECTS.get(fieldClass);
            }

            fieldsClassCache.put(field, fieldClass);
        }

        return fieldClass;
    }

    /**
     * Use this Map if we have a particular implementation of the OCDS with many of the Objects extended
     * Example:
     *      .put(Award.class, VNAward.class)
     *      .put(Budget.class, VNBudget.class)
     */
    public static final ImmutableMap<Class, Class> INHERITEDOCDSOBJECTS = new ImmutableMap.Builder<Class, Class>()
            .build();
}
