package org.devgateway.toolkit.persistence.excel.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.toolkit.persistence.excel.ExcelFile;
import org.devgateway.toolkit.persistence.excel.ExcelFileDefault;
import org.devgateway.toolkit.persistence.repository.BaseJpaRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
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
@Cacheable
public class ExcelGeneratorService {
    /**
     * Method that returns a byte array with an excel export.
     *
     * @param jpaRepository - {@link JpaRepository} from where we will get the data
     * @param spec          - {@link Specification} for filtering the data
     * @param pageable      - {@link Pageable} for paginating the data
     *
     * @return byte[]
     * @throws IOException
     */
    public byte[] getExcelDownload(final BaseJpaRepository jpaRepository,
                                   final Specification spec,
                                   final Pageable pageable) throws IOException {
        final List<Object> objects = jpaRepository.findAll(spec, pageable).getContent();
        final ExcelFile excelFile = new ExcelFileDefault(objects);
        final Workbook workbook = excelFile.createWorkbook();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        final byte[] bytes = baos.toByteArray();

        return bytes;
    }

    /**
     * Return the number of records for this {@link Specification}.
     *
     * @param jpaRepository
     * @param spec
     * @return count
     */
    public long count(final BaseJpaRepository jpaRepository,
                      final Specification spec) {
        return jpaRepository.count(spec);
    }
}
