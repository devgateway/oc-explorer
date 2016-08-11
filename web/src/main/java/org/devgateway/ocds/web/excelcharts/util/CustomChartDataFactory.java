package org.devgateway.ocds.web.excelcharts.util;

import org.devgateway.ocds.web.excelcharts.areachart.AreaChartData;
import org.devgateway.ocds.web.excelcharts.barchart.BarChartData;
import org.devgateway.ocds.web.excelcharts.piechart.PieChartData;
import org.devgateway.ocds.web.excelcharts.stackedbarchart.StackedBarChartData;

/**
 * @author idobre
 * @since 8/11/16
 *
 * A factory for different charts data types (like bar chart, area chart)
 */
public interface CustomChartDataFactory {
    /**
     * @return a AreaChartData instance
     */
    AreaChartData createAreaChartData();

    /**
     * @return a BarChartData instance
     */
    BarChartData createBarChartData();

    /**
     * @return a PieChartData instance
     */
    PieChartData createPieChartData();

    /**
     * @return a StackedBarChartData instance
     */
    StackedBarChartData createStackedBarChartData();
}
