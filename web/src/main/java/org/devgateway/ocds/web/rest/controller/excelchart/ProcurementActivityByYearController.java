package org.devgateway.ocds.web.rest.controller.excelchart;

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.excelcharts.ChartType;
import org.devgateway.ocds.web.rest.controller.CountPlansTendersAwardsController;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 8/17/16
 *
 * Exports an excel chart based on *Procurement activity by year* dashboard
 */
@RestController
public class ProcurementActivityByYearController extends GenericOCDSController {
    @Autowired
    private ExcelChartGenerator excelChartGenerator;

    @Autowired
    private CountPlansTendersAwardsController countPlansTendersAwardsController;

    @ApiOperation(value = "Exports *Procurement activity by year* dashboard in Excel format.")
    @RequestMapping(value = "/api/ocds/procurementActivityExcelChart", method = {RequestMethod.GET, RequestMethod.POST})
    public void excelExport(@ModelAttribute @Valid final YearFilterPagingRequest filter,
                            HttpServletResponse response) throws IOException {
        // fetch the data that will be displayed in the chart (we have multiple sources for this dashboard)
        final List<DBObject> countAwardsByYear = countPlansTendersAwardsController.countAwardsByYear(filter);
        final List<DBObject> countTendersByYear = countPlansTendersAwardsController.countTendersByYear(filter);

        final List<String> categories = getCategoriesFromDBObject("_id", countAwardsByYear, countTendersByYear);
        final List<List<? extends Number>> values = new ArrayList<>();

        final List<Number> valueAwards = getValuesFromDBObject(countAwardsByYear, categories, "_id", "count");
        final List<Number> valueTenders = getValuesFromDBObject(countTendersByYear, categories, "_id", "count");
        values.add(valueAwards);
        values.add(valueTenders);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + "procurement activity by year.xlsx");
        response.getOutputStream().write(excelChartGenerator.getExcelChart(ChartType.line, categories, values));
    }

    /**
     * Collects categories from a List of DBObjects.
     *
     * @param catKey   - key that represents the categories
     * @param lists - multiple DBObject lists from who we will extract categories
     * @return
     */
    private List<String> getCategoriesFromDBObject(String catKey, final List<DBObject>... lists) {
        final List<String> categoriesWithDuplicates = new ArrayList<>();
        for (List<DBObject> list : lists) {
            list.parallelStream().forEach(
                    item -> categoriesWithDuplicates.add(String.valueOf(item.toMap().get(catKey))));
        }

        // sort and keep only the unique categories
        // keep in mind that we can have different number of categories from each source
        // (example different number of years)
        return categoriesWithDuplicates.parallelStream().sorted().distinct().collect(Collectors.toList());
    }

    /**
     * Collects values for each category from a List of DBObjects.
     * If the category doesn't exist then we add the null value (we will have an empty cell in excel file).
     */
    private List<Number> getValuesFromDBObject(List<DBObject> list, List<String> categories,
                                               String catKey, String valKey) {
        final List<Number> values = new ArrayList<>();

        categories.forEach(cat -> {
            Optional<DBObject> result = list.parallelStream().filter(
                    val -> val.toMap().get(catKey).equals(Integer.parseInt(cat))).findFirst();
            if (result.isPresent()) {
                values.add((int) result.get().toMap().get(valKey));
            } else {
                values.add(null);
            }
        });

        return values;
    }
}
