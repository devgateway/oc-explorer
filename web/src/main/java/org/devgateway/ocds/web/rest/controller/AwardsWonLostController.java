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
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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
public class AwardsWonLostController extends GenericOCDSController {


    @ApiOperation(value = "Counts the won procurements, receives any filters, but most important here"
            + " is the supplierId")
    @RequestMapping(value = "/api/procurementsWon",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsWon(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where("awards.status").is("active")
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                unwind("awards"),
                match(getYearDefaultFilterCriteria(
                        filter,
                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                )),
                group(Fields.UNDERSCORE_ID),
                group().count().as("cnt"),
                project("cnt").andExclude(Fields.UNDERSCORE_ID)
        );
        return releaseAgg(agg);
    }


    @ApiOperation(value = "Counts the lost procurements, receives any filters, but most important here"
            + " is the supplierId. This will use bidder filter")
    @RequestMapping(value = "/api/procurementsLost",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsLost(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        )),
                unwind("bids"),
                match(getYearDefaultFilterCriteria(
                        filter,
                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                )),
                group(Fields.UNDERSCORE_ID),
                group().count().as("cnt"),
                project("cnt").andExclude(Fields.UNDERSCORE_ID)
        );
        return releaseAgg(agg);
    }

}