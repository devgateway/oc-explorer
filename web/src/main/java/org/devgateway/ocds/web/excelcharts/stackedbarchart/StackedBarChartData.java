package org.devgateway.ocds.web.excelcharts.stackedbarchart;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Data for a Stacked Bar Chart
 */
public interface StackedBarChartData extends ChartData {

    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new stacked bar chart serie.
     */
    StackedBarChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends StackedBarChartSeries> getSeries();
}
