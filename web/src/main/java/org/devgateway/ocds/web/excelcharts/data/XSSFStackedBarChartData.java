package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.STBarDir;
import org.openxmlformats.schemas.drawingml.x2006.chart.STBarGrouping;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Holds data for a XSSF Stacked Bar Chart.
 */
public class XSSFStackedBarChartData extends XSSFBarChartData {
    public XSSFStackedBarChartData(final String title) {
        super(title);
    }

    @Override
    public void fillChart(final Chart chart, final ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        final XSSFChart xssfChart = (XSSFChart) chart;
        final CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        final CTBarChart barChart = plotArea.addNewBarChart();

        // create a stacked bar
        barChart.addNewGrouping().setVal(STBarGrouping.PERCENT_STACKED);
        barChart.addNewOverlap().setVal((byte) 100);

        barChart.addNewVaryColors().setVal(false);

        // set bars orientation
        barChart.addNewBarDir().setVal(STBarDir.COL);

        xssfChart.setTitle(this.title);

        for (CustomChartSeries s : series) {
            s.addToChart(barChart);
        }

        for (ChartAxis ax : axis) {
            barChart.addNewAxId().setVal(ax.getId());
        }
    }
}
