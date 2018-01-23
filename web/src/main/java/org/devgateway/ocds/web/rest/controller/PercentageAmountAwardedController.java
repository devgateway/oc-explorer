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
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class PercentageAmountAwardedController extends GenericOCDSController {

    public static final class Keys {

    }

    @Override
    protected Criteria getYearDefaultFilterCriteria(final YearFilterPagingRequest filter, final String dateProperty) {
        return new Criteria().andOperator(
                getBidTypeIdFilterCriteria(filter),
                getNotBidTypeIdFilterCriteria(filter),
                getNotProcuringEntityIdCriteria(filter),
                getProcurementMethodCriteria(filter),
                getByTenderDeliveryLocationIdentifier(filter),
                getByTenderAmountIntervalCriteria(filter),
                getByAwardAmountIntervalCriteria(filter),
                getElectronicSubmissionCriteria(filter),
                getFlaggedCriteria(filter),
                getFlagTypeFilterCriteria(filter),
                getYearFilterCriteria(filter, dateProperty),
                getAwardStatusFilterCriteria(filter));
    }

    @ApiOperation("Calculate percentage of awards awarded to a list of suppliers vs total awards. Filters by all"
            + " filters. Careful using supplierId filter here!"
            + " It has a different signification than for other endpoints.")
    @RequestMapping(value = "/api/percentageAmountAwarded",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<DBObject> percentTendersCancelled(@ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Assert.notEmpty(filter.getProcuringEntityId(), "Must provide at least one procuringEntity!");
        Assert.notEmpty(filter.getSupplierId(), "Must provide at least one supplierId!");
        Aggregation agg = newAggregation(
                getMatchYearDefaultFilterOperation(filter, MongoConstants.FieldNames.TENDER_PERIOD_START_DATE),
                match(where("tender.procuringEntity").exists(true).and("awards.suppliers.0").exists(true)
                        .andOperator(getProcuringEntityIdCriteria(filter))),
                unwind("awards"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())),
                facet().and(match(getSupplierIdCriteria(filter.awardFiltering())),
                        group().sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("sum")
                ).as("totalAwardedToSuppliers")
                        .and(group().sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("sum")).as("totalAwarded"),
                unwind("totalAwardedToSuppliers"),
                unwind("totalAwarded"),
                new CustomProjectionOperation(new BasicDBObject("percentage",
                        getPercentageMongoOp("totalAwardedToSuppliers.sum",
                                "totalAwarded.sum")).append("totalAwardedToSuppliers.sum", 1)
                        .append("totalAwarded.sum", 1))
        );

       return releaseAgg(agg);
    }


}