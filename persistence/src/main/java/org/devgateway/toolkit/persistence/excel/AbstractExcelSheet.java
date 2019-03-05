package org.devgateway.toolkit.persistence.excel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Class that prepares the default Styles and Fonts for Excel cells.
 *
 * @author idobre
 * @since 13/11/2017
 */
public abstract class AbstractExcelSheet implements ExcelSheet {
    protected final Workbook workbook;

    private Font dataFont;

    private Font headerFont;

    private Font linkFont;

    private final CellStyle dataStyleCell;

    private final CellStyle headerStyleCell;

    private final CellStyle linkStyleCell;

    private final CreationHelper createHelper;

    // declare only one cell object reference
    private Cell cell;

    public AbstractExcelSheet(final Workbook workbook) {
        this.workbook = workbook;

        // get the styles from workbook without creating them again (by default the workbook has already 1 style)
        if (workbook.getNumCellStyles() > 1) {
            this.dataStyleCell = workbook.getCellStyleAt((short) 1);
            this.headerStyleCell = workbook.getCellStyleAt((short) 2);
            this.linkStyleCell = workbook.getCellStyleAt((short) 3);
        } else {
            // init the fonts and styles
            this.dataFont = this.workbook.createFont();
            this.dataFont.setFontHeightInPoints((short) 12);
            this.dataFont.setFontName("Times New Roman");
            this.dataFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

            this.headerFont = this.workbook.createFont();
            this.headerFont.setFontHeightInPoints((short) 14);
            this.headerFont.setFontName("Times New Roman");
            this.headerFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
            this.headerFont.setBold(true);

            this.linkFont = this.workbook.createFont();
            this.linkFont.setFontHeightInPoints((short) 12);
            this.linkFont.setFontName("Times New Roman");
            // by default hyperlinks are blue and underlined
            this.linkFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
            this.linkFont.setUnderline(Font.U_SINGLE);

            this.dataStyleCell = this.workbook.createCellStyle();
            this.dataStyleCell.setAlignment(HorizontalAlignment.LEFT);
            this.dataStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
            this.dataStyleCell.setWrapText(true);
            this.dataStyleCell.setFont(this.dataFont);

            this.headerStyleCell = this.workbook.createCellStyle();
            this.headerStyleCell.setAlignment(HorizontalAlignment.CENTER);
            this.headerStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
            this.headerStyleCell.setWrapText(true);
            this.headerStyleCell.setFont(this.headerFont);

            this.linkStyleCell = this.workbook.createCellStyle();
            this.linkStyleCell.setAlignment(HorizontalAlignment.LEFT);
            this.linkStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
            this.linkStyleCell.setWrapText(true);
            this.linkStyleCell.setFont(this.linkFont);
        }

        this.createHelper = workbook.getCreationHelper();
    }

    /**
     * Creates a cell and tries to determine it's type based on the value type.
     * <p>
     * There is only one Cell object otherwise the Heap Space will fill really quickly.
     *
     * @param value
     * @param row
     * @param column
     */
    @Override
    public void writeCell(final Object value, final Row row, final int column) {
        // try to determine the cell type based on the object value
        // if nothing matches then use 'CellType.STRING' as type and call the object toString() function.
        //      * don't create any cell if the value is null (Cell.CELL_TYPE_BLANK)
        //      * do nothing if we have an empty List/Set instead of display empty brackets like [ ]
        if (value != null && !((value instanceof List || value instanceof Set) && ((Collection) value).isEmpty())) {
            if (value instanceof String) {
                cell = row.createCell(column, CellType.STRING);
                cell.setCellValue((String) value);
            } else {
                if (value instanceof Integer) {
                    cell = row.createCell(column, CellType.NUMERIC);
                    cell.setCellValue((Integer) value);
                } else {
                    if (value instanceof BigDecimal) {
                        cell = row.createCell(column, CellType.NUMERIC);
                        cell.setCellValue(((BigDecimal) value).doubleValue());
                    } else {
                        if (value instanceof Boolean) {
                            cell = row.createCell(column, CellType.BOOLEAN);
                            cell.setCellValue(((Boolean) value) ? "Yes" : "No");
                        } else {
                            if (value instanceof Date) {
                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                cell = row.createCell(column);
                                cell.setCellValue(sdf.format((Date) value));
                            } else {
                                cell = row.createCell(column, CellType.STRING);
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }
            }

            cell.setCellStyle(dataStyleCell);
        } else {
            // create a CellType.BLANK
            row.createCell(column);
        }
    }

    /**
     * Create a header cell with a particular style.
     *
     * @param value
     * @param row
     * @param column
     */
    protected void writeHeaderCell(final Object value, final Row row, final int column) {
        this.writeCell(value, row, column);
        cell.setCellStyle(headerStyleCell);
    }

    /**
     * Creates a cell that is a link to another sheet in the document {@link HyperlinkType#DOCUMENT}.
     *
     * @param value
     * @param row
     * @param column
     * @param sheetName
     * @param rowNumber
     */
    public void writeCellLink(final Object value, final Row row, final int column,
                              final String sheetName, final int rowNumber) {
        this.writeCell(value, row, column);
        final Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);

        // always point to first column A in excel file
        link.setAddress("'" + sheetName + "'!A" + rowNumber);
        cell.setHyperlink(link);
        cell.setCellStyle(linkStyleCell);
    }

    /**
     * Create a new row and set the default height (different heights for headers and data rows).
     *
     * @param sheet
     * @param rowNumber
     * @return Row
     */
    protected Row createRow(final Sheet sheet, final int rowNumber) {
        final Row row = sheet.createRow(rowNumber);

        if (rowNumber < 1) {
            row.setHeight((short) 2000);             // 100px (2000 / 10 / 2)
        } else {
            row.setHeight((short) 600);              // 30px  (600 / 10 / 2)
        }

        return row;
    }

    /**
     * Get the last 'free' cell of a {@link Row}.
     *
     * @param row
     */
    protected int getFreeColl(final Row row) {
        return row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
    }
}
