package org.devgateway.ocds.web.excelcharts;

import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartSeries;

/**
 * @author idobre
 * @since 8/12/16
 *
 * Represents a chart series.
 */
public interface CustomChartSeries extends ChartSeries {
    /**
     * @return data source used for category axis data.
     */
    ChartDataSource<?> getCategoryAxisData();

    /**
     * @return data source used for value axis.
     */
    ChartDataSource<? extends Number> getValues();
}
