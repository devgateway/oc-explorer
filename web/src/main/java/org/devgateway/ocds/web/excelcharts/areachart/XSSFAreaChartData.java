package org.devgateway.ocds.web.excelcharts.areachart;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.charts.AbstractXSSFChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 * Holds data for a XSSF Area Chart
 */
public class XSSFAreaChartData implements AreaChartData {
    /**
     * List of all data series.
     */
    private List<Series> series;

    public XSSFAreaChartData() {
        series = new ArrayList<Series>();
    }

    static class Series extends AbstractXSSFChartSeries implements AreaChartSeries {
        private int id;
        private int order;
        private ChartDataSource<?> categories;
        private ChartDataSource<? extends Number> values;

        protected Series(int id, int order,
                         ChartDataSource<?> categories,
                         ChartDataSource<? extends Number> values) {
            this.id = id;
            this.order = order;
            this.categories = categories;
            this.values = values;
        }

        public ChartDataSource<?> getCategoryAxisData() {
            return categories;
        }

        public ChartDataSource<? extends Number> getValues() {
            return values;
        }

        protected void addToChart(CTAreaChart ctAreaChart) {
            CTAreaSer ctAreaSer = ctAreaChart.addNewSer();
            ctAreaSer.addNewIdx().setVal(id);
            ctAreaSer.addNewOrder().setVal(order);

            CTAxDataSource catDS = ctAreaSer.addNewCat();
            XSSFChartUtil.buildAxDataSource(catDS, categories);

            CTNumDataSource valueDS = ctAreaSer.addNewVal();
            XSSFChartUtil.buildNumDataSource(valueDS, values);

            if (isTitleSet()) {
                ctAreaSer.setTx(getCTSerTx());
            }
        }
    }

    public AreaChartSeries addSeries(ChartDataSource<?> categoryAxisData, ChartDataSource<? extends Number> values) {
        if (!values.isNumeric()) {
            throw new IllegalArgumentException("Value data source must be numeric.");
        }
        int numOfSeries = series.size();
        XSSFAreaChartData.Series newSeries =
                new XSSFAreaChartData.Series(numOfSeries, numOfSeries, categoryAxisData, values);
        series.add(newSeries);
        return newSeries;
    }

    public List<? extends AreaChartSeries> getSeries() {
        return series;
    }

    public void fillChart(Chart chart, ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTAreaChart areChart = plotArea.addNewAreaChart();
        areChart.addNewVaryColors().setVal(false);

        for (XSSFAreaChartData.Series s : series) {
            s.addToChart(areChart);
        }

        for (ChartAxis ax : axis) {
            areChart.addNewAxId().setVal(ax.getId());
        }
    }
}
