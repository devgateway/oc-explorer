package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.xmlbeans.XmlObject;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

/**
 * @author idobre
 * @since 8/8/16
 * Holds data for a XSSF Area Chart
 */
public class XSSFAreaChartData extends AbstarctXSSFChartData {

    @Override
    protected CustomChartSeries createNewSerie(final int id, final int order, final ChartDataSource<?> categories,
                                               final ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            public void addToChart(final XmlObject ctChart) {
                CTAreaChart ctAreaChart = (CTAreaChart) ctChart;
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
        };
    }

    public void fillChart(final Chart chart, final ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTAreaChart areChart = plotArea.addNewAreaChart();
        areChart.addNewVaryColors().setVal(false);

        for (CustomChartSeries s : series) {
            s.addToChart(areChart);
        }

        for (ChartAxis ax : axis) {
            areChart.addNewAxId().setVal(ax.getId());
        }
    }
}
