package org.devgateway.toolkit.persistence.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author idobre
 * @since 15/11/2017
 */
public class AbstractExcelSheetTest {
    private class MockExcelSheet extends AbstractExcelSheet {
        public MockExcelSheet(Workbook workbook) {
            super(workbook);
        }

        @Override
        public void writeRow(Class clazz, Object object, Row row) {

        }

        @Override
        public void writeSheet(Class clazz, List<Object> objects) {

        }

        @Override
        public int writeSheetGetLink(Class clazz, List<Object> objects) {
            return 0;
        }

        @Override
        public String getExcelSheetName() {
            return null;
        }

        @Override
        public void emptySheet() {

        }
    }

    @Test
    public void writeCell() throws Exception {
        final Workbook workbook = new XSSFWorkbook();
        final ExcelSheet excelSheet = new MockExcelSheet(workbook);
        final Sheet sheet = workbook.createSheet("sheet");
        final Row row = sheet.createRow(0);

        excelSheet.writeCell(null, row, 0);
        excelSheet.writeCell(Boolean.TRUE, row, 1);
        excelSheet.writeCell("text", row, 2);
        excelSheet.writeCell(1, row, 3);

        Assert.assertEquals(CellType.BLANK, row.getCell(0).getCellType());
        Assert.assertEquals("Yes", row.getCell(1).getStringCellValue());
        Assert.assertEquals(CellType.STRING, row.getCell(2).getCellType());
        Assert.assertEquals(CellType.NUMERIC, row.getCell(3).getCellType());
    }
}
