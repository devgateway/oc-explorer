package org.devgateway.ocds.persistence.mongo.excel;

import org.devgateway.ocds.persistence.mongo.ReleasePackage;
import org.devgateway.ocds.persistence.mongo.spring.json2object.JsonToObject;
import org.devgateway.ocds.persistence.mongo.spring.json2object.ReleaseJsonToObject;
import org.devgateway.ocds.persistence.mongo.spring.json2object.ReleasePackageJsonToObject;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * @author idobre
 * @since 6/7/16
 */
public class ReleaseExportFileTest {
    @Test
    public void createWorkbook() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("json/release-excel-export.json").getFile());
        JsonToObject releasePackageJsonToObject = new ReleasePackageJsonToObject(file);

        ReleasePackage releasePackage = (ReleasePackage) releasePackageJsonToObject.toObject();

        ExcelFile releaseExcelFile = new ReleaseExportFile(new ArrayList<>(releasePackage.getReleases()));
        // ExcelFile releaseExcelFile = new ReleaseExportFile(null);
        // try (FileOutputStream outputStream = new FileOutputStream("/Users/ionut/Downloads/ocds-export.xlsx")) {
        //     releaseExcelFile.createWorkbook().write(outputStream);
        // }

    }
}
