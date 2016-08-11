package org.devgateway.ocds.web.excelcharts.piechart;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.charts.AbstractXSSFChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 * Holds data for a XSSF Pie Chart
 */
public class XSSFPieChartData implements PieChartData {
    /**
     * List of all data series.
     */
    private List<Series> series;

    public XSSFPieChartData() {
        series = new ArrayList<Series>();
    }

    static class Series extends AbstractXSSFChartSeries implements PieChartSeries {
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

        protected void addToChart(CTPieChart ctPieChart) {
            CTPieSer ctPieSer = ctPieChart.addNewSer();
            ctPieSer.addNewIdx().setVal(id);
            ctPieSer.addNewOrder().setVal(order);

            CTAxDataSource catDS = ctPieSer.addNewCat();
            XSSFChartUtil.buildAxDataSource(catDS, categories);

            CTNumDataSource valueDS = ctPieSer.addNewVal();
            XSSFChartUtil.buildNumDataSource(valueDS, values);

            if (isTitleSet()) {
                ctPieSer.setTx(getCTSerTx());
            }
        }
    }

    public PieChartSeries addSeries(ChartDataSource<?> categoryAxisData, ChartDataSource<? extends Number> values) {
        if (!values.isNumeric()) {
            throw new IllegalArgumentException("Value data source must be numeric.");
        }
        int numOfSeries = series.size();
        XSSFPieChartData.Series newSeries =
                new XSSFPieChartData.Series(numOfSeries, numOfSeries, categoryAxisData, values);
        series.add(newSeries);
        return newSeries;
    }

    public List<? extends PieChartSeries> getSeries() {
        return series;
    }

    public void fillChart(Chart chart, ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTPieChart pieChart = plotArea.addNewPieChart();
        pieChart.addNewVaryColors().setVal(true);

        for (XSSFPieChartData.Series s : series) {
            s.addToChart(pieChart);
        }
    }
}
