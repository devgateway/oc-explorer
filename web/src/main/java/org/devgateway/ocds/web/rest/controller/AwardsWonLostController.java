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
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.BIDS_DETAILS_TENDERERS_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.BIDS_DETAILS_VALUE_AMOUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.FLAGS_TOTAL_FLAGGED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
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
public class AwardsWonLostController extends GenericOCDSController {

    @ApiOperation(value = "Counts the won, lost procurements, flags and amounts. Receives any filters, "
            + "but most important here is the supplierId and bidderId. Requires bid extension. Use bidderId instead "
            + "of supplierId.")
    @RequestMapping(value = "/api/procurementsWonLost",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsWonLost(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Assert.notEmpty(filter.getBidderId(), "bidderId must not be empty!");
        Assert.isTrue(
                CollectionUtils.isEmpty(filter.getSupplierId()),
                "supplierId is not allowed here! Use bidderId to show results!"
        );

        //supplier is the same thing as bidder for this particular query
        filter.setSupplierId(filter.getBidderId());

        Map<String, CriteriaDefinition> noSupplierCriteria =
                createDefaultFilterCriteriaMap(
                        filter);
        noSupplierCriteria.remove(MongoConstants.Filters.SUPPLIER_ID);

        Aggregation agg = newAggregation(
                facet().and(
                        match(getYearDefaultFilterCriteria(
                                filter,
                                noSupplierCriteria,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        )),
                        unwind("bids.details"),
                        unwind("bids.details.tenderers"),
                        match(getYearDefaultFilterCriteria(
                                filter,
                                noSupplierCriteria,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        )),
                        group(BIDS_DETAILS_TENDERERS_ID).count().as("count")
                                .sum(BIDS_DETAILS_VALUE_AMOUNT).as("totalAmount")
                                .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                        project("count", "totalAmount", "countFlags")
                ).as("applied").and(
                        match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                                .andOperator(getYearDefaultFilterCriteria(
                                        filter,
                                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                                ))),
                        unwind("awards"),
                        unwind("awards.suppliers"),
                        match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                                .andOperator(getYearDefaultFilterCriteria(
                                        filter.awardFiltering(),
                                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                                ))),
                        group(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID).count().as("count")
                                .sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("totalAmount")
                                .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                        project("count", "totalAmount", "countFlags")
                ).as("won"),
                unwind("won"),
                unwind("applied"),
                project("won", "applied").and("applied._id").cmp("$won._id").as("comp")
                        .and("applied.count").minus("won.count").as("lostCount")
                        .and("applied.totalAmount").minus("won.totalAmount").as("lostAmount"),
                match(where("comp").is(0)),
                project("won", "applied", "lostCount", "lostAmount")
        );
        return releaseAgg(agg);
    }

    @ApiOperation(value = "Counts the number of wins per supplierId per procuringEntityId, plus shows the flags"
            + " and the awarded total. You must provide supplierId parameter. Any other filter can be used as well.")
    @RequestMapping(value = "/api/supplierWinsPerProcuringEntity",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsWonLostPerProcuringEntity(@ModelAttribute @Valid final YearFilterPagingRequest
                                                                        filter) {

        Assert.notEmpty(filter.getSupplierId(), "supplierId must not be empty!");

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        )).and(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID).exists(true)
                        .and(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_NAME).exists(true)),
                unwind("awards"),
                unwind("awards.suppliers"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter.awardFiltering(),
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                group(Fields.from(
                        Fields.field("supplierId", MongoConstants
                                .FieldNames.AWARDS_SUPPLIERS_ID),
                        Fields.field("procuringEntityName", MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_NAME),
                        Fields.field("procuringEntityId", MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID)
                ))
                        .count().as("count")
                        .sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("totalAmountAwarded")
                        .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                sort(Sort.Direction.DESC, "count")
        );
        return releaseAgg(agg);
    }


}