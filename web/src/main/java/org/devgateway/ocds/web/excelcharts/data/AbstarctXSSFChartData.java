package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.devgateway.ocds.web.excelcharts.CustomChartData;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 8/12/16
 */
public abstract class AbstarctXSSFChartData implements CustomChartData {
    /**
     * List of all data series.
     */
    protected List<CustomChartSeries> series;

    public AbstarctXSSFChartData() {
        series = new ArrayList<CustomChartSeries>();
    }


    public CustomChartSeries addSeries(ChartDataSource<?> categoryAxisData, ChartDataSource<? extends Number> values) {
        if (!values.isNumeric()) {
            throw new IllegalArgumentException("Value data source must be numeric.");
        }

        int numOfSeries = series.size();
        CustomChartSeries newSeries = createNewSerie(numOfSeries, numOfSeries, categoryAxisData, values);
        series.add(newSeries);
        return newSeries;
    }

    public List<? extends CustomChartSeries> getSeries() {
        return series;
    }

    protected abstract CustomChartSeries createNewSerie(int id,
                                                        int order,
                                                        ChartDataSource<?> categories,
                                                        ChartDataSource<? extends Number> values);

    public abstract void fillChart(Chart chart, ChartAxis... axis);
}
