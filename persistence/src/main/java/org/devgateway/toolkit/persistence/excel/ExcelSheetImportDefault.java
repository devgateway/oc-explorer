package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.devgateway.toolkit.persistence.excel.info.ImportBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 22/03/2018
 */
public class ExcelSheetImportDefault implements ExcelSheetImport {
    private static final Logger logger = LoggerFactory.getLogger(ExcelSheetImportDefault.class);

    private final ImportBean importBean;

    private final List<String[]> rows;

    private final ApplicationContext applicationContext;

    private final ImportResponse importResponse;

    private Field parentField;

    public ExcelSheetImportDefault(final ApplicationContext applicationContext,
                                   final List<String[]> rows,
                                   final ImportBean importBean) {
        this.applicationContext = applicationContext;
        this.rows = rows;
        this.importBean = importBean;
        this.importResponse = new ImportResponse();
    }

    @Override
    public Object readRow(final Class clazz, final Integer rowNum, final MutableInt colNum) throws Exception {
        final Iterator<Field> fields = ExcelFieldImportService.getFields(clazz);
        final Constructor[] constructors = clazz.getConstructors();

        Constructor constructor = null;
        for (int i = 0; i < constructors.length; i++) {
            // check if we have a default constructor with no arguments
            if (constructors[i].getParameterCount() == 0) {
                constructor = constructors[i];
            }
        }

        if (constructor == null) {
            final String error = clazz + " doesn't have a default constructor without arguments, abort the reading";
            logger.error(error);
            addError(rowNum, error);

            return null;
        }

        // instantiate the object
        final Object object = constructor.newInstance();

        while (fields.hasNext()) {
            final Field field = fields.next();
            final FieldTypeImport fieldType = ExcelFieldImportService.getFieldType(field);

            if (ExcelFieldService.isCollection(field)) {
                final String error = "We don't support collections: " + clazz.getSimpleName() + " - " + field.getName();
                logger.error(error);
                addError(rowNum, error);
            } else {
                switch (fieldType) {
                    case basic:
                        try {
                            setFieldValue(object, field, getCellValueAndConvert(rowNum, colNum, field));
                        } catch (Exception e) {
                            final String error = "Error setting the value for field: " + field.getName()
                                    + " from column: " + colNum.getValue();
                            addError(rowNum, error);
                            throw e;
                        }

                        break;

                    case object:
                        final Class fieldClass = ExcelFieldService.getFieldClass(field);

                        // keep a reference to Field's parent and restore it back after recursion call
                        final Field parentBackup = this.parentField;
                        this.parentField = field;

                        final Object fieldObject = readRow(fieldClass, rowNum, colNum);

                        this.parentField = parentBackup;

                        try {
                            setFieldValue(object, field, fieldObject);
                        } catch (Exception e) {
                            final String error = "Error setting the value for field: " + field.getName()
                                    + " from column: " + colNum.getValue();
                            addError(rowNum, error);
                            throw e;
                        }

                        break;

                    case objectImportByProp:
                        final Object cellValue = getCellValue(rowNum, colNum);
                        try {
                            final Object value = ExcelFieldImportService.getObjectFromRepository(
                                    this.applicationContext, this.parentField, field, cellValue, this.importBean);
                            setFieldValue(object, field, value);
                            if (value == null) {
                                final String error = "Error getting the value from the database for field: "
                                        + field.getName() + " from column: " + colNum.getValue()
                                        + " - the cell value is: " + cellValue;
                                addError(rowNum, error);
                            }
                        } catch (Exception e) {
                            final String error = "Error getting and setting the value from the database for field: "
                                    + field.getName() + " from column: " + colNum.getValue()
                                    + " - the cell value is: " + cellValue;
                            addError(rowNum, error);
                            throw e;
                        }

                        break;
                    default:
                        logger.error("Undefined field type");
                        break;
                }
            }
        }

        return object;
    }

    @Override
    public ImportResponse readSheet(final Class clazz) throws Exception {
        // we skip the first row since we don't import the header :)
        for (int rowNum = 1; rowNum < this.rows.size(); rowNum++) {
            this.importResponse.getObjects().add(this.readRow(clazz, rowNum, new MutableInt(0)));
        }

        return this.importResponse;
    }

    /**
     * Return the cell value for a particular row and column.
     */
    private String getCellValue(final Integer rowNum, final MutableInt colNum) {
        final String value;

        if (rows.get(rowNum).length <= colNum.getValue()) {
            final String error = "We don't have that many columns (" + colNum.getValue() + ") in this row: "
                    + rowNum + " - total number of columns is: " + (rows.get(rowNum).length - 1);
            logger.error(error);
            addError(rowNum, error);
            value = null;
        } else {
            value = rows.get(rowNum)[colNum.getValue()];
        }

        // have a single point of increment, only when we read the data
        colNum.increment();

        return value;
    }

    /**
     * Return and convert the cell value for a particular row and column.
     */
    private Object getCellValueAndConvert(final Integer rowNum, final MutableInt colNum, final Field field) {
        final String value = this.getCellValue(rowNum, colNum);

        // for integers we need to convert them to double first
        return field.getType() == int.class || field.getType() == Integer.class
                ? ConvertUtils.convert(ConvertUtils.convert(value, Double.class), field.getType())
                : ConvertUtils.convert(value, field.getType());
    }

    /**
     * Set the value of a {@link Field} from an {@link Object}.
     */
    private void setFieldValue(final Object object, final Field field, final Object value)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (PropertyUtils.isWriteable(object, field.getName())) {
            PropertyUtils.setProperty(object, field.getName(), value);
        }
    }

    private void addError(final Integer rowNum, final String error) {
        final Map<Integer, List<String>> errors = this.importResponse.getErrors();
        List<String> rowErrors = this.importResponse.getErrors().get(rowNum);

        // if needed we initialize the row errors list.
        if (rowErrors == null) {
            rowErrors = new ArrayList<>();
            errors.put(rowNum, rowErrors);
        }

        rowErrors.add(error);
    }
}
