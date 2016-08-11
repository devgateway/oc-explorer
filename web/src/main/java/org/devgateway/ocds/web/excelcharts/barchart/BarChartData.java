package org.devgateway.ocds.web.excelcharts.barchart;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Data for a Bar Chart
 */
public interface BarChartData extends ChartData {

    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new bar chart serie.
     */
    BarChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends BarChartSeries> getSeries();
}
