/**
 * 
 */
package org.devgateway.ocds.web.rest.controller.selector;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;

import io.swagger.annotations.ApiOperation;

/**
 * @author mihai
 *
 */
@RestController
@Cacheable
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
public class TendersAwardsValueIntervals extends GenericOCDSController {
	@ApiOperation(value = "Returns the min and max of tender.value.amount")
	@RequestMapping(value = "/api/tenderValueInterval", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json")
	public List<DBObject> tenderValueInterval(@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

		Aggregation agg = Aggregation.newAggregation(match(where("tender.value.amount").exists(true)),
				getMatchDefaultFilterOperation(filter), project().and("tender.value.amount").as("tender.value.amount"),
				group().min("tender.value.amount").as("minTenderValue").max("tender.value.amount").as("maxTenderValue"),
				project().andInclude("minTenderValue", "maxTenderValue").andExclude(Fields.UNDERSCORE_ID));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;
	}
	
	
	@ApiOperation(value = "Returns the min and max of awards.value.amount")
	@RequestMapping(value = "/api/awardValueInterval", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json")
	public List<DBObject> awardValueInterval(@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

		Aggregation agg = Aggregation.newAggregation(
				getMatchDefaultFilterOperation(filter), unwind("awards"),
				project().and("awards.value.amount").as("awards.value.amount"),
				match(where("awards.value.amount").exists(true)),				
				group().min("awards.value.amount").as("minAwardValue").max("awards.value.amount").as("maxAwardValue"),
				project().andInclude("minAwardValue", "maxAwardValue").andExclude(Fields.UNDERSCORE_ID));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;
	}
	
	
}
