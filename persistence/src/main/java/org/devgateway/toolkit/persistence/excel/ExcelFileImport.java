package org.devgateway.toolkit.persistence.excel;

/**
 * ExcelFileImport Type - it should transform an Excel into a list of Objects.
 *
 * @author idobre
 * @since 21/03/2018
 */
public interface ExcelFileImport {
    /**
     * Read an Workbook and converts it's rows into Entities.
     *
     * @return {@link ImportResponse}
     */
    ImportResponse readWorkbook() throws Exception;
}
