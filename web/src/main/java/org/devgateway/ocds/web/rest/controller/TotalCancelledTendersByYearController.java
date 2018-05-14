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
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TotalCancelledTendersByYearController extends GenericOCDSController {

    public static final class Keys {
        public static final String TOTAL_CANCELLED_TENDERS_AMOUNT = "totalCancelledTendersAmount";
        public static final String YEAR = "year";
    }

    @ApiOperation(value = "Total Cancelled tenders by year. The tender amount is read from tender.value."
            + "The tender status has to be 'cancelled'. The year is retrieved from tender.tenderPeriod.startDate.")
    @RequestMapping(value = "/api/totalCancelledTendersByYear", method = { RequestMethod.POST, RequestMethod.GET },
            produces = "application/json")
    public List<DBObject> totalCancelledTendersByYear(@ModelAttribute @Valid final YearFilterPagingRequest filter) {


        DBObject project = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project, ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE));
        project.put(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT, 1);

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_STATUS).is(Tender.Status.cancelled.toString())
                        .and(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                new CustomOperation(new BasicDBObject("$project", project)),
                getYearlyMonthlyGroupingOperation(filter).
                sum(ref(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT)).as(Keys.TOTAL_CANCELLED_TENDERS_AMOUNT),
                transformYearlyGrouping(filter).andInclude(Keys.TOTAL_CANCELLED_TENDERS_AMOUNT),
                getSortByYearMonth(filter));

       return releaseAgg(agg);
    }

}