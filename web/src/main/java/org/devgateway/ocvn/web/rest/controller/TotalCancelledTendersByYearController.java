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
package org.devgateway.ocvn.web.rest.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocvn.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class TotalCancelledTendersByYearController extends GenericOcvnController {


	@RequestMapping("/api/totalCancelledTendersByYear")
	public List<DBObject> totalCancelledTendersByYear(@Valid DefaultFilterPagingRequest filter) {

		DBObject year = new BasicDBObject("$year", "$tender.tenderPeriod.startDate");

		DBObject project = new BasicDBObject();
		project.put(Fields.UNDERSCORE_ID, 0);
		project.put("year", year);
		project.put("tender.value.amount", 1);

		Aggregation agg = newAggregation(
				match(where("tender.status").is("cancelled")),
				getFilterOperation(filter),		
				new CustomOperation(new BasicDBObject("$project", project)),
				group("$year").sum("$tender.value.amount").as("totalCancelledTendersAmount"),
				sort(Direction.ASC, Fields.UNDERSCORE_ID));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> list = results.getMappedResults();
		return list;
	}



}