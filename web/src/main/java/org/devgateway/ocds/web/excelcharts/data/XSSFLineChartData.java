package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.xmlbeans.XmlObject;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;

/**
 * @author idobre
 * @since 8/12/16
 *
 * Holds data for a XSSF Line Chart
 */
public class XSSFLineChartData extends AbstarctXSSFChartData {
    protected CustomChartSeries createNewSerie(int id, int order, ChartDataSource<?> categories,
                                               ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            public void addToChart(XmlObject ctChart) {
                CTLineChart ctLineChart = (CTLineChart) ctChart;
                CTLineSer ctLineSer = ctLineChart.addNewSer();
                ctLineSer.addNewIdx().setVal(id);
                ctLineSer.addNewOrder().setVal(order);

                // No marker symbol on the chart line.
                ctLineSer.addNewMarker().addNewSymbol().setVal(STMarkerStyle.NONE);

                CTAxDataSource catDS = ctLineSer.addNewCat();
                XSSFChartUtil.buildAxDataSource(catDS, categories);

                CTNumDataSource valueDS = ctLineSer.addNewVal();
                XSSFChartUtil.buildNumDataSource(valueDS, values);

                if (isTitleSet()) {
                    ctLineSer.setTx(getCTSerTx());
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
        CTLineChart lineChart = plotArea.addNewLineChart();
        lineChart.addNewVaryColors().setVal(false);

        for (CustomChartSeries s : series) {
            s.addToChart(lineChart);
        }

        for (ChartAxis ax : axis) {
            lineChart.addNewAxId().setVal(ax.getId());
        }

        // add grid lines
        CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
        rgb.setVal(new byte[]{(byte) 0, (byte) 0, (byte) 0});

        CTCatAx[] ctCatAx = plotArea.getCatAxArray();
        if (ctCatAx.length != 0) {
            ctCatAx[0].addNewMajorGridlines().addNewSpPr().addNewSolidFill().setSrgbClr(rgb);
        }

        CTValAx[] ctValAx = plotArea.getValAxArray();
        if (ctValAx.length != 0) {
            ctValAx[0].addNewMajorGridlines().addNewSpPr().addNewSolidFill().setSrgbClr(rgb);
        }
    }
}
