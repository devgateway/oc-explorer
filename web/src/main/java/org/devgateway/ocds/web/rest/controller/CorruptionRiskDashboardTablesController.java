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

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class CorruptionRiskDashboardTablesController extends GenericOCDSController {

    @ApiOperation(value = "Returns data to show in the table on corruption risk overview page."
            + "This is presented as releases only with the information in the table present and unwinded by "
            + "flags.flaggedStats")
    @RequestMapping(value = "/api/corruptionRiskOverviewTable",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> corruptionRiskOverviewTable(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {


        Aggregation agg = newAggregation(
                match(where("flags.flaggedStats.0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                unwind("flags.flaggedStats"),
                project("ocid", "tender.procuringEntity.name", "tender.tenderPeriod", "flags",
                        "tender.title", "tag")
                        .and("tender.value").as("tender.value").and("awards.value").as("awards.value")
                        .and(MongoConstants.FieldNames.AWARDS_STATUS).as(MongoConstants.FieldNames.AWARDS_STATUS)
                        .andExclude(Fields.UNDERSCORE_ID),
                sort(Sort.Direction.DESC, "flags.flaggedStats.count"),
                skip(filter.getSkip()),
                limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }

    @ApiOperation(value = "Counts data to show in the table on corruption risk overview page.")
    @RequestMapping(value = "/api/corruptionRiskOverviewTable/count",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> corruptionRiskOverviewTableCount(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where("flags.flaggedStats.0").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                unwind("flags.flaggedStats"),
                group().count().as("count")
        );

       return releaseAgg(agg);
    }

}