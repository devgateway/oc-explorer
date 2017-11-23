package org.devgateway.toolkit.persistence.excel;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * ExcelFile Type - it should transform a list of Objects into a Workbook.
 *
 * @author idobre
 * @since 13/11/2017
 */
public interface ExcelFile {
    /**
     * Create an Workbook that can be exported.
     *
     * @return {@link Workbook}
     */
    Workbook createWorkbook();
}
