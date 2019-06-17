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
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomGroupingOperation;
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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TenderPercentagesController extends GenericOCDSController {

    public static final class Keys {
        public static final String TOTAL_TENDERS = "totalTenders";
        public static final String TOTAL_CANCELLED = "totalCancelled";
        public static final String PERCENT_CANCELLED = "percentCancelled";
        public static final String YEAR = "year";
        public static final String TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS = "totalTendersWithTwoOrMoreTenderers";
        public static final String PERCENT_TENDERS = "percentTenders";
        public static final String TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS = "totalTendersWithOneOrMoreTenderers";
        public static final String TOTAL_TENDERS_USING_EBID = "totalTendersUsingEbid";
        public static final String PERCENTAGE_TENDERS_USING_EBID = "percentageTendersUsingEbid";
        public static final String TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN = "totalTendersWithLinkedProcurementPlan";
    }

    @ApiOperation("Returns the percent of tenders that were cancelled, grouped by year."
            + " The year is taken from tender.tenderPeriod.startDate. The response also contains the"
            + " total number of tenders and total number of cancelled tenders for each year.")
    @RequestMapping(value = "/api/percentTendersCancelled",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> percentTendersCancelled(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));
        project1.put(MongoConstants.FieldNames.TENDER_STATUS, 1);

        DBObject group = new BasicDBObject();
        addYearlyMonthlyReferenceToGroup(filter, group);
        group.put(Keys.TOTAL_TENDERS, new BasicDBObject("$sum", 1));
        group.put(Keys.TOTAL_CANCELLED, new BasicDBObject("$sum", new BasicDBObject(
                "$cond",
                Arrays.asList(new BasicDBObject("$eq", Arrays.asList(
                        ref(MongoConstants.FieldNames.TENDER_STATUS), Tender.Status.cancelled.toString())), 1, 0)
        )));

        DBObject project2 = new BasicDBObject();
        project2.put(Keys.TOTAL_TENDERS, 1);
        project2.put(Keys.TOTAL_CANCELLED, 1);
        project2.put(Keys.PERCENT_CANCELLED, new BasicDBObject(
                "$multiply",
                Arrays.asList(new BasicDBObject("$divide", Arrays.asList("$totalCancelled", "$totalTenders")), 100)
        ));

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group),
                new CustomProjectionOperation(project2),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_TENDERS,
                        Keys.TOTAL_CANCELLED, Keys.PERCENT_TENDERS, Keys.PERCENT_CANCELLED
                ),
                getSortByYearMonth(filter),
                skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }

    @ApiOperation("Percentage of tenders with >1 tenderer/bidder): "
            + "Count of tenders with numberOfTenderers >1 divided by total count of tenders."
            + "This endpoint uses tender.tenderPeriod.startDate to calculate the tender year.")
    @RequestMapping(value = "/api/percentTendersWithTwoOrMoreTenderers",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> percentTendersWithTwoOrMoreTenderers(@ModelAttribute
                                                               @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));
        project1.put(MongoConstants.FieldNames.TENDER_NO_TENDERERS, 1);

        DBObject group = new BasicDBObject();
        addYearlyMonthlyReferenceToGroup(filter, group);
        group.put(Keys.TOTAL_TENDERS, new BasicDBObject("$sum", 1));
        group.put("totalTendersWithTwoOrMoreTenderers", new BasicDBObject("$sum", new BasicDBObject(
                "$cond",
                Arrays.asList(
                        new BasicDBObject("$gt", Arrays.asList(ref(MongoConstants.FieldNames.TENDER_NO_TENDERERS), 1)),
                        1, 0
                )
        )));

        DBObject project2 = new BasicDBObject();
        project2.put(Keys.TOTAL_TENDERS, 1);
        project2.put(Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS, 1);
        project2.put(Keys.PERCENT_TENDERS, new BasicDBObject("$multiply", Arrays.asList(
                new BasicDBObject("$divide", Arrays.asList("$totalTendersWithTwoOrMoreTenderers", "$totalTenders")),
                100
        )));

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group),
                new CustomProjectionOperation(project2),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_TENDERS,
                        Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS, Keys.PERCENT_TENDERS
                ),
                getSortByYearMonth(filter),
                skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }

    @ApiOperation("Percent of awarded tenders with >1 tenderer/bidder"
            + "Count of tenders with numberOfTenderers >1 divided by total count of tenders with numberOfTenderers >0"
            + "This endpoint uses tender.tenderPeriod.startDate to calculate the tender year.")
    @RequestMapping(value = "/api/percentTendersAwardedWithTwoOrMoreTenderers",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> percentTendersAwarded(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));
        project1.put(MongoConstants.FieldNames.TENDER_NO_TENDERERS, 1);

        DBObject group = new BasicDBObject();
        addYearlyMonthlyReferenceToGroup(filter, group);
        group.put(Keys.TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS, new BasicDBObject("$sum", new BasicDBObject(
                "$cond",
                Arrays.asList(
                        new BasicDBObject("$gt", Arrays.asList(ref(MongoConstants.FieldNames.TENDER_NO_TENDERERS), 0)),
                        1, 0
                )
        )));

        group.put(Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS, new BasicDBObject("$sum", new BasicDBObject(
                "$cond",
                Arrays.asList(
                        new BasicDBObject("$gt", Arrays.asList(ref(MongoConstants.FieldNames.TENDER_NO_TENDERERS), 1)),
                        1, 0
                )
        )));

        DBObject project2 = new BasicDBObject();
        project2.put(Keys.TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS, 1);
        project2.put(Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS, 1);
        project2.put(
                Keys.PERCENT_TENDERS,
                new BasicDBObject("$multiply", Arrays.asList(
                        new BasicDBObject(
                                "$divide",
                                Arrays.asList(
                                        "$totalTendersWithTwoOrMoreTenderers",
                                        "$totalTendersWithOneOrMoreTenderers"
                                )
                        ),
                        100
                ))
        );

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE)
                        .exists(true).and(MongoConstants.FieldNames.TENDER_NO_TENDERERS).gt(0)
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group),
                new CustomProjectionOperation(project2),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS,
                        Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS, Keys.PERCENT_TENDERS
                ),
                getSortByYearMonth(filter), skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }


    @ApiOperation("Returns the percent of tenders with active awards, "
            + "with tender.submissionMethod='electronicSubmission'."
            + "The endpoint also returns the total tenderds with active awards and the count of tenders with "
            + "tender.submissionMethod='electronicSubmission")
    @RequestMapping(value = "/api/percentTendersUsingEBid", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> percentTendersUsingEBid(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));
        project1.put(Fields.UNDERSCORE_ID, "$tender._id");
        project1.put("electronicSubmission", new BasicDBObject("$cond", Arrays.asList(new BasicDBObject(
                        "$eq",
                        Arrays.asList(
                                "$tender.submissionMethod",
                                Tender.SubmissionMethod.electronicSubmission.toString()
                        )
                ), 1,
                0
        )));

        DBObject group1 = new BasicDBObject();
        group1.put(
                Fields.UNDERSCORE_ID, org.springframework.data
                        .mongodb.core.aggregation.Fields.UNDERSCORE_ID_REF);
        addYearlyMonthlyGroupingOperationFirst(filter, group1);
        group1.put("electronicSubmission", new BasicDBObject("$max", "$electronicSubmission"));

        DBObject group2 = new BasicDBObject();
        addYearlyMonthlyReferenceToGroup(filter, group2);
        group2.put(Keys.TOTAL_TENDERS, new BasicDBObject("$sum", 1));
        group2.put(Keys.TOTAL_TENDERS_USING_EBID, new BasicDBObject("$sum", "$electronicSubmission"));

        DBObject project2 = new BasicDBObject();
        project2.put(Keys.TOTAL_TENDERS, 1);
        project2.put(Keys.TOTAL_TENDERS_USING_EBID, 1);
        project2.put(Keys.PERCENTAGE_TENDERS_USING_EBID, new BasicDBObject("$multiply", Arrays
                .asList(new BasicDBObject("$divide", Arrays.asList("$totalTendersUsingEbid", "$totalTenders")), 100)));

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .and("tender.submissionMethod.0").exists(true).
                                and(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                unwind("$tender.submissionMethod"),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group1),
                new CustomGroupingOperation(group2),
                new CustomProjectionOperation(project2),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_TENDERS,
                        Keys.TOTAL_TENDERS_USING_EBID, Keys.PERCENTAGE_TENDERS_USING_EBID
                ),
                getSortByYearMonth(filter), skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }


    @ApiOperation("Percentage of tenders that are associated in releases that "
            + "have the planning.budget.amount non empty,"
            + "meaning there really is a planning entity correlated with the tender entity."
            + "This endpoint uses tender.tenderPeriod.startDate to calculate the tender year.")
    @RequestMapping(value = "/api/percentTendersWithLinkedProcurementPlan",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> percentTendersWithLinkedProcurementPlan(@ModelAttribute
                                                                  @Valid final YearFilterPagingRequest filter) {

        DBObject project1 = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project1, "$tender.tenderPeriod.startDate");
        project1.put("tender._id", 1);
        project1.put("planning.budget.amount", 1);

        DBObject group = new BasicDBObject();
        addYearlyMonthlyReferenceToGroup(filter, group);
        group.put(Keys.TOTAL_TENDERS, new BasicDBObject("$sum", 1));
        group.put(Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN, new BasicDBObject("$sum", new BasicDBObject(
                "$cond",
                Arrays.asList(new BasicDBObject("$gt", Arrays.asList("$planning.budget.amount", null)), 1, 0)
        )));

        DBObject project2 = new BasicDBObject();
        project2.put(Keys.YEAR, 1);
        project2.put("month", 1);
        project2.put(Keys.TOTAL_TENDERS, 1);
        project2.put(Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN, 1);
        project2.put(
                Keys.PERCENT_TENDERS,
                new BasicDBObject(
                        "$multiply",
                        Arrays.asList(
                                new BasicDBObject("$divide", Arrays.asList(
                                        "$" + Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN,
                                        "$" + Keys.TOTAL_TENDERS
                                )),
                                100
                        )
                )
        );

        Aggregation agg = newAggregation(
                match(where("tender.tenderPeriod.startDate").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, "tender.tenderPeriod.startDate"))),
                new CustomProjectionOperation(project1), new CustomGroupingOperation(group),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_TENDERS,
                        Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN, Keys.PERCENT_TENDERS
                ),
                new CustomProjectionOperation(project2),
                getSortByYearMonth(filter), skip(filter.getSkip()), limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }


}