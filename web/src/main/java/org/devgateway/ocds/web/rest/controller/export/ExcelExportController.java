package org.devgateway.ocds.web.rest.controller.export;

import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author idobre
 * @since 7/23/16
 */
@RestController
public class ExcelExportController {
    protected final Logger logger = LoggerFactory.getLogger(ExcelExportController.class);

    @Autowired
    private ExcelGenerator excelGenerator;

    @ApiOperation(value = "Export releases in Excel format.")
    @RequestMapping(value = "/api/ocds/excelExport", method = {RequestMethod.GET, RequestMethod.POST})
    public void excelExport(@ModelAttribute @Valid final YearFilterPagingRequest filter,
                            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + "excel-export.xlsx");

        response.getOutputStream().write(excelGenerator.getExcelDownload(filter));
    }
}
