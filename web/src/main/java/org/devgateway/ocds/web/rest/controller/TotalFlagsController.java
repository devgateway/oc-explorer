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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TotalFlagsController extends GenericOCDSController {

    public static final class Keys {
        public static final String TYPE = "type";
        public static final String COUNT = "count";
        public static final String FLAGGED_COUNT = "flaggedCount";
        public static final String FLAGS_COUNT = "flagsCount";

        public static final String ELIGIBLE_COUNT = "eligibleCount";
    }


    @ApiOperation(value = "Counts the indicators flagged as true, and groups them by indicator type. "
            + "An indicator that has two types it will be counted twice, once in each group.")
    @RequestMapping(value = "/api/totalFlaggedIndicatorsByIndicatorType",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalFlaggedIndicatorsByIndicatorType(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags.flaggedStats"),
                project("flags.flaggedStats"),
                group("flaggedStats.type").sum("flaggedStats.count").as(Keys.FLAGS_COUNT),
                project(Keys.FLAGS_COUNT).and(Fields.UNDERSCORE_ID).as(Keys.TYPE).andExclude(Fields.UNDERSCORE_ID)
        );


        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }


    @ApiOperation(value = "Counts the indicators flagged as true. An indicator that has two types will be counted"
            + "only once.")
    @RequestMapping(value = "/api/totalFlaggedIndicators", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalFlaggedIndicators(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .and("flags.flaggedStats.0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags.flaggedStats"),
                project("flags.flaggedStats"),
                group().sum("flaggedStats.count").as(Keys.COUNT),
                project(Keys.COUNT).andExclude(Fields.UNDERSCORE_ID)
        );


        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }


    @ApiOperation(value = "Counts the projects with at least one indicator flagged as true, grouped by indicator type")
    @RequestMapping(value = "/api/totalFlaggedProjectsByIndicatorType", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalFlaggedProjectsByIndicatorType(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, "$tender.tenderPeriod.startDate");
        project1.put("flaggedStats", "$flags.flaggedStats");
        project1.put(Fields.UNDERSCORE_ID,0);

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .and("flags.flaggedStats.0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags.flaggedStats"),
                new CustomProjectionOperation(project1),
                group(getYearlyMonthlyGroupingFields(filter, "flaggedStats.type")).
                        sum("flaggedStats.count").as(Keys.FLAGS_COUNT).count().as(Keys.FLAGGED_COUNT),
                transformYearlyGrouping(filter).andInclude(Keys.FLAGGED_COUNT, Keys.FLAGS_COUNT)
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }


}