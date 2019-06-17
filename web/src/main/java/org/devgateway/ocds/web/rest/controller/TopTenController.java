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
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomGroupingOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TopTenController extends GenericOCDSController {

    public static final class Keys {
        public static final String AWARDS = "awards";
        public static final String DATE = "date";
        public static final String SUPPLIERS = "suppliers";
        public static final String VALUE = "value";
        public static final String NAME = "name";
        public static final String TENDER = "tender";
        public static final String TENDER_PERIOD = "tenderPeriod";
        public static final String PROCURING_ENTITY = "procuringEntity";
        public static final String TOTAL_AWARD_AMOUNT = "totalAwardAmount";
        public static final String TOTAL_CONTRACTS = "totalContracts";
        public static final String PROCURING_ENTITY_IDS = "procuringEntityIds";
        public static final String PROCURING_ENTITY_IDS_COUNT = "procuringEntityIdsCount";
        public static final String SUPPLIER_ID = "supplierId";
    }

    /**
     * db.release.aggregate( [ {$match: {MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT: {$exists:
     * true}}}, {$unwind:"$awards"},
     * {$project:{_id:0,MongoConstants.FieldNames.AWARDS_DATE:1,
     * "awards.suppliers.name":1,"awards.value":1, "planning.budget":1}},
     * {$sort: {MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT:-1}}, {$limit:10} ] )
     *
     * @return
     */

    @ApiOperation(value = "Returns the top ten largest active awards."
            + " The amount is taken from the award.value field. The returned data will contain"
            + "the following fields: "
            + "awards.date, awards.suppliers.name, "
            + "awards.value.amount, awards.suppliers.name, planning.budget (if any)")
    @RequestMapping(value = "/api/topTenLargestAwards", method = {RequestMethod.POST,
            RequestMethod.GET},
            produces = "application/json")
    public List<Document> topTenLargestAwards(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        BasicDBObject project = new BasicDBObject();
        project.put(Fields.UNDERSCORE_ID, 0);
        project.put(MongoConstants.FieldNames.AWARDS_DATE, 1);
        project.put("awards.suppliers.name", 1);
        project.put(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT, 1);
        project.put("planning.budget", 1);

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).exists(true)
                        .and(MongoConstants.FieldNames.AWARDS_STATUS)
                        .is(Award.Status.active.toString())
                        .andOperator(getDefaultFilterCriteria(filter))),
                unwind("awards"),
                match(getYearFilterCriteria(filter.awardFiltering(), MongoConstants.FieldNames.AWARDS_DATE)),
                new CustomOperation(new Document("$project", project)),
                sort(Direction.DESC, MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT), limit(10)
        );

        return releaseAgg(agg);
    }

    /**
     * db.release.aggregate( [ {$match: {MongoConstants.FieldNames.TENDER_VALUE_AMOUNT: {$exists:
     * true}}}, {$project:{_id:0,"tender.value":1,
     * "tender.tenderPeriod":1,"tender.procuringEntity.name":1}}, {$sort:
     * {MongoConstants.FieldNames.TENDER_VALUE_AMOUNT:-1}}, {$limit:10} ] )
     *
     * @return
     */
    @ApiOperation(value = "Returns the top ten largest active tenders."
            + " The amount is taken from the tender.value.amount field." + " The returned data will contain"
            + "the following fields: " + "tender.date, tender.value.amount, tender.tenderPeriod, "
            + "tender.procuringEntity.name")
    @RequestMapping(value = "/api/topTenLargestTenders", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> topTenLargestTenders(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        BasicDBObject project = new BasicDBObject();
        project.put(Fields.UNDERSCORE_ID, 0);
        project.put(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT, 1);
        project.put("tender.tenderPeriod", 1);
        project.put("tender.procuringEntity.name", 1);

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                new CustomOperation(new Document("$project", project)),
                sort(Direction.DESC, MongoConstants.FieldNames.TENDER_VALUE_AMOUNT), limit(10)
        );

        return releaseAgg(agg);
    }


    @ApiOperation(value = "This endpoint should return the following data for the Top 10 suppliers (by award value)."
            + "Returns supplier id, total awarded amount of all awarded contracts, count of awarded contracts,"
            + "Ids of the procuring entities from which they have received an award, and their count. "
            + "All filters ally here, the year filter uses the awards.date field.")
    @RequestMapping(value = "/api/topTenSuppliers", method = {RequestMethod.POST,
            RequestMethod.GET},
            produces = "application/json")
    public List<Document> topTenLargestSuppliers(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        BasicDBObject project = new BasicDBObject();
        project.put(Fields.UNDERSCORE_ID, 0);
        project.put(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID, 1);
        project.put(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT, 1);
        project.put(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID, 1);

        BasicDBObject group = new BasicDBObject();
        group.put(Fields.UNDERSCORE_ID, ref(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID));
        group.put(
                Keys.TOTAL_AWARD_AMOUNT, new BasicDBObject("$sum", ref(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT)));
        group.put(Keys.TOTAL_CONTRACTS, new BasicDBObject("$sum", 1));
        group.put(
                Keys.PROCURING_ENTITY_IDS,
                new BasicDBObject("$addToSet", ref(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID))
        );


        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).exists(true)
                        .and(MongoConstants.FieldNames.AWARDS_STATUS)
                        .is(Award.Status.active.toString())
                        .andOperator(getDefaultFilterCriteria(filter))),
                unwind("awards"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())),
                unwind("awards.suppliers"),
                match(getYearFilterCriteria(filter.awardFiltering(), MongoConstants.FieldNames.AWARDS_DATE)),
                new CustomProjectionOperation(project),
                new CustomGroupingOperation(group),
                sort(Direction.DESC, Keys.TOTAL_AWARD_AMOUNT),
                limit(10),
                project().and(Fields.UNDERSCORE_ID).
                        as(Keys.SUPPLIER_ID).
                        andInclude(Keys.TOTAL_AWARD_AMOUNT, Keys.TOTAL_CONTRACTS, Keys.PROCURING_ENTITY_IDS)
                        .andExclude(Fields.UNDERSCORE_ID)
                        .and(Keys.PROCURING_ENTITY_IDS).size().as(Keys.PROCURING_ENTITY_IDS_COUNT)
        );

        return releaseAgg(agg);
    }

}