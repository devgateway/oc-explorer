package org.devgateway.ocds.persistence.mongo.info;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author idobre
 * @since 6/3/16
 */
public class ClassFieldsDefault implements ClassFields {
    public final Class clazz;

    ClassFieldsDefault(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Iterator<Field> getFields() {
        Field[] declaredFields = clazz.getDeclaredFields();

        // filter some of the fields including this$0 used in inner classes
        Iterator<Field> fields = Arrays.stream(declaredFields)
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .filter(field -> !field.getName().equals("this$0"))
                .iterator();

        return fields;
    }
}
