package org.devgateway.ocds.web.excelcharts;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author idobre
 * @since 8/16/16
 *
 * Create an Workbook with an excel chart that can be exported.
 */
public interface ExcelChart {
    Workbook createWorkbook();
}
