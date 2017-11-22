package org.devgateway.toolkit.persistence.excel;

import org.apache.poi.ss.usermodel.Row;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;

import java.util.List;

/**
 * Create an Excel Sheet representation of an Object of type T.
 * In this process it's possible to create a new Excel Sheet if
 * some fields are annotated with {@link ExcelExport#separateSheet = true}
 *
 * @author idobre
 * @since 13/11/2017
 */
public interface ExcelSheet {
    /**
     * Write the value into specified {@link Row} and column number.
     *
     * @param value
     * @param row
     * @param column
     */
    void writeCell(Object value, Row row, int column);

    /**
     * Write the object into specified {@link Row}.
     * We need to also know the {@link Class} of the Object
     * since we will export null objects as well but with empty cells.
     *
     * @param clazz
     * @param object
     * @param row
     */
    void writeRow(Class clazz, Object object, Row row);

    /**
     * Write the received list of objects into an excel sheet.
     * We need to also know the {@link Class} of the Object(s)
     * since we will export null objects as well but with empty cells.
     *
     * @param clazz
     * @param objects
     */
    void writeSheet(Class clazz, List<Object> objects);

    /**
     * Write the objects and return the first row index.
     * Used to (possible) create a document link between sheets.
     */
    int writeSheetGetLink(Class clazz, List<Object> objects);

    /**
     * Returns the Sheet name.
     */
    String getExcelSheetName();

    /**
     * Just create an empty excel sheet.
     */
    void emptySheet();
}
