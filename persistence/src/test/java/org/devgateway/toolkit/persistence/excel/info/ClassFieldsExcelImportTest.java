package org.devgateway.toolkit.persistence.excel.info;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author idobre
 * @since 19/03/2018
 */
public class ClassFieldsExcelImportTest {
    private class TestClass {
        private static final long serialVersionUID = 1L;

        @ExcelImport(name = "id")
        private int id;

        @ExcelImport
        private String label;

        private String description;
    }

    private class TestClassImproved extends TestClass {
        private static final long serialVersionUID = 1L;

        @ExcelImport
        private Boolean valid;

        private Long amount;
    }


    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"id", "label"};

        final ClassFields classFields = new ClassFieldsExcelImport(
                new ClassFieldsDefault(TestClass.class)
        );
        final Iterator<Field> fields = classFields.getFields();

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assert.assertArrayEquals("Check declared @ExcelImport fields", expectedFields, actualFields.toArray());
    }

    @Test
    public void getInheritedFields() throws Exception {
        final String[] expectedFields = {"id", "label", "valid"};

        final ClassFields classFields = new ClassFieldsExcelImport(
                new ClassFieldsDefault(TestClassImproved.class, true)
        );
        final Iterator<Field> fields = classFields.getFields();

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assert.assertArrayEquals("Check declared & inherited @ExcelImport fields",
                expectedFields, actualFields.toArray());
    }
}