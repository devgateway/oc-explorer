package org.devgateway.ocds.web.rest.controller.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.excel.ExcelFile;
import org.devgateway.ocds.persistence.mongo.excel.ReleaseExportFile;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author idobre
 * @since 7/29/16
 */
@Service
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "excelExport")
public class ExcelGenerator extends GenericOCDSController {

    /**
     * Method that returns a byte array with excel export.
     *
     * @param filter
     * @return
     * @throws IOException
     */
    @Cacheable
    public byte[] getExcelDownload(final YearFilterPagingRequest filter) throws IOException {
        // export only first 10000 releases
        PageRequest pageRequest = new PageRequest(0, 10000, Sort.Direction.ASC, "id");

        List<Release> releases = mongoTemplate
                .find(query(getYearFilterCriteria("tender.tenderPeriod.startDate", filter)
                        .andOperator(getDefaultFilterCriteria(filter)))
                        .with(pageRequest), Release.class);

        ExcelFile releaseExcelFile = new ReleaseExportFile(releases);
        Workbook workbook = releaseExcelFile.createWorkbook();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] bytes = baos.toByteArray();

        return bytes;
    }
}
