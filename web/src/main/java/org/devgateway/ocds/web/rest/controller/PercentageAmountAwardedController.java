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

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class PercentageAmountAwardedController extends GenericOCDSController {

    public static final class Keys {

    }

    @ApiOperation("")
    @RequestMapping(value = "/api/percentageAmountAwarded",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public List<DBObject> percentTendersCancelled(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

//        DBObject project1 = new BasicDBObject();
//        addYearlyMonthlyProjection(filter, project1, MongoConstants.FieldNames.TENDER_PERIOD_START_DATE_REF);
//        project1.put("tender.status", 1);
//
//        DBObject group = new BasicDBObject();
//        addYearlyMonthlyReferenceToGroup(filter, group);
//        group.put(Keys.TOTAL_TENDERS, new BasicDBObject("$sum", 1));
//        group.put(Keys.TOTAL_CANCELLED, new BasicDBObject("$sum", new BasicDBObject("$cond",
//                Arrays.asList(new BasicDBObject("$eq", Arrays.asList("$tender.status", "cancelled")), 1, 0))));
//
//        DBObject project2 = new BasicDBObject();
//        project2.put(Keys.TOTAL_TENDERS, 1);
//        project2.put(Keys.TOTAL_CANCELLED, 1);
//        project2.put(Keys.PERCENT_CANCELLED, new BasicDBObject("$multiply",
//                Arrays.asList(new BasicDBObject("$divide", Arrays.asList("$totalCancelled", "$totalTenders")), 100)));



        Aggregation agg = newAggregation(
               match(where("tender.procuringEntity").exists(true).and("awards.suppliers.0").exists(true)
                        .andOperator(getProcuringEntityIdCriteria(filter))),
               unwind("awards"),
               match(where("awards.status").is("active")),
               facet().and(
               //         project("awards.suppliers._id").and("awards.value.amount").as("awards.value.amount")
                        match(getSupplierIdCriteria(filter))
//                        group().sum("awards.value.amount").as("sum")
               ).as("totalAwardedTo"),
               facet().and(group().sum("awards.value.amount").as("sum")).as("totalAwarded")
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }



}