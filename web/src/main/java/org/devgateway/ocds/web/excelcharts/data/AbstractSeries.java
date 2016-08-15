package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.charts.AbstractXSSFChartSeries;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;

/**
 * @author idobre
 * @since 8/12/16
 */
public abstract class AbstractSeries extends AbstractXSSFChartSeries implements CustomChartSeries {
    protected int id;

    protected int order;

    protected ChartDataSource<?> categories;

    protected ChartDataSource<? extends Number> values;

    public AbstractSeries(final int id,
                          final int order,
                          final ChartDataSource<?> categories,
                          final ChartDataSource<? extends Number> values) {
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
}
