package org.devgateway.ocvn.web.rest.controller;

import javax.annotation.Nonnegative;

import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class TenderBidPeriodPercentilesByYearController extends GenericOcvnController {

	@RequestMapping("/api/tenderBidPeriodPercentilesByYearController")
	public Object tenderBidPeriodPercentilesByYearController(@RequestParam(required = false) Integer year) {

		ScriptOperations scriptOps = mongoTemplate.scriptOps();

		Object object = scriptOps.call("tenderBidPeriodPercentilesByYearController",
				year == null ? null : year.toString());

		return object;
	}

}
