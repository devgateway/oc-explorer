package org.devgateway.ocds.web.rest.controller.export;

import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.excel.ExcelFile;
import org.devgateway.ocds.persistence.mongo.excel.ReleaseExportFile;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author idobre
 * @since 7/23/16
 */
@RestController
public class ExcelExportController {
    protected final Logger logger = LoggerFactory.getLogger(ExcelExportController.class);

    @Autowired
    ExcelGenerator excelGenerator;

    @ApiOperation(value = "Export releases in Excel format.")
    @RequestMapping(value = "/api/ocds/excelExport", method = {RequestMethod.GET, RequestMethod.POST})
    public void excelExport(@ModelAttribute @Valid final YearFilterPagingRequest filter,
                            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + "excel-export.xlsx");

        response.getOutputStream().write(excelGenerator.getExcelDownload(filter));
    }
}
