package org.devgateway.ocds.web.rest.controller.excelchart;

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.AverageTenderAndAwardPeriodsController;
import org.devgateway.ocds.web.rest.controller.request.LangYearFilterPagingRequest;
import org.devgateway.toolkit.web.excelcharts.ChartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 8/22/16
 *
 * Exports an excel chart based on *Bid Timeline* dashboard
 */
@RestController
public class AverageTenderAndAwardsExcelController extends ExcelChartOCDSController {
    @Autowired
    private ExcelChartGenerator excelChartGenerator;

    @Autowired
    private ExcelChartHelper excelChartHelper;

    @Autowired
    private AverageTenderAndAwardPeriodsController averageTenderAndAwardPeriodsController;

    @ApiOperation(value = "Exports *Bid Timeline* dashboard in Excel format.")
    @RequestMapping(value = "/api/ocds/bidTimelineExcelChart", method = {RequestMethod.GET, RequestMethod.POST})
    public void bidTimelineExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
                                      final HttpServletResponse response) throws IOException {
        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:bidPeriod:title");

        // fetch the data that will be displayed in the chart (we have multiple sources for this dashboard)
        final List<Document> averageAwardPeriod = averageTenderAndAwardPeriodsController.averageAwardPeriod(filter);
        final List<Document> averageTenderPeriod = averageTenderAndAwardPeriodsController.averageTenderPeriod(filter);

        final List<?> categories = excelChartHelper.getCategoriesFromDBObject(getExportYearMonthXAxis(filter),
                averageTenderPeriod, averageAwardPeriod);
        final List<List<? extends Number>> values = new ArrayList<>();

        final List<Number> valueTenders = excelChartHelper.getValuesFromDBObject(averageTenderPeriod, categories,
                getExportYearMonthXAxis(filter), AverageTenderAndAwardPeriodsController.Keys.AVERAGE_TENDER_DAYS);
        final List<Number> valueAwards = excelChartHelper.getValuesFromDBObject(averageAwardPeriod, categories,
                getExportYearMonthXAxis(filter), AverageTenderAndAwardPeriodsController.Keys.AVERAGE_AWARD_DAYS);
        if (!valueTenders.isEmpty()) {
            values.add(valueTenders);
        }
        if (!valueAwards.isEmpty()) {
            values.add(valueAwards);
        }

        // check if we have anything to display before setting the *seriesTitle*.
        final List<String> seriesTitle;
        if (!values.isEmpty()) {
            seriesTitle = Arrays.asList(
                    translationService.getValue(filter.getLanguage(), "charts:bidPeriod:traces:tender"),
                    translationService.getValue(filter.getLanguage(), "charts:bidPeriod:traces:award"));
        } else {
            seriesTitle = new ArrayList<>();
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + chartTitle + ".xlsx");
        response.getOutputStream().write(
                excelChartGenerator.getExcelChart(
                        ChartType.stackedbar,
                        chartTitle,
                        seriesTitle,
                        categories, values));
    }
}
