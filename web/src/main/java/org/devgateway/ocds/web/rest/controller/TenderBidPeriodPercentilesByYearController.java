package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TenderBidPeriodPercentilesByYearController extends GenericOCDSController {

	@ApiOperation(value = "Returns the tender bid period percentiles: min, q1, median, a3 and max. "
			+ "The tender length in days is "
			+ "calculated from tender.tenderPeriod.startDate and tender.tenderPeriod.endDate")
	@RequestMapping(value = "/api/tenderBidPeriodPercentiles", 
			method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
	public Object tenderBidPeriodPercentiles(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

		ScriptOperations scriptOps = mongoTemplate.scriptOps();

		Object object = scriptOps.call("tenderBidPeriodPercentiles",
				filter.getYear() == null ? null : Arrays.toString(filter.getYear().toArray()),
				filter.getProcuringEntityId() == null ? null
						: Arrays.toString(filter.getProcuringEntityId().toArray()).replace("[", "").replace("]", ""),
				filter.getBidTypeId() == null ? null
						: Arrays.toString(filter.getBidTypeId().toArray()).replace("[", "").replace("]", "")

		);

		return object;
	}

}
