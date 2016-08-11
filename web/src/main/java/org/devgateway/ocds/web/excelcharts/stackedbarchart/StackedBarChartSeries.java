package org.devgateway.ocds.web.excelcharts.stackedbarchart;

import org.apache.poi.ss.usermodel.charts.ChartDataSource;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Represents a stacked bar chart series.
 */
public interface StackedBarChartSeries {
    /**
     * @return data source used for category axis data.
     */
    ChartDataSource<?> getCategoryAxisData();

    /**
     * @return data source used for value axis.
     */
    ChartDataSource<? extends Number> getValues();
}
