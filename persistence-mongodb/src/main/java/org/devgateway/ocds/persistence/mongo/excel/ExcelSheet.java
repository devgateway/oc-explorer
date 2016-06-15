package org.devgateway.ocds.persistence.mongo.excel;

import org.apache.poi.ss.usermodel.Row;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;

import java.util.List;

/**
 * Create an Excel Sheet representation of an Object of type T.
 * In this process it's possible to create new Excel Sheet if
 * some fields are annotated with {@link ExcelExportSepareteSheet}
 *
 * @author idobre
 * @since 6/7/16
 */
public interface ExcelSheet {
    void writeCell(final Object value, final Row row, final int column);

    void writeRow(final Object object, final Row row);

    void writeSheet(final List<Object> objects);

    void emptySheet();
}
