package org.devgateway.toolkit.persistence.excel.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.toolkit.persistence.excel.ExcelFile;
import org.devgateway.toolkit.persistence.excel.ExcelFileDefault;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author idobre
 * @since 15/11/2017
 */
@Service
@CacheConfig(keyGenerator = "genericExcelKeyGenerator", cacheNames = "excelExportCache")
public class ExcelGeneratorService {
    @Autowired(required = false)
    private TranslateService translateService;

    /**
     * Method that returns a byte array with an excel export.
     *
     * @param jpaService - {@link BaseJpaService} from where we will get the data
     * @param spec       - {@link Specification} for filtering the data
     * @param pageable   - {@link Pageable} for paginating the data
     * @return byte[]
     * @throws IOException
     */
    @Cacheable
    public byte[] getExcelDownload(final BaseJpaService jpaService,
                                   final Specification spec,
                                   final Pageable pageable) throws IOException {
        final List<Object> objects = jpaService.findAll(spec, pageable).getContent();
        final ExcelFile excelFile = new ExcelFileDefault(objects, translateService);
        final Workbook workbook = excelFile.createWorkbook();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        final byte[] bytes = baos.toByteArray();

        return bytes;
    }

    /**
     * Method that returns a byte array with an excel export.
     *
     * @param objects - entities that will be exported to excel file.
     * @return byte[]
     * @throws IOException
     */
    public byte[] getExcelDownload(final List<Object> objects) throws IOException {
        final ExcelFile excelFile = new ExcelFileDefault(objects, translateService);
        final Workbook workbook = excelFile.createWorkbook();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        final byte[] bytes = baos.toByteArray();

        return bytes;
    }

    /**
     * Return the number of records for this {@link Specification}.
     *
     * @param jpaService
     * @param spec
     * @return count
     */
    @Cacheable
    public long count(final BaseJpaService jpaService,
                      final Specification spec) {
        return jpaService.count(spec);
    }
}
