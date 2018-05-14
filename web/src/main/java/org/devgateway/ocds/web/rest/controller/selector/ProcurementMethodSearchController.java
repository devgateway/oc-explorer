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
package org.devgateway.ocds.web.rest.controller.selector;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@Cacheable
@CacheConfig(cacheNames = "procurementMethodsJson")
public class ProcurementMethodSearchController extends GenericOCDSController {

    @ApiOperation(value = "Display the available procurement methods. "
            + "These are taken from tender.procurementMethod")
    @RequestMapping(value = "/api/ocds/procurementMethod/all", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    public List<DBObject> procurementMethods() {

        DBObject project = new BasicDBObject(MongoConstants.FieldNames.TENDER_PROC_METHOD, 1);

        Aggregation agg = newAggregation(new CustomOperation(new BasicDBObject("$project", project)),
                group(ref(MongoConstants.FieldNames.TENDER_PROC_METHOD)));

        return releaseAgg(agg);
    }

}