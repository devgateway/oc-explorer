package org.devgateway.toolkit.persistence.excel;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.excel.info.ClassFields;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsDefault;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Persistable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author idobre
 * @since 10/11/2017
 */
public class ExcelFieldServiceTest {
    private class TestClass implements Persistable<Long> {
        private static final long serialVersionUID = 1L;

        private Long id;

        private List<String> label;

        @ExcelExport(justExport = true)
        private OtherClass otherClass1;

        @ExcelExport(separateSheet = true)
        private OtherClass otherClass2;

        public TestClass(final Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }
    }

    private class OtherClass {
        private Boolean valid;

        @ExcelExport(name = "money")
        private Integer amount;
    }

    @Test
    public void getFieldType() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assert.assertEquals("Check basic field type", FieldType.basic, ExcelFieldService.getFieldType(firstField));

        final Field secondField = fields.next();      // get second element
        Assert.assertEquals("Check type class for a List", FieldType.basic, ExcelFieldService.getFieldType(secondField));

        final Field thirdField = fields.next();      // get third element
        Assert.assertEquals("Check basic field type for Object", FieldType.basic, ExcelFieldService.getFieldType(thirdField));

        final Field fourthField = fields.next();      // get fourth element
        Assert.assertEquals("Check objectSeparateSheet field type Object", FieldType.objectSeparateSheet, ExcelFieldService.getFieldType(fourthField));
    }

    @Test
    public void getFieldClass() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(TestClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assert.assertEquals("Check basic field class", Long.class, ExcelFieldService.getFieldClass(firstField));


        final Field secondField = fields.next();      // get second element
        Assert.assertEquals("Check field class for a List", String.class, ExcelFieldService.getFieldClass(secondField));
    }

    @Test
    public void getFields() throws Exception {
        final String[] expectedFields = {"otherClass1", "otherClass2"};

        final Iterator<Field> fields = ExcelFieldService.getFields(TestClass.class);

        final List<String> actualFields = new ArrayList<>();
        while (fields.hasNext()) {
            final Field f = fields.next();
            actualFields.add(f.getName());
        }

        Assert.assertArrayEquals("Check get fields", expectedFields, actualFields.toArray());
    }

    @Test
    public void getObjectID() throws Exception {
        final OtherClass otherClass = new OtherClass();
        Assert.assertEquals("Check object ID", Long.valueOf(-1), ExcelFieldService.getObjectID(otherClass));

        final TestClass testclass = new TestClass((long) 10);
        Assert.assertEquals("Check object ID", Long.valueOf(10), ExcelFieldService.getObjectID(testclass));
    }

    @Test
    public void getFieldName() throws Exception {
        final ClassFields classFields = new ClassFieldsDefault(OtherClass.class);
        final Iterator<Field> fields = classFields.getFields();

        final Field firstField = fields.next();      // get first element
        Assert.assertEquals("Check field name", "valid", ExcelFieldService.getFieldName(null, firstField, null));

        final Field secondField = fields.next();      // get second element
        Assert.assertEquals("Check field name", "money", ExcelFieldService.getFieldName(null, secondField, null));
    }
}
