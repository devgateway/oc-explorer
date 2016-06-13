package org.devgateway.ocds.persistence.mongo.excel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;
import org.devgateway.ocds.persistence.mongo.info.ClassFields;
import org.devgateway.ocds.persistence.mongo.info.ClassFieldsDefault;
import org.devgateway.ocds.persistence.mongo.info.ClassFieldsExcelExport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/**
 * Implementation of a {@link ExcelSheet} interface for an OCDS Object
 *
 * @author idobre
 * @since 6/7/16
 */
public final class OCDSObjectExcelSheet extends AbstractExcelSheet {
    private static final Logger LOGGER = Logger.getLogger(OCDSObjectExcelSheet.class);

    private final Sheet excelSheet;

    private final String excelSheetName;

    private final Class clazz;

    private final String headerPrefix;

    /**
     * Constructor used to print an OCDS Object in a separate excel sheet.
     * The sheet will be created based on object name that is exported
     *
     * @param workbook
     * @param clazz
     */
    OCDSObjectExcelSheet(final Workbook workbook, final Class clazz) {
        super(workbook);

        this.clazz = clazz;

        // get the excel sheet based on what type of object we export (release, award, contract)
        excelSheetName = clazz.getSimpleName().toLowerCase();

        headerPrefix = "";

        // it's possible that this sheet was created by other object
        if (workbook.getSheet(excelSheetName) == null) {
            // create the main excel sheet
            excelSheet = workbook.createSheet(excelSheetName);

            // freeze the header row
            excelSheet.createFreezePane(0, 1);

            // create the first row that is used as a header
            createRow(excelSheet, 0);

            // set a default width - this will increase the performance
            excelSheet.setDefaultColumnWidth(30);
        } else {
            excelSheet = workbook.getSheet(excelSheetName);
        }
    }

    /**
     * Constructor used to print an OCDS Object in an existing excel sheet
     *
     * @param workbook
     * @param clazz
     * @param excelSheetName
     * @param headerPrefix
     */
    OCDSObjectExcelSheet(final Workbook workbook, final Class clazz,
                         final String excelSheetName, final String headerPrefix) {
        super(workbook);

        this.clazz = clazz;
        this.excelSheetName = excelSheetName;
        this.headerPrefix = headerPrefix;

        // we should already have an excel sheet since we call constructor from another OCDSObjectExcelSheet object
        excelSheet = workbook.getSheet(excelSheetName);
    }

    @Override
    public void writeRow(final Object object, final Row row) {
        final ClassFields classFields = new ClassFieldsExcelExport(
                new ClassFieldsDefault(clazz)
        );
        final Iterator<Field> fields = classFields.getFields();

        while (fields.hasNext()) {
            final Field field = fields.next();

            final int fieldType = getFieldType(field);
            try {
                switch (fieldType) {
                    case FieldType.BASIC_FIELD:
                        // get the last 'free' cell
                        int coll = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();

                        writeHeaderLabel(field, coll);
                        writeCell(object == null ? null : PropertyUtils.getProperty(object, field.getName()), row, coll);

                        break;
                    case FieldType.OCDS_OBJECT_FIELD:
                        if (field.getType().equals(java.util.Set.class) || field.getType().equals(java.util.List.class)) {
                            final List<Object> ocdsFlattenObjects = new ArrayList();
                            ocdsFlattenObjects.addAll((Collection) PropertyUtils.getProperty(object, field.getName()));

                            writeFlattenObject(ocdsFlattenObjects, field, row, field.getName());
                        } else {
                            final OCDSObjectExcelSheet objectSheet = new OCDSObjectExcelSheet(
                                    this.workbook,
                                    getFieldClass(field),
                                    excelSheetName,
                                    getHeaderPrefix() + field.getName());

                            // print the Object on the same row
                            objectSheet.writeRow(object == null ? null : PropertyUtils.getProperty(object, field.getName()), row);
                        }

                        break;
                    case FieldType.OCDS_OBJECT_SEPARETE_SHEET_FIELD:
                        final OCDSObjectExcelSheet objectSheetSepareteSheet = new OCDSObjectExcelSheet(
                                this.workbook, getFieldClass(field));
                        final List<Object> ocdsObjectsSeparateSheet = new ArrayList();

                        if (field.getType().equals(java.util.Set.class) || field.getType().equals(java.util.List.class)) {
                            ocdsObjectsSeparateSheet.addAll((Collection) PropertyUtils.getProperty(object, field.getName()));
                        } else {
                            ocdsObjectsSeparateSheet.add(PropertyUtils.getProperty(object, field.getName()));
                        }
                        int rowNumber = objectSheetSepareteSheet.writeSheetGetLink(ocdsObjectsSeparateSheet);

                        if (rowNumber != -1) {
                            // get the last 'free' cell
                            coll = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();

                            writeHeaderLabel(field, coll);
                            writeCellLink(field.getName(), row, coll, objectSheetSepareteSheet.getExcelSheetName(), rowNumber);
                        }

                        break;

                    default:
                        LOGGER.error("Undefined field type");
                        break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Write the objects and return the first number.
     * Used to create a document link between sheets.
     *
     * @param objects
     * @return
     */
    private int writeSheetGetLink(final List<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return -1;
        }

        // check if this is first time when we created this sheet and skip header row
        // also add 2 since the getLastRowNum() function is 0-based and Excel is 1-based
        int lastRow = excelSheet.getLastRowNum() == 0 ? 2 : (excelSheet.getLastRowNum() + 2);
        this.writeSheet(objects);

        return lastRow;
    }

    @Override
    public void writeSheet(final List<Object> objects) {
        for (Object obj : objects) {
            int lastRow = excelSheet.getLastRowNum();
            Row row = createRow(excelSheet, ++lastRow);

            writeRow(obj, row);
        }
    }

    /**
     * Helper function to flat print an array of objects. Example:
     * [
     *      {
     *          x: aaa,
     *          y: bbb,
     *      },
     *      {
     *          x: ccc,
     *          y: ddd,
     *      }
     * ]
     *
     * Will be printed as:
     *      |     x     |     y     |
     *      | aaa ; bbb | ccc ; ddd |
     *
     * @param objects
     * @param field
     * @param row
     * @param labelPrefix
     */
    private void writeFlattenObject(final List<Object> objects, Field field, Row row, String labelPrefix) {
        final ClassFields classFields = new ClassFieldsExcelExport(
                new ClassFieldsDefault(getFieldClass(field))
        );
        final Iterator<Field> fields = classFields.getFields();

        while (fields.hasNext()) {
            final Field innerField = fields.next();

            final int innerFieldType = getFieldType(innerField);
            try {
                switch (innerFieldType) {
                    case FieldType.BASIC_FIELD:
                        // get the last 'free' cell
                        int coll = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
                        StringJoiner flattenValue = new StringJoiner(" | ");
                        for (Object obj : objects) {
                            if (obj != null && PropertyUtils.getProperty(obj, innerField.getName()) != null) {
                                flattenValue.add(PropertyUtils.getProperty(obj, innerField.getName()).toString());
                            }
                        }

                        writeHeaderLabel(labelPrefix + "/" + innerField.getName(), coll);
                        writeCell(flattenValue.toString(), row, coll);

                        break;
                    case FieldType.OCDS_OBJECT_FIELD:
                        if (innerField.getType().equals(java.util.Set.class) || innerField.getType().equals(java.util.List.class)) {
                            LOGGER.error("Unsupported operation for field: '" + innerField.getName() +
                                    "'! You can not flatten an array of objects that contains other array of objects " +
                                    "that need to be printed in other sheet.");
                        } else {
                            final List<Object> newObjects = new ArrayList();
                            for (Object obj : objects) {
                                newObjects.add(PropertyUtils.getProperty(obj, innerField.getName()));
                            }

                            writeFlattenObject(newObjects, innerField, row, labelPrefix + "/" + innerField.getName());
                        }

                        break;
                    case FieldType.OCDS_OBJECT_SEPARETE_SHEET_FIELD:
                        LOGGER.error("Unsupported operation for field: '" + innerField.getName() +
                                "'! You can not flatten an array of objects that contains objects " +
                                "that need to be printed in other sheet.");

                        break;
                    default:
                        LOGGER.error("Undefined field type");
                        break;
                }
            }  catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Return the {@link FieldType} of a Field
     * This is used to determine the writing strategy for this particular field
     *
     * @param field
     * @return
     */
    private int getFieldType(Field field) {
        final Class fieldClass = getFieldClass(field);

        if (FieldType.basicTypes.contains(fieldClass)) {
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
    private Class getFieldClass(Field field) {
        Class fieldClass = null;

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

        return fieldClass;
    }

    private String getHeaderPrefix() {
        if (StringUtils.isEmpty(headerPrefix)) {
            return "";
        } else {
            return headerPrefix + "/";
        }
    }

    /**
     * Functions that check if the header cell is empty, if yes then add the field label
     */
    private void writeHeaderLabel(Field field, int coll) {
        final Row headerRow = excelSheet.getRow(0);
        final Cell headerCell = headerRow.getCell(coll);

        if (headerCell == null) {
            writeCell(getHeaderPrefix() + field.getName(), headerRow, coll);
        }
    }

    /**
     * Functions that check if the header cell is empty, if yes then add the label
     */
    private void writeHeaderLabel(String label, int coll) {
        final Row headerRow = excelSheet.getRow(0);
        final Cell headerCell = headerRow.getCell(coll);

        if (headerCell == null) {
            writeCell(getHeaderPrefix() + label, headerRow, coll);
        }
    }

    /**
     * Print an error message in case we have an empty sheet
     */
    public void emptySheet() {
        final Row row;
        if (excelSheet.getRow(0) == null) {
            row = createRow(excelSheet, 0);
        } else {
            row = excelSheet.getRow(0);
        }
        writeCell("No objects returned.", row, 0);
        excelSheet.autoSizeColumn(0);
    }

    private String getExcelSheetName() {
        return excelSheetName;
    }
}
