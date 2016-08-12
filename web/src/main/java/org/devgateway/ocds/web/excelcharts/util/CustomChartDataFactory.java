package org.devgateway.ocds.web.excelcharts.util;

import org.devgateway.ocds.web.excelcharts.CustomChartData;

/**
 * @author idobre
 * @since 8/11/16
 *
 * A factory for different charts data types (like bar chart, area chart)
 */
public interface CustomChartDataFactory {
    /**
     * @return an appropriate ScatterChartData instance
     */
    CustomChartData createScatterChartData();

    /**
     * @return a LineChartData instance
     */
    CustomChartData createLineChartData();

    /**
     * @return a AreaChartData instance
     */
    CustomChartData createAreaChartData();

    /**
     * @return a BarChartData instance
     */
    CustomChartData createBarChartData();

    /**
     * @return a PieChartData instance
     */
    CustomChartData createPieChartData();

    /**
     * @return a StackedBarChartData instance
     */
    CustomChartData createStackedBarChartData();
}
