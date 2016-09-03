package org.devgateway.ocds.web.rest.controller.excelchart;

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.excelcharts.ChartType;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.TenderPriceByTypeYearController;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Fields;
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
 * Exports an excel chart based on *Procurement method* dashboard
 */
@RestController
public class TenderPriceExcelController extends GenericOCDSController {
    @Autowired
    private ExcelChartGenerator excelChartGenerator;

    @Autowired
    private ExcelChartHelper excelChartHelper;

    @Autowired
    private TenderPriceByTypeYearController tenderPriceByTypeYearController;

    @ApiOperation(value = "Exports *Procurement method* dashboard in Excel format.")
    @RequestMapping(value = "/api/ocds/procurementMethodExcelChart", method = {RequestMethod.GET, RequestMethod.POST})
    public void procurementMethodExcelChart(@ModelAttribute @Valid final YearFilterPagingRequest filter,
                                       final HttpServletResponse response) throws IOException {
        final String chartTitle = "Procurement method";

        // fetch the data that will be displayed in the chart
        final List<DBObject> tenderPriceByBidSelection =
                tenderPriceByTypeYearController.tenderPriceByProcurementMethod(filter);

        final List<?> categories = excelChartHelper.getCategoriesFromDBObject(
                TenderPriceByTypeYearController.Keys.PROCUREMENT_METHOD, tenderPriceByBidSelection);

        final List<List<? extends Number>> values = new ArrayList<>();
        final List<Number> totalTenderAmount = excelChartHelper.getValuesFromDBObject(tenderPriceByBidSelection,
                categories, TenderPriceByTypeYearController.Keys.PROCUREMENT_METHOD,
                TenderPriceByTypeYearController.Keys.TOTAL_TENDER_AMOUNT);
        values.add(totalTenderAmount);

        final List<String> seriesTitle = Arrays.asList(
                "Procurement method");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + chartTitle + ".xlsx");
        response.getOutputStream().write(
                excelChartGenerator.getExcelChart(
                        ChartType.barcol,
                        chartTitle,
                        seriesTitle,
                        categories, values));
    }
}
