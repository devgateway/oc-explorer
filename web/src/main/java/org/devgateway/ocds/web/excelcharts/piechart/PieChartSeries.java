package org.devgateway.ocds.web.excelcharts.piechart;

import org.apache.poi.ss.usermodel.charts.ChartDataSource;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Represents a pie chart series.
 */
public interface PieChartSeries {
    /**
     * @return data source used for category axis data.
     */
    ChartDataSource<?> getCategoryAxisData();

    /**
     * @return data source used for value axis.
     */
    ChartDataSource<? extends Number> getValues();
}
