package org.devgateway.ocvn.web.rest.controller;

import java.util.Arrays;

import javax.validation.Valid;

import org.devgateway.ocvn.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class TenderBidPeriodPercentilesByYearController extends GenericOcvnController {

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
