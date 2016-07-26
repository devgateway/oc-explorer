package org.devgateway.ocds.web.rest.controller.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.excel.ExcelFile;
import org.devgateway.ocds.persistence.mongo.excel.ReleaseExportFile;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author idobre
 * @since 7/23/16
 */
@RestController
public class ExcelExportController extends GenericOCDSController {
    protected final Logger logger = LoggerFactory.getLogger(GenericOCDSController.class);

    @Autowired
    private ReleaseRepository releaseRepository;

    @RequestMapping(value = "/api/ocds/excelExport", method = {RequestMethod.GET, RequestMethod.POST})
    // @Cacheable("excelExport")
    public void excelExport(HttpServletResponse response) throws IOException {
        List<Release> releases = releaseRepository.findAll(new PageRequest(0, 1000)).getContent();

        ExcelFile releaseExcelFile = new ReleaseExportFile(releases);
        Workbook workbook = releaseExcelFile.createWorkbook();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + "excel-export.xlsx");

        workbook.write(response.getOutputStream());
    }
}
