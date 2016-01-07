package org.devgateway.ocvn.web.rest.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class TenderPriceByTypeYearController extends GenericOcvnController {

	@RequestMapping("/api/tenderPriceByOcdsTypeYear/{year:^\\d{4}$}")
	public List<DBObject> tenderPriceByOcdsTypeYear(@PathVariable Integer year) {
		
		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)
						.and("tender.tenderPeriod.endDate").gte(getStartDate(year)).lte(getEndDate(year))),
				group("tender.procurementMethod").sum("$tender.value.amount").as("totalTenderAmount"));
		

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}
	
	@RequestMapping("/api/tenderPriceByVnTypeYear/{year:^\\d{4}$}")
	public List<DBObject> tenderPriceByVnTypeYear(@PathVariable Integer year) {
		
		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)
						.and("tender.tenderPeriod.endDate").gte(getStartDate(year)).lte(getEndDate(year))),
				group("tender.succBidderMethodName").sum("$tender.value.amount").as("totalTenderAmount"));
		
		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

}
