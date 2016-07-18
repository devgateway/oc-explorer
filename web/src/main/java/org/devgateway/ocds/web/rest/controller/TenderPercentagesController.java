/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.web.rest.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomGroupingOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import io.swagger.annotations.ApiOperation;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TenderPercentagesController extends GenericOCDSController {

	@ApiOperation("Returns the percent of tenders that were cancelled, grouped by year."
			+ " The year is taken from tender.tenderPeriod.startDate. The response also contains the"
			+ " total number of tenders and total number of cancelled tenders for each year.")
    @RequestMapping(value = "/api/percentTendersCancelled", method = RequestMethod.GET, produces = "application/json")
    public List<DBObject> percentTendersCancelled(@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        project1.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.startDate"));
        project1.put("tender.status", 1);

        DBObject group = new BasicDBObject();
        group.put(Fields.UNDERSCORE_ID, "$year");
        group.put("totalTenders", new BasicDBObject("$sum", 1));
        group.put("totalCancelled", new BasicDBObject("$sum", new BasicDBObject("$cond",
                Arrays.asList(new BasicDBObject("$eq", Arrays.asList("$tender.status", "cancelled")), 1, 0))));

        DBObject project2 = new BasicDBObject();
        project2.put(Fields.UNDERSCORE_ID, 0);
        project2.put("year", Fields.UNDERSCORE_ID_REF);
        project2.put("totalTenders", 1);
        project2.put("totalCancelled", 1);
        project2.put("percentCancelled", new BasicDBObject("$multiply",
                Arrays.asList(new BasicDBObject("$divide", Arrays.asList("$totalCancelled", "$totalTenders")), 100)));

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .andOperator(getDefaultFilterCriteria(filter))),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group),
                new CustomProjectionOperation(project2),
                sort(Direction.ASC, "year"), skip(filter.getSkip()), limit(filter.getPageSize())
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }
	
	
	@ApiOperation("Returns the percent of tenders with active awards, "
			+ "with tender.submissionMethod='electronicSubmission'."
			+ "The endpoint also returns the total tenderds with active awards and the count of tenders with "
			+ "tender.submissionMethod='electronicSubmission")
    @RequestMapping(value = "/api/percentTendersUsingEBid", method = RequestMethod.GET, produces = "application/json")
    public List<DBObject> percentTendersUsingEBid(@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        project1.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.startDate"));
        project1.put(Fields.UNDERSCORE_ID, "$tender._id");
		project1.put("electronicSubmission", new BasicDBObject("$cond", Arrays.asList(new BasicDBObject("$eq",
				Arrays.asList("$tender.submissionMethod", Tender.SubmissionMethod.electronicSubmission.toString())), 1,
				0)));

		DBObject group1 = new BasicDBObject();
		group1.put(Fields.UNDERSCORE_ID, Fields.UNDERSCORE_ID_REF);
		group1.put("year", new BasicDBObject("$first", "$year"));
		group1.put("electronicSubmission", new BasicDBObject("$max", "$electronicSubmission"));
		
		DBObject group2 = new BasicDBObject();
		group2.put(Fields.UNDERSCORE_ID, "$year");
		group2.put("totalTenders", new BasicDBObject("$sum", 1));
		group2.put("totalTendersUsingEbid", new BasicDBObject("$sum", "$electronicSubmission"));
               
		DBObject project2 = new BasicDBObject();
		project2.put(Fields.UNDERSCORE_ID, 0);
		project2.put("year", Fields.UNDERSCORE_ID_REF);
		project2.put("totalTenders", 1);
		project2.put("totalTendersUsingEbid", 1);
		project2.put("percentageTendersUsingEbid", new BasicDBObject("$multiply", Arrays
				.asList(new BasicDBObject("$divide", Arrays.asList("$totalTendersUsingEbid", "$totalTenders")), 100)));

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true).and("tender.submissionMethod.0").exists(true).
                		and("awards.status").is("active")
                        .andOperator(getDefaultFilterCriteria(filter))),
                unwind("$tender.submissionMethod"),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group1),
                new CustomGroupingOperation(group2),
                new CustomProjectionOperation(project2),
                sort(Direction.ASC,  "year"), skip(filter.getSkip()), limit(filter.getPageSize())
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }
	
	

}