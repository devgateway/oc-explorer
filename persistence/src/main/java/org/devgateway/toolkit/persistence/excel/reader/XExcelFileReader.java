package org.devgateway.toolkit.persistence.excel.reader;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Fast Excel Stream Reader with a small memory footprint usage.
 *
 * This class was updated for our scope from:
 * http://lchenaction.blogspot.com/2013/12/how-to-read-super-large-excel-and-csv.html
 *
 * @author idobre
 * @since 21/03/2018
 */
public final class XExcelFileReader {
    private int rowNum;

    private final OPCPackage opcPkg;

    private final ReadOnlySharedStringsTable stringsTable;

    private final XMLStreamReader xmlReader;

    /**
     * Reads data from the specified excel file and from the first sheet.
     */
    public XExcelFileReader(final InputStream is) throws Exception {
        opcPkg = OPCPackage.open(is);
        this.stringsTable = new ReadOnlySharedStringsTable(opcPkg);

        final XSSFReader xssfReader = new XSSFReader(opcPkg);
        final XMLInputFactory factory = XMLInputFactory.newInstance();

        // just get the first sheet from the workplan
        final XSSFReader.SheetIterator it = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        if (!it.hasNext()) {
            throw new Exception("The Workbook must have at least 1 Sheet!");
        }
        final InputStream inputStream = it.next();

        xmlReader = factory.createXMLStreamReader(inputStream);

        while (xmlReader.hasNext()) {
            xmlReader.next();
            if (xmlReader.isStartElement()) {
                if (xmlReader.getLocalName().equals("sheetData")) {
                    break;
                }
            }
        }
    }

    public int rowNum() {
        return rowNum;
    }

    public List<String[]> readRows(final int batchSize) throws XMLStreamException {
        final String elementName = "row";
        final List<String[]> dataRows = new ArrayList<>();
        if (batchSize > 0) {
            while (xmlReader.hasNext()) {
                xmlReader.next();
                if (xmlReader.isStartElement()) {
                    if (xmlReader.getLocalName().equals(elementName)) {
                        rowNum++;
                        dataRows.add(getDataRow());
                        if (dataRows.size() == batchSize) {
                            break;
                        }
                    }
                }
            }
        }

        return dataRows;
    }

    private String[] getDataRow() throws XMLStreamException {
        final List<String> rowValues = new ArrayList<>();

        while (xmlReader.hasNext()) {
            xmlReader.next();

            if (xmlReader.isStartElement()) {
                if (xmlReader.getLocalName().equals("c")) {
                    final CellReference cellReference = new CellReference(xmlReader.getAttributeValue(null, "r"));
                    // Fill in the possible blank cells!
                    while (rowValues.size() < cellReference.getCol()) {
                        rowValues.add("");
                    }
                    final String cellType = xmlReader.getAttributeValue(null, "t");
                    rowValues.add(getCellValue(cellType));
                }
            } else if (xmlReader.isEndElement() && xmlReader.getLocalName().equals("row")) {
                break;
            }
        }

        return rowValues.toArray(new String[rowValues.size()]);
    }

    private String getCellValue(final String cellType) throws XMLStreamException {
        String value = ""; // by default

        while (xmlReader.hasNext()) {
            xmlReader.next();
            if (xmlReader.isStartElement()) {
                // this is for xlsx without stringsTable, just inline
                if (xmlReader.getLocalName().equals("is")) {
                    if (cellType != null && cellType.equals("inlineStr")) {
                        while (xmlReader.hasNext()) {
                            xmlReader.next();

                            if (xmlReader.isStartElement() && xmlReader.getLocalName().equals("t")) {
                                return xmlReader.getElementText();
                            } else {
                                if (xmlReader.isEndElement() && xmlReader.getLocalName().equals("t")) {
                                    break;
                                }
                            }

                        }
                    }

                }

                // this part is for xlsx with stringsTable
                if (xmlReader.getLocalName().equals("v")) {
                    if (cellType != null && cellType.equals("s")) {
                        final int idx = Integer.parseInt(xmlReader.getElementText());
                        return new XSSFRichTextString(stringsTable.getEntryAt(idx)).toString();
                    } else {
                        return xmlReader.getElementText();
                    }
                }
            } else {
                if (xmlReader.isEndElement() && xmlReader.getLocalName().equals("c")) {
                    break;
                }
            }
        }
        return value;
    }

    public void close() {
        if (opcPkg != null) {
            try {
                opcPkg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}