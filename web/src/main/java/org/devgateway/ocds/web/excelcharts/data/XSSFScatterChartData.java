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
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.STScatterStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;


/**
 * @author idobre
 * @since 8/12/16
 *
 * Holds data for a XSSF Scatter Chart
 */
public class XSSFScatterChartData extends AbstarctXSSFChartData {
    @Override
    protected CustomChartSeries createNewSerie(final int id, final int order, final ChartDataSource<?> categories,
                                               final ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            public void addToChart(final XmlObject ctChart) {
                CTScatterChart ctScatterChart = (CTScatterChart) ctChart;
                CTScatterSer scatterSer = ctScatterChart.addNewSer();

                scatterSer.addNewIdx().setVal(this.id);
                scatterSer.addNewOrder().setVal(this.order);

                CTAxDataSource catDS = scatterSer.addNewXVal();
                XSSFChartUtil.buildAxDataSource(catDS, categories);

                CTNumDataSource valueDS = scatterSer.addNewYVal();
                XSSFChartUtil.buildNumDataSource(valueDS, values);

                if (isTitleSet()) {
                    scatterSer.setTx(getCTSerTx());
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
        CTScatterChart scatterChart = plotArea.addNewScatterChart();
        addStyle(scatterChart);

        for (CustomChartSeries s : series) {
            s.addToChart(scatterChart);
        }

        for (ChartAxis ax : axis) {
            scatterChart.addNewAxId().setVal(ax.getId());
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

    private void addStyle(final CTScatterChart ctScatterChart) {
        CTScatterStyle scatterStyle = ctScatterChart.addNewScatterStyle();
        scatterStyle.setVal(STScatterStyle.LINE_MARKER);
    }
}
