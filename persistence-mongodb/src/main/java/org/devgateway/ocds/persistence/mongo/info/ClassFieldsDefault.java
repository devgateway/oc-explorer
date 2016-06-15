package org.devgateway.ocds.persistence.mongo.info;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author idobre
 * @since 6/3/16
 */
public final class ClassFieldsDefault implements ClassFields {
    private final Class clazz;

    private Field[] declaredFields;

    public ClassFieldsDefault(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Iterator<Field> getFields() {
        // cache declared fields of a class
        if (declaredFields == null) {
            declaredFields = clazz.getDeclaredFields();
        }

        // filter some of the fields including this$0 used in inner classes
        Iterator<Field> fields = Arrays.stream(declaredFields)
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .filter(field -> !field.getName().equals("this$0"))
                .iterator();

        return fields;
    }
}
