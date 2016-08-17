package org.devgateway.ocds.web.excelcharts;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * @author idobre
 * @since 8/16/16
 *
 * Sheet used to export Dashboards.
 */
public interface ExcelChartSheet {
    void writeCell(final Object value, final Row row, final int column);

    Row createRow(final int rowNumber);

    Row createRow();

    Chart createChartAndLegend();

    ChartDataSource getCategoryChartDataSource();

    List<ChartDataSource<Number>> getValuesChartDataSource();
}
