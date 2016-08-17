package org.devgateway.ocds.web.excelcharts.util;

import org.devgateway.ocds.web.excelcharts.ChartType;
import org.devgateway.ocds.web.excelcharts.CustomChartData;

/**
 * @author idobre
 * @since 8/11/16
 *
 * A factory for different charts data types (like bar chart, area chart)
 */
public interface CustomChartDataFactory {
    /**
     * @return an appropriate CustomChartData instance
     */
    CustomChartData createChartData(final ChartType type, final String title);
}
