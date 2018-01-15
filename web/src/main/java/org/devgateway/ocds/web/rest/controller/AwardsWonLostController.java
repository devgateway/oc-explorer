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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
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

    @ApiOperation(value = "Counts the won, lost procurements, receives any filters, but most important here"
            + " is the supplierId and bidderId. Requires bid extension. Use bidderId")
    @RequestMapping(value = "/api/procurementsWonLost",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsWonLost(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Assert.notEmpty(filter.getBidderId(), "BidderId must not be empty!");
        Assert.isTrue(CollectionUtils.isEmpty(filter.getSupplierId()),
                "SupplierId is not allowed here! Use bidderId to show results!");

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
                        group(Fields.UNDERSCORE_ID),
                        group().count().as("cnt"),
                        project("cnt").andExclude(Fields.UNDERSCORE_ID)

                ).as("applied").and(
                        match(where("awards.status").is("active")
                                .andOperator(getYearDefaultFilterCriteria(
                                        filter,
                                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                                ))),
                        unwind("awards"),
                        match(where("awards.status").is("active")
                                .andOperator(getYearDefaultFilterCriteria(
                                        filter,
                                        MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                                ))),
                        group(Fields.UNDERSCORE_ID),
                        group().count().as("cnt"),
                        project("cnt").andExclude(Fields.UNDERSCORE_ID)
                ).as("won"),
                unwind("won"),
                unwind("applied"),
                project("won", "applied").and("applied.cnt").minus("won.cnt").as("lostCnt")
        );
        return releaseAgg(agg);
    }

}