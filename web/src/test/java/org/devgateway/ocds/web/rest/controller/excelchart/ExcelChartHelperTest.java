package org.devgateway.ocds.web.rest.controller.excelchart;

import org.bson.Document;
import org.devgateway.toolkit.web.AbstractWebTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 9/14/16
 */
public class ExcelChartHelperTest extends AbstractWebTest {
    private static Logger logger = LoggerFactory.getLogger(ExcelChartHelperTest.class);

//    @Autowired
//    private CacheManager cacheManager;

//    @Before
//    public final void setUp() throws Exception {
//        // clean the cache (we need this especially for endpoints cache)
//        if (cacheManager != null) {
//            cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
//        }
//    }

    @Test
    public void getCategoriesValuesFromDBObject() throws Exception {
        final ExcelChartHelper excelChartHelper = new ExcelChartHelper();
        final List<Document> testDBObject = new ArrayList<>();
        testDBObject.add(Document.parse("{'yearTest': 2015, 'amountTest': 1000}"));
        testDBObject.add(Document.parse("{'yearTest': 2016, 'amountTest': 2000}"));
        testDBObject.add(Document.parse("{'yearTest': 2017}"));
        testDBObject.add(Document.parse("{'amountTest': 3000}"));


        final List<?> categories = excelChartHelper.getCategoriesFromDBObject("yearTest", testDBObject);
        Assert.assertArrayEquals(new Integer[] {2015, 2016, 2017},
                categories.toArray());

        final List<Number> values = excelChartHelper.getValuesFromDBObject(testDBObject,
                categories, "yearTest", "amountTest");
        Assert.assertArrayEquals(new Integer[] {1000, 2000, null},
                values.toArray());
    }
}
