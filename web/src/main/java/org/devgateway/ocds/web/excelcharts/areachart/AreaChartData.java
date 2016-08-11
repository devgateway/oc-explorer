package org.devgateway.ocds.web.excelcharts.areachart;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Data for a Area Chart
 */
public interface AreaChartData extends ChartData {

    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new area chart serie.
     */
    AreaChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends AreaChartSeries> getSeries();
}
