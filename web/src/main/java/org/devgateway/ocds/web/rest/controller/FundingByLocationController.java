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
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
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
public class FundingByLocationController extends GenericOCDSController {

    public static final class Keys {
        public static final String ITEMS_DELIVERY_LOCATION = "deliveryLocation";
        public static final String TOTAL_TENDERS_AMOUNT = "totalTendersAmount";
        public static final String TENDERS_COUNT = "tendersCount";
        public static final String TOTAL_TENDERS_WITH_START_DATE_AND_LOCATION = "totalTendersWithStartDateAndLocation";
        public static final String TOTAL_TENDERS_WITH_START_DATE = "totalTendersWithStartDate";
        public static final String PERCENT_TENDERS_WITH_START_DATE_AND_LOCATION =
                "percentTendersWithStartDateAndLocation";
        public static final String YEAR = "year";
    }

    @ApiOperation(value = "Total estimated funding (tender.value) grouped by "
            + "tender.items.deliveryLocation and also grouped by year."
            + " The endpoint also returns the count of tenders for each location. "
            + "It responds to all filters. The year is calculated based on tender.tenderPeriod.startDate")
    @RequestMapping(value = "/api/fundingByTenderDeliveryLocation", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    public List<DBObject> fundingByTenderDeliveryLocation(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        project.put("tender.items.deliveryLocation", 1);
        project.put(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT, 1);
        addYearlyMonthlyProjection(filter, project, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));

        Aggregation agg = newAggregation(
                match(where("tender").exists(true).and(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                new CustomProjectionOperation(project), unwind("$tender.items"),
                unwind("$tender.items.deliveryLocation"),
                project(getYearlyMonthlyGroupingFields(filter)).and(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT)
                        .as("tenderAmount")
                        .and("tender.items.deliveryLocation").as("deliveryLocation"),
                match(where("deliveryLocation.geometry.coordinates.0").exists(true)),
                group(getYearlyMonthlyGroupingFields(filter, Keys.ITEMS_DELIVERY_LOCATION))
                        .sum("tenderAmount").as(Keys.TOTAL_TENDERS_AMOUNT).count().as(Keys.TENDERS_COUNT),
                getSortByYearMonth(filter)
        // ,skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }


    @ApiOperation("Calculates percentage of releases with tender with at least one specified delivery location,"
            + " that is the array tender.items.deliveryLocation has to have items."
            + "Filters out stub tenders, therefore tender.tenderPeriod.startDate has to exist.")
    @RequestMapping(value = "/api/qualityFundingByTenderDeliveryLocation", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    public List<DBObject> qualityFundingByTenderDeliveryLocation(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        project.putAll(filterProjectMap);
        project.put(Fields.UNDERSCORE_ID, "$tender._id");
        project.put("tenderItemsDeliveryLocation", new BasicDBObject("$cond",
                Arrays.asList(new BasicDBObject("$gt",
                        Arrays.asList("$tender.items.deliveryLocation", null)), 1, 0)));


        DBObject project1 = new BasicDBObject();
        project1.put(Fields.UNDERSCORE_ID, 0);
        project1.put(Keys.TOTAL_TENDERS_WITH_START_DATE, 1);
        project1.put(Keys.TOTAL_TENDERS_WITH_START_DATE_AND_LOCATION, 1);
        project1.put(Keys.PERCENT_TENDERS_WITH_START_DATE_AND_LOCATION,
                new BasicDBObject("$multiply",
                        Arrays.asList(new BasicDBObject("$divide",
                                        Arrays.asList(
                                                "$totalTendersWithStartDateAndLocation", "$totalTendersWithStartDate")),
                                100)));

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                unwind("$tender.items"), new CustomProjectionOperation(project),
                group(Fields.UNDERSCORE_ID_REF).max("tenderItemsDeliveryLocation").as("hasTenderItemsDeliverLocation"),
                group().count().as("totalTendersWithStartDate").sum("hasTenderItemsDeliverLocation")
                        .as(Keys.TOTAL_TENDERS_WITH_START_DATE_AND_LOCATION),
                new CustomProjectionOperation(project1)
        // ,skip(filter.getSkip()),limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }



}