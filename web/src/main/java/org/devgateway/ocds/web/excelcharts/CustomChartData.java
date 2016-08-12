package org.devgateway.ocds.web.excelcharts;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/12/16
 *
 * Data for a Chart
 */
public interface CustomChartData extends ChartData {
    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new chart serie.
     */
    CustomChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends CustomChartSeries> getSeries();
}
