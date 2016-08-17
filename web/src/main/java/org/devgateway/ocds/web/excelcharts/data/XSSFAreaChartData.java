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
 *
 * Holds data for a XSSF Area Chart.
 */
public class XSSFAreaChartData extends AbstractXSSFChartData {
    public XSSFAreaChartData(final String title) {
        super(title);
    }

    @Override
    protected CustomChartSeries createNewSerie(final int id, final int order, final ChartDataSource<String> categories,
                                               final ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            @Override
            public void addToChart(final XmlObject ctChart) {
                final CTAreaChart ctAreaChart = (CTAreaChart) ctChart;
                final CTAreaSer ctAreaSer = ctAreaChart.addNewSer();

                ctAreaSer.addNewIdx().setVal(this.id);
                ctAreaSer.addNewOrder().setVal(this.order);

                final CTAxDataSource catDS = ctAreaSer.addNewCat();
                XSSFChartUtil.buildAxDataSource(catDS, this.categories);

                final CTNumDataSource valueDS = ctAreaSer.addNewVal();
                XSSFChartUtil.buildNumDataSource(valueDS, this.values);

                if (isTitleSet()) {
                    ctAreaSer.setTx(getCTSerTx());
                }
            }
        };
    }

    @Override
    public void fillChart(final Chart chart, final ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        final XSSFChart xssfChart = (XSSFChart) chart;
        final CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        final CTAreaChart areChart = plotArea.addNewAreaChart();
        areChart.addNewVaryColors().setVal(false);

        xssfChart.setTitle(this.title);

        for (CustomChartSeries s : series) {
            s.addToChart(areChart);
        }

        for (ChartAxis ax : axis) {
            areChart.addNewAxId().setVal(ax.getId());
        }
    }
}
