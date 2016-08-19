package org.devgateway.ocds.web.excelcharts;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.ocds.web.excelcharts.util.CustomChartDataFactory;
import org.devgateway.ocds.web.excelcharts.util.CustomChartDataFactoryDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 8/16/16
 *
 * Class that returns Workbook with a chart based on categories/values provided.
 */
public class ExcelChartDefault implements ExcelChart {
    private final ChartType type;

    private final String title;

    private final List<String> categories;

    private final List<List<? extends Number>> values;

    private final Workbook workbook;

    private final List<String> seriesTitle;

    public ExcelChartDefault(final String title,
                             final ChartType type,
                             final List<String> categories,
                             final List<List<? extends Number>> values) {
        for (List<? extends Number> value : values) {
            if (categories.size() != value.size()) {
                throw new IllegalArgumentException("categories and value size should match!");
            }
        }

        this.title = title;
        this.type = type;
        this.categories = categories;
        this.values = values;
        this.workbook = new XSSFWorkbook();
        this.seriesTitle = new ArrayList<>();
    }

    @Override
    public Workbook createWorkbook() {
        final ExcelChartSheet excelChartSheet = new ExcelChartSheetDefault(workbook, type.toString());
        final Chart chart = excelChartSheet.createChartAndLegend();

        addCategories(excelChartSheet);
        addValues(excelChartSheet);

        final CustomChartDataFactory customChartDataFactory = new CustomChartDataFactoryDefault();
        final CustomChartData chartData = customChartDataFactory.createChartData(type, title);

        final ChartDataSource<String> categoryDataSource = excelChartSheet.getCategoryChartDataSource();
        final List<ChartDataSource<Number>> valuesDataSource = excelChartSheet.getValuesChartDataSource();
        for (int i = 0; i < valuesDataSource.size(); i++) {
            final ChartDataSource<Number> valueDataSource = valuesDataSource.get(i);
            if (seriesTitle.isEmpty()) {
                chartData.addSeries(categoryDataSource, valueDataSource);
            } else {
                chartData.addSeries(seriesTitle.get(i), categoryDataSource, valueDataSource);
            }
        }

        // we don't have any axis for a pie chart
        if (type.equals(ChartType.pie)) {
            chart.plot(chartData);
        } else {
            // Use a category axis for the bottom axis.
            final ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
            final ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            chart.plot(chartData, bottomAxis, leftAxis);
        }

        return workbook;
    }

    @Override
    public void configureSeriesTitle(final List<String> seriesTitle) {
        if (values.size() != seriesTitle.size()) {
            throw new IllegalArgumentException("seriesTitle and values size should match!");
        }
        this.seriesTitle.addAll(seriesTitle);
    }

    /**
     * Add a row with the categories.
     */
    private void addCategories(final ExcelChartSheet excelChartSheet) {
        final Row row = excelChartSheet.createRow();
        int coll = 0;
        for (String category : categories) {
            excelChartSheet.writeCell(category, row, coll);
            excelChartSheet.setColumnWidth(coll, 3500);
            coll++;
        }
    }

    /**
     * Add one or multiple rows with the values.
     */
    private void addValues(final ExcelChartSheet excelChartSheet) {
        for (List<? extends Number> value : values) {
            final Row row = excelChartSheet.createRow();
            int coll = 0;
            for (Number val : value) {
                excelChartSheet.writeCell(val, row, coll++);
            }
        }
    }
}
