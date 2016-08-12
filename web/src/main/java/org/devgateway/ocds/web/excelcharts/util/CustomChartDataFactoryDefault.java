package org.devgateway.ocds.web.excelcharts.util;

import org.devgateway.ocds.web.excelcharts.CustomChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFAreaChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFBarChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFLineChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFPieChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFScatterChartData;
import org.devgateway.ocds.web.excelcharts.data.XSSFStackedBarChartData;

/**
 * @author idobre
 * @since 8/11/16
 */
public class CustomChartDataFactoryDefault implements CustomChartDataFactory {
    /**
     * @return new scatter charts data instance
     */
    public CustomChartData createScatterChartData() {
        return new XSSFScatterChartData();
    }

    /**
     * @return new line charts data instance
     */
    public CustomChartData createLineChartData() {
        return new XSSFLineChartData();
    }

    /**
     * @return new area charts data instance
     */
    public CustomChartData createAreaChartData() {
        return new XSSFAreaChartData();
    }

    /**
     * @return new bar charts data instance
     */
    public CustomChartData createBarChartData() {
        return new XSSFBarChartData();
    }

    /**
     * @return new pie charts data instance
     */
    public CustomChartData createPieChartData() {
        return new XSSFPieChartData();
    }

    /**
     * @return new stacked bar charts data instance
     */
    public CustomChartData createStackedBarChartData() {
        return new XSSFStackedBarChartData();
    }
}
