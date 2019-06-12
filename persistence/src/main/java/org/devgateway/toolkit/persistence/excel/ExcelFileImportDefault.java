package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.lang3.Validate;
import org.devgateway.toolkit.persistence.excel.info.ImportBean;
import org.devgateway.toolkit.persistence.excel.reader.XExcelFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the {@link ExcelFileImport} type.
 *
 * @author idobre
 * @since 21/03/2018
 */
public class ExcelFileImportDefault implements ExcelFileImport {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileImportDefault.class);

    private final Class clazz;

    private final ImportBean importBean;

    private final XExcelFileReader xExcelFileReader;

    private final ApplicationContext applicationContext;

    public ExcelFileImportDefault(final ApplicationContext applicationContext, final InputStream is,
                                  final Class clazz, final ImportBean importBean)
            throws Exception {
        Validate.notNull(is, "The InputStream can't be null!");
        Validate.notNull(clazz, "The Class can't be null!");

        this.applicationContext = applicationContext;
        this.clazz = clazz;
        this.importBean = importBean;
        this.xExcelFileReader = new XExcelFileReader(is);
    }

    @Override
    public ImportResponse readWorkbook()
            throws Exception {
        final long startTime = System.nanoTime();
        logger.warn(">>>>>>>>>>>>>>> import started >>>>>>>>>>>>>>>");

        final List<String[]> rows = xExcelFileReader.readRows(1000);
        xExcelFileReader.close();
        normalizeRows(rows);

        ExcelSheetImport excelSheetImport = new ExcelSheetImportDefault(this.applicationContext, rows, this.importBean);
        final ImportResponse importResponse = excelSheetImport.readSheet(clazz);

        final long endTime = System.nanoTime();
        final double duration = (endTime - startTime) / 1000000000.0;
        logger.warn(">>>>>>>>>>>>>>> import ended |  duration: " + duration + " >>>>>>>>>");

        return importResponse;
    }

    /**
     * Normalize all rows to the same length - we can encounter this situation when last columns are Strings
     * and they are empty, making this hypothetical row smaller than a row that has values for all columns.
     */
    private void normalizeRows(final List<String[]> rows) {
        // find the longest row
        final Optional<Integer> maxRowLength = rows.stream().map(row -> row.length).max(Integer::compareTo);

        if (maxRowLength.isPresent()) {
            // update all rows to the maxRowLength
            for (int i = 0; i < rows.size(); i++) {
                final String[] row = rows.get(i);
                if (row.length < maxRowLength.get()) {
                    final List<String> newRow = new ArrayList<>(Arrays.asList(row));
                    // adding the remaining elements
                    for (int j = 0; j < maxRowLength.get() - row.length; j++) {
                        newRow.add(null);
                    }

                    rows.set(i, newRow.toArray(new String[newRow.size()]));
                }
            }
        }
    }
}
