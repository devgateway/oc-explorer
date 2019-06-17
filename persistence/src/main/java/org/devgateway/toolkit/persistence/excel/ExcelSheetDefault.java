package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.toolkit.persistence.excel.service.TranslateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringJoiner;

/**
 * @author idobre
 * @since 13/11/2017
 */
public class ExcelSheetDefault extends AbstractExcelSheet {
    private static final Logger logger = LoggerFactory.getLogger(ExcelSheetDefault.class);

    private static final String PARENTSHEET = "parentSheet";
    private static final String PARENTID = "parentID";
    private static final String PARENTROWNUMBER = "parentRowNumber";

    private final TranslateService translateService;

    private final Sheet excelSheet;

    private final String excelSheetName;

    // Use this field to identify the header prefix for children objects that are exported in the same sheet.
    private final Stack<String> headerPrefix;

    private Map<String, Object> parentInfo;

    // Boolean that identifies if the header was written already for this sheet.
    private Boolean headerWasWritten = false;

    /**
     * Constructor used to print an Object in a separate excel sheet.
     * The sheet will be created based on object name that is exported.
     *
     * {@link #excelSheetName} would be the name of the excel
     * and the recommended value is {@link Class#getSimpleName} toLowerCase.
     *
     */
    public ExcelSheetDefault(final Workbook workbook, final TranslateService translateService,
                             final String excelSheetName) {
        super(workbook);

        this.excelSheetName = excelSheetName;
        this.translateService = translateService;
        this.headerPrefix = new Stack<>();

        // it's possible that this sheet was created by other object
        if (workbook.getSheet(excelSheetName) == null) {
            // create the main excel sheet
            excelSheet = workbook.createSheet(excelSheetName);

            // freeze the header row
            excelSheet.createFreezePane(0, 1);

            // create the first row that is used as a header
            createRow(excelSheet, 0);

            // set a default width - this will increase the performance
            excelSheet.setDefaultColumnWidth(35);
        } else {
            excelSheet = workbook.getSheet(excelSheetName);
        }
    }

    /**
     * Constructor used to print an Object in a separate sheet
     * but with the additional possibility to create a link back to Object's Parent sheet.
     *
     */
    ExcelSheetDefault(final Workbook workbook, final TranslateService translateService,
                      final String excelSheetName, final Map<String, Object> parentInfo) {
        this(workbook, translateService, excelSheetName);

        this.parentInfo = parentInfo;
    }

    @Override
    public void writeRow(final Class clazz, final Object object, final Row row) {
        final Iterator<Field> fields = ExcelFieldService.getFields(clazz);

        // if we have a parent then the first row should be the parent name with a link.
        if (getFreeColl(row) == 0 && parentInfo != null) {
            String parentCell = (String) parentInfo.get(PARENTSHEET);
            if (parentInfo.get(PARENTID) != null) {
                parentCell += " - " + parentInfo.get(PARENTID);
            }
            writeHeaderLabel("Parent", row, 0);
            writeCellLink(parentCell, row, 0,
                    (String) parentInfo.get(PARENTSHEET), (int) parentInfo.get(PARENTROWNUMBER));
        }

        while (fields.hasNext()) {
            final Field field = fields.next();
            final FieldType fieldType = ExcelFieldService.getFieldType(field);

            try {
                switch (fieldType) {
                    case basic:
                        int coll = getFreeColl(row);
                        writeHeaderLabel(clazz, field, row, coll);
                        writeCell(getFieldValue(field, object), row, coll);

                        break;

                    case object:
                        headerPrefix.push(ExcelFieldService.getFieldName(clazz, field, translateService));
                        Class fieldClass = ExcelFieldService.getFieldClass(field);

                        if (ExcelFieldService.isCollection(field)) {
                            final List<Object> flattenObjects = new ArrayList();
                            flattenObjects.addAll((Collection) getFieldValue(field, object));
                            writeRowFlattenObject(fieldClass, flattenObjects, row);
                        } else {
                            writeRow(fieldClass, getFieldValue(field, object), row);
                        }

                        headerPrefix.pop();
                        break;

                    case objectSeparateSheet:
                        fieldClass = ExcelFieldService.getFieldClass(field);

                        // add some informations about the parent
                        final Map<String, Object> info = new HashMap();
                        info.put(PARENTSHEET, excelSheetName);
                        info.put(PARENTID, ExcelFieldService.getObjectID(object));
                        info.put(PARENTROWNUMBER, row.getRowNum() + 1);

                        final ExcelSheet objectSepareteSheet = new ExcelSheetDefault(workbook, this.translateService,
                                fieldClass.getSimpleName().toLowerCase(), info);
                        final List<Object> newObjects = new ArrayList();
                        final Object value = getFieldValue(field, object);

                        if (value != null) {
                            if (ExcelFieldService.isCollection(field)) {
                                newObjects.addAll((Collection) value);
                            } else {
                                newObjects.add(value);
                            }
                        }

                        coll = getFreeColl(row);
                        writeHeaderLabel(clazz, field, row, coll);
                        final int rowNumber = objectSepareteSheet.writeSheetGetLink(fieldClass, newObjects);
                        if (rowNumber != -1) {
                            writeCellLink(field.getName(), row, coll,
                                    objectSepareteSheet.getExcelSheetName(), rowNumber);
                        } else {
                            writeCell(null, row, coll);
                        }

                        break;

                    default:
                        logger.error("Undefined field type");
                        break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("writeRow Error!", e);
            }
        }
    }

    /**
     * Function to flat export an array of objects. Example:
     * [{
     *      x: aaa,
     *      y: bbb,
     *  },{
     *      x: ccc,
     *      y: ddd,
     * }]
     *
     * Will be printed as:
     *      |     x     |     y     |
     *      | aaa ; bbb | ccc ; ddd |
     *
     * @param clazz
     * @param objects
     * @param row
     */
    private void writeRowFlattenObject(final Class clazz, final List<Object> objects, final Row row) {
        final Iterator<Field> fields = ExcelFieldService.getFields(clazz);

        while (fields.hasNext()) {
            final Field field = fields.next();
            final FieldType fieldType = ExcelFieldService.getFieldType(field);

            try {
                switch (fieldType) {
                    case basic:
                        final int coll = getFreeColl(row);
                        final StringJoiner flattenValue = new StringJoiner(" | ");

                        for (final Object obj : objects) {
                            final Object value = getFieldValue(field, obj);
                            if (value != null) {
                                flattenValue.add(value.toString());
                            }
                        }

                        writeHeaderLabel(clazz, field, row, coll);
                        writeCell(flattenValue.toString(), row, coll);
                        break;

                    case object:
                        if (ExcelFieldService.isCollection(field)) {
                            logger.error("Unsupported operation for field: '" + field.getName() + "'! You can not "
                                    + "flatten an array of objects that contains other array of objects");
                        } else {
                            headerPrefix.push(ExcelFieldService.getFieldName(clazz, field, translateService));
                            final Class fieldClass = ExcelFieldService.getFieldClass(field);
                            final List<Object> newObjects = new ArrayList();

                            for (Object obj : objects) {
                                final Object value = getFieldValue(field, obj);
                                if (value != null) {
                                    newObjects.add(value);
                                }
                            }

                            writeRowFlattenObject(fieldClass, newObjects, row);
                            headerPrefix.pop();
                        }

                        break;

                    case objectSeparateSheet:
                        logger.error("Unsupported operation for field: '" + field.getName() + "'! You can not flatten "
                                + "an array of objects that contains objects that need to be printed in other sheet.");
                        break;

                    default:
                        logger.error("Undefined field type");
                        break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("writeRowFlattenObject error!", e);
            }
        }
    }

    @Override
    public void writeSheet(final Class clazz, final List<Object> objects) {
        for (Object obj : objects) {
            int lastRow = excelSheet.getLastRowNum();
            final Row row = createRow(excelSheet, ++lastRow);

            writeRow(clazz, obj, row);
        }
    }

    @Override
    public int writeSheetGetLink(final Class clazz, final List<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return -1;
        }

        // check if this is first time when we created this sheet and skip header row
        // also add 2 since the getLastRowNum() function is 0-based and Excel is 1-based
        int lastRow = excelSheet.getLastRowNum() == 0 ? 2 : (excelSheet.getLastRowNum() + 2);
        this.writeSheet(clazz, objects);

        return lastRow;
    }

    /**
     * Print an error message in case we have an empty sheet.
     */
    public void emptySheet() {
        final Row row;
        if (excelSheet.getRow(0) == null) {
            row = createRow(excelSheet, 0);
        } else {
            row = excelSheet.getRow(0);
        }
        writeHeaderCell("There are no records to export", row, 0);
    }

    /**
     * Compute the header prefix for the current object.
     */
    private String getHeaderPrefix() {
        if (headerPrefix.isEmpty()) {
            return "";
        }

        final StringJoiner header = new StringJoiner("/");
        final Enumeration<String> elements = headerPrefix.elements();
        while (elements.hasMoreElements()) {
            header.add(elements.nextElement());
        }

        return header.toString() + "/";
    }

    /**
     * Functions that check if the header cell is empty, if yes then add the label.
     */
    private void writeHeaderLabel(final String label, final Row row, final int coll) {
        if (!headerWasWritten) {
            if (row.getRowNum() == 1) { // write the header only once.
                final Row headerRow = excelSheet.getRow(0);
                final Cell headerCell = headerRow.getCell(coll);

                if (headerCell == null) {
                    writeHeaderCell(label, headerRow, coll);
                }
            } else {
                headerWasWritten = true;
            }
        }
    }

    /**
     * Functions that check if the header cell is empty, if yes then add the field label.
     */
    private void writeHeaderLabel(final Class clazz, final Field field, final Row row, final int coll) {
        if (!headerWasWritten) {
            writeHeaderLabel(getHeaderPrefix()
                    + ExcelFieldService.getFieldName(clazz, field, translateService), row, coll);
        }
    }

    /**
     * Return the value of a {@link Field} from an {@link Object}.
     */
    private Object getFieldValue(final Field field, final Object object)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return object == null || !PropertyUtils.isReadable(object, field.getName())
                ? null
                : PropertyUtils.getProperty(object, field.getName());
    }

    @Override
    public String getExcelSheetName() {
        return excelSheetName;
    }
}
