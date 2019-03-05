package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Read an Excel Sheet representation of an Object of type T.
 *
 * @author idobre
 * @since 22/03/2018
 */
public interface ExcelSheetImport {
    /**
     * Read a row from the Excel Sheet and convert it to an Object.
     *
     * @param clazz
     * @param rowNum
     * @param colNum
     * @throws Exception
     */
    Object readRow(Class clazz, Integer rowNum, MutableInt colNum) throws Exception;

    /**
     * Read an Excel Sheet and return all the Objects found.
     *
     * @param clazz
     * @throws Exception
     */
    ImportResponse readSheet(Class clazz) throws Exception;
}
