package org.devgateway.ocds.web.rest.controller.excelchart;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 9/14/16
 */
public class ExcelChartHelperTest {
    private static Logger logger = Logger.getLogger(ExcelChartHelperTest.class);

    @Test
    public void getCategoriesValuesFromDBObject() throws Exception {
        final ExcelChartHelper excelChartHelper = new ExcelChartHelper();
        final List<DBObject> testDBObject = new ArrayList<>();
        testDBObject.add((DBObject) JSON.parse("{'yearTest': 2015, 'amountTest': 1000}"));
        testDBObject.add((DBObject) JSON.parse("{'yearTest': 2016, 'amountTest': 2000}"));
        testDBObject.add((DBObject) JSON.parse("{'yearTest': 2017}"));
        testDBObject.add((DBObject) JSON.parse("{'amountTest': 3000}"));


        final List<?> categories = excelChartHelper.getCategoriesFromDBObject("yearTest", testDBObject);
        Assert.assertArrayEquals(new Integer[] {2015, 2016, 2017},
                categories.toArray());

        final List<Number> values = excelChartHelper.getValuesFromDBObject(testDBObject,
                categories, "yearTest", "amountTest");
        Assert.assertArrayEquals(new Integer[] {1000, 2000, null},
                values.toArray());
    }
}
