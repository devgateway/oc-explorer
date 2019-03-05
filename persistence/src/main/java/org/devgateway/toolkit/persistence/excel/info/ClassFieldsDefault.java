package org.devgateway.toolkit.persistence.excel.info;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link ClassFields} interface that will return all declared fields of a class.
 *
 * @author idobre
 * @since 10/11/2017
 */
public final class ClassFieldsDefault implements ClassFields {
    private final Class clazz;

    private final Boolean getInheritedFields;

    private Field[] declaredFields;

    public ClassFieldsDefault(final Class clazz, final Boolean getInheritedFields) {
        this.clazz = clazz;
        this.getInheritedFields = getInheritedFields;
    }

    public ClassFieldsDefault(final Class clazz) {
        this(clazz, false);
    }

    @Override
    public Iterator<Field> getFields() {
        // cache declared fields of a class
        if (declaredFields == null) {
            if (getInheritedFields) {
                declaredFields = getAllFields(clazz).toArray(new Field[getAllFields(clazz).size()]);
            } else {
                declaredFields = clazz.getDeclaredFields();
            }
        }

        // filter some of the fields including this$0 used in inner classes
        final Iterator<Field> fields = Arrays.stream(declaredFields)
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .filter(field -> !field.getName().equals("this$0"))
                .iterator();

        return fields;
    }

    @Override
    public Class getClazz() {
        return this.clazz;
    }

    /**
     * Function used to get also the inherited fields.
     *
     * @param clazz
     * @return
     */
    private List<Field> getAllFields(final Class clazz) {
        final List<Field> fields = new ArrayList<>();
        final Class superClazz = clazz.getSuperclass();

        if (superClazz != null) {
            fields.addAll(getAllFields(superClazz));
        }

        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        return fields;
    }
}
