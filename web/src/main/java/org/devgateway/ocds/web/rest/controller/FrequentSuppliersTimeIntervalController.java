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
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
public class FrequentSuppliersTimeIntervalController extends GenericOCDSController {

    public static class TendererPair {
        private String tendererId1;
        private String tendererId2;

        public String getTendererId1() {
            return tendererId1;
        }

        public void setTendererId1(String tenderer1) {
            this.tendererId1 = tenderer1;
        }

        public String getTendererId2() {
            return tendererId2;
        }

        public void setTendererId2(String tenderer2) {
            this.tendererId2 = tenderer2;
        }

    }


    @ApiOperation(value = ".")
    @RequestMapping(value = "/api/frequentSuppliersTimeInterval", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> frequentSuppliersTimeInterval(
            @RequestParam(defaultValue = "365", required = false) Integer intervalDays,
            @RequestParam(defaultValue = "3", required = false) Integer maxAwards) {

        DBObject project = new BasicDBObject();
        project.put("awards._id", 1);
        project.put("tender.procuringEntity._id", 1);
        project.put("awards.suppliers._id", 1);
        project.put("awards.date", 1);
        project.put(Fields.UNDERSCORE_ID, 0);
        project.put("timeInterval", new BasicDBObject("$ceil", new BasicDBObject("$divide",
                Arrays.asList(new BasicDBObject("$divide", Arrays.asList(new BasicDBObject("$subtract",
                        Arrays.asList(new Date(), "$awards.date")), 86400000)), intervalDays))));


        Aggregation agg = Aggregation.newAggregation(
                match(where("tender.procuringEntity").exists(true).and("awards.suppliers.0").exists(true)
                        .and("awards.date").exists(true)),
                unwind("$awards"),
                unwind("$awards.suppliers"),
                new CustomProjectionOperation(project),
                group("$tender.procuringEntity._id", "$awards.suppliers._id", "$timeInterval").
                        count().as("count").addToSet("$awards._id").as("awardIds"),
                match(where("count").gt(maxAwards)),
                sort(Sort.Direction.DESC, "count")
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> list = results.getMappedResults();
        return list;
    }

}