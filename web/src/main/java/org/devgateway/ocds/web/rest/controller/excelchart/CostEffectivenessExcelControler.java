package org.devgateway.ocds.web.rest.controller.excelchart;

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.excelcharts.ChartType;
import org.devgateway.ocds.web.rest.controller.CostEffectivenessVisualsController;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.GroupingFilterPagingRequest;
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
 * @since 8/19/16
 */
@RestController
public class CostEffectivenessExcelControler extends GenericOCDSController {
    @Autowired
    private ExcelChartGenerator excelChartGenerator;

    @Autowired
    private ExcelChartHelper excelChartHelper;

    @Autowired
    private CostEffectivenessVisualsController costEffectivenessVisualsController;

    @ApiOperation(value = "Exports *Cost effectiveness* dashboard in Excel format.")
    @RequestMapping(value = "/api/ocds/costEffectivenessExcelChart", method = {RequestMethod.GET, RequestMethod.POST})
    public void excelExport(@ModelAttribute @Valid final GroupingFilterPagingRequest filter,
                            HttpServletResponse response) throws IOException {
        final String chartTitle = "cost effectiveness";

        // fetch the data that will be displayed in the chart (we have multiple sources for this dashboard)
        final List<DBObject> costEffectivenessAwardAmount =
                costEffectivenessVisualsController.costEffectivenessAwardAmount(filter);
        final List<DBObject> costEffectivenessTenderAmount =
                costEffectivenessVisualsController.costEffectivenessTenderAmount(filter);

        final List<String> categories = excelChartHelper.getCategoriesFromDBObject("_id",
                costEffectivenessAwardAmount, costEffectivenessTenderAmount);
        final List<List<? extends Number>> values = new ArrayList<>();

        final List<Number> tenderPrice = excelChartHelper.getValuesFromDBObject(costEffectivenessTenderAmount,
                categories, "_id", "totalTenderAmount");
        final List<Number> awardPrice = excelChartHelper.getValuesFromDBObject(costEffectivenessAwardAmount,
                categories, "_id", "totalAwardAmount");

        // calculate the difference
        final List<Number> diffPrice = new ArrayList<>();
        for (int i = 0; i < tenderPrice.size(); i++) {
            diffPrice.add((double) tenderPrice.get(i) - (double) awardPrice.get(i));
        }
        values.add(tenderPrice);
        values.add(diffPrice);

        final List<String> seriesTitle = Arrays.asList(
                "Bid price",
                "Difference"
        );

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + chartTitle + ".xlsx");

        response.getOutputStream().write(
                excelChartGenerator.getExcelChart(
                        ChartType.stacked,
                        chartTitle,
                        seriesTitle,
                        categories, values));
    }
}
