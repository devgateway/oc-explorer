package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import io.swagger.annotations.ApiOperation;

import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TenderPriceByTypeYearController extends GenericOCDSController {
	
	public static final class Keys {
		public static final String YEAR = "year";
		public static final String TOTAL_TENDER_AMOUNT = "totalTenderAmount";
		public static final String PROCUREMENT_METHOD = "procurementMethod";
	}

	@ApiOperation(value = "Returns the tender price by OCDS type (procurementMethod), by year. "
			+ "The OCDS type is read from tender.procurementMethod. The tender price is read from "
			+ "tender.value.amount")
	@RequestMapping(value = "/api/tenderPriceByProcurementMethodYear", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json")
	public List<DBObject> tenderPriceByProcurementMethodYear(
			@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        project.put(Keys.YEAR, new BasicDBObject("$year", "$tender.tenderPeriod.startDate"));
        project.put("tender.procurementMethod", 1);
        project.put("tender.value", 1);

		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)),
				getMatchDefaultFilterOperation(filter), new CustomProjectionOperation(project),
				group(Keys.YEAR, "tender." + Keys.PROCUREMENT_METHOD).sum("$tender.value.amount")
						.as(Keys.TOTAL_TENDER_AMOUNT),
				sort(Direction.DESC, Keys.TOTAL_TENDER_AMOUNT), skip(filter.getSkip()), limit(filter.getPageSize()));

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> tagCount = results.getMappedResults();
        return tagCount;

    }

}
