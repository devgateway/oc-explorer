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
        public static final String PROJECT_COUNT = "projectCount";
    }

    private static final String ELIGIBLE_STATS = "eligibleStats";
    private static final String FLAGGED_STATS = "flaggedStats";


    @ApiOperation(value = "Counts the indicators flagged, and groups them by indicator type. "
            + "An indicator that has two types it will be counted twice, once in each group.")
    @RequestMapping(value = "/api/totalFlaggedIndicatorsByIndicatorType",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalFlaggedIndicatorsByIndicatorType(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        return totalIndicatorsByIndicatorType(FLAGGED_STATS, filter);
    }

    @ApiOperation(value = "Counts the indicators eligible, and groups them by indicator type. "
            + "An indicator that has two types it will be counted twice, once in each group.")
    @RequestMapping(value = "/api/totalEligibleIndicatorsByIndicatorType",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalEligibleIndicatorsByIndicatorType(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        return totalIndicatorsByIndicatorType(ELIGIBLE_STATS, filter);
    }


    private List<DBObject> totalIndicatorsByIndicatorType(String statsProperty,
                                                          final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .and("flags." + statsProperty + ".0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags." + statsProperty),
                project("flags." + statsProperty),
                group(statsProperty + ".type").sum(statsProperty + ".count").as(Keys.COUNT),
                project(Keys.COUNT).and(Fields.UNDERSCORE_ID).as(Keys.TYPE).andExclude(Fields.UNDERSCORE_ID)
        );


        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }

    @ApiOperation(value = "Counts the indicators flagged, and groups them by indicator type and by year/month. "
            + "An indicator that has two types it will be counted twice, once in each group.")
    @RequestMapping(value = "/api/totalIndicatorsByIndicatorTypeYearly",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalIndicatorsByIndicatorTypeYearly(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        return totalIndicatorsByIndicatorTypeYearly(FLAGGED_STATS, filter);
    }

    @ApiOperation(value = "Counts the indicators eligible, and groups them by indicator type and by year/month. "
            + "An indicator that has two types it will be counted twice, once in each group.")
    @RequestMapping(value = "/api/totalEligibleIndicatorsByIndicatorTypeYearly",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> totalEligibleIndicatorsByIndicatorTypeYearly(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        return totalIndicatorsByIndicatorTypeYearly(ELIGIBLE_STATS, filter);
    }


    private List<DBObject> totalIndicatorsByIndicatorTypeYearly(String statsProperty,
                                                                final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, "$tender.tenderPeriod.startDate");
        project1.put("stats", "$flags." + statsProperty);
        project1.put(Fields.UNDERSCORE_ID, 0);

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .and("flags." + statsProperty + ".0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags." + statsProperty),
                new CustomProjectionOperation(project1),
                group(getYearlyMonthlyGroupingFields(filter, "stats.type")).
                        sum("stats.count").as(Keys.COUNT)
        );


        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }

    @ApiOperation(value = "Counts the projects and the indicators flagged, grouped by indicator type. "
            + "The 'count' represents the number of indicators flagged, the 'projectCount' represents the number"
            + " of projects flagged.")
    @RequestMapping(value = "/api/totalFlaggedProjectsByIndicatorType",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<DBObject> totalFlaggedProjectsByIndicatorType(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        return totalProjectsByIndicatorType(FLAGGED_STATS, filter);
    }

    @ApiOperation(value = "Counts the projects and the indicators eligible, grouped by indicator type. "
            + "The 'count' represents the number of indicators eligible, the 'projectCount' represents the number"
            + " of projects eligible.")
    @RequestMapping(value = "/api/totalEligibleProjectsByIndicatorType",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<DBObject> totalEligibleProjectsByIndicatorType(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        return totalProjectsByIndicatorType(ELIGIBLE_STATS, filter);
    }


    private List<DBObject> totalProjectsByIndicatorType(String statsProperty, final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, "$tender.tenderPeriod.startDate");
        project1.put("stats", "$flags." + statsProperty);
        project1.put(Fields.UNDERSCORE_ID, 0);

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .and("flags." + statsProperty + ".0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                unwind("flags." + statsProperty),
                new CustomProjectionOperation(project1),
                group(getYearlyMonthlyGroupingFields(filter, "stats.type")).
                        sum("stats.count").as(Keys.COUNT).count().as(Keys.PROJECT_COUNT)
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release",
                DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }


}