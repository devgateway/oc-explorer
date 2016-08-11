package org.devgateway.ocds.web.excelcharts.piechart;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Data for a Pie Chart
 */
public interface PieChartData extends ChartData {

    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new pie chart serie.
     */
    PieChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends PieChartSeries> getSeries();
}
