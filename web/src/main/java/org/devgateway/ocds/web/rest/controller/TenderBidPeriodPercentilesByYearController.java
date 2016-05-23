package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
public class TenderBidPeriodPercentilesByYearController extends GenericOCDSController {

    @RequestMapping(value = "/api/tenderBidPeriodPercentiles", method = RequestMethod.GET,
            produces = "application/json")
    public Object tenderBidPeriodPercentiles(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        ScriptOperations scriptOps = mongoTemplate.scriptOps();

        Object object = scriptOps.call("tenderBidPeriodPercentiles",
                filter.getYear() == null ? null : Arrays.toString(filter.getYear().toArray()),
                filter.getProcuringEntityId() == null ? null
                        : Arrays.toString(filter.getProcuringEntityId().toArray()).replace("[", "").replace("]", ""),
                filter.getBidTypeId() == null ? null
                        : Arrays.toString(filter.getBidTypeId().toArray()).replace("[", "").replace("]", ""),
                filter.getBidSelectionMethod() == null ? null
                        : Arrays.toString(filter.getBidSelectionMethod().toArray()).replace("[", "").replace("]", "")

        );

        return object;
    }

}
