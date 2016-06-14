package org.devgateway.ocds.persistence.mongo.info;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author idobre
 * @since 6/3/16
 */
public class ClassFieldsDefaultTest {
    private class TestClass {
        private static final long serialVersionUID = 1L;

        private int id;

        private String label;
    }

    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"id", "label"};

        ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        Iterator<Field> fields = classFields.getFields();

        List<String> actualFields = new ArrayList<>();
        while(fields.hasNext()) {
            Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assert.assertArrayEquals(expectedFields, actualFields.toArray());
    }
}
