package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.devgateway.toolkit.persistence.excel.service.TranslateService;

import java.util.List;

/**
 * Default implementation of the {@link ExcelFile} type.
 *
 * @author idobre
 * @since 13/11/2017
 */
public class ExcelFileDefault implements ExcelFile {
    private final List<Object> objects;

    private final Workbook workbook;

    private final TranslateService translateService;

    public ExcelFileDefault(final List<Object> objects, final TranslateService translateService) {
        Validate.notNull(objects, "The list of objects can't be null!");
        Validate.noNullElements(objects, "The list of objects can't contain null elements!");

        this.objects = objects;
        this.translateService = translateService;

        // create the excel file
        this.workbook = new SXSSFWorkbook(100);
    }

    @Override
    public Workbook createWorkbook() {
        // don't do anything if the list of objects is empty, just display the error message.
        if (objects.isEmpty()) {
            final ExcelSheet excelSheet = new ExcelSheetDefault(this.workbook, this.translateService, "no data");
            excelSheet.emptySheet();
        } else {
            final Class clazz = this.objects.get(0).getClass();
            final ExcelSheet excelSheet = new ExcelSheetDefault(this.workbook, this.translateService,
                    clazz.getSimpleName().toLowerCase());
            excelSheet.writeSheet(clazz, objects);
        }

        return workbook;
    }
}
