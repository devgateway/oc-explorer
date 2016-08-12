package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.xmlbeans.XmlObject;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.STBarDir;

/**
 * @author idobre
 * @since 8/8/16
 * Holds data for a XSSF Bar Chart
 */
public class XSSFBarChartData extends AbstarctXSSFChartData {
    protected CustomChartSeries createNewSerie(int id, int order, ChartDataSource<?> categories,
                                               ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            public void addToChart(XmlObject ctChart) {
                CTBarChart ctBarChart = (CTBarChart) ctChart;
                CTBarSer ctBarSer = ctBarChart.addNewSer();
                ctBarSer.addNewIdx().setVal(id);
                ctBarSer.addNewOrder().setVal(order);

                CTAxDataSource catDS = ctBarSer.addNewCat();
                XSSFChartUtil.buildAxDataSource(catDS, categories);

                CTNumDataSource valueDS = ctBarSer.addNewVal();
                XSSFChartUtil.buildNumDataSource(valueDS, values);

                if (isTitleSet()) {
                    ctBarSer.setTx(getCTSerTx());
                }
            }
        };
    }

    public void fillChart(Chart chart, ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTBarChart barChart = plotArea.addNewBarChart();

        barChart.addNewVaryColors().setVal(false);

        // set bars orientation
        barChart.addNewBarDir().setVal(STBarDir.COL);

        for (CustomChartSeries s : series) {
            s.addToChart(barChart);
        }

        for (ChartAxis ax : axis) {
            barChart.addNewAxId().setVal(ax.getId());
        }
    }
}
