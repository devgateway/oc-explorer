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
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.GroupingFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class CostEffectivenessVisualsController extends GenericOCDSController {

	@RequestMapping(value = "/api/costEffectivenessAwardAmount", method = RequestMethod.GET,
			produces = "application/json")
	public List<DBObject> costEffectivenessAwardAmount(@ModelAttribute @Valid final DefaultFilterPagingRequest filter) {

		DBObject project = new BasicDBObject();
		project.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.endDate"));
		project.put("awards.value.amount", 1);

		Aggregation agg = Aggregation.newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)),
				getMatchDefaultFilterOperation(filter), unwind("$awards"),
				match(where("awards.status").is("active").and("awards.value").exists(true)),
				new CustomProjectionOperation(project),
				group("$year").sum("$awards.value.amount").as("totalAwardAmount"),
				sort(Direction.DESC, "totalAwardAmount"), skip(filter.getSkip()), limit(filter.getPageSize()));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

	@RequestMapping(value = "/api/costEffectivenessTenderAmount", method = RequestMethod.GET,
			 produces = "application/json")
	public List<DBObject> costEffectivenessTenderAmount(
			@ModelAttribute @Valid final GroupingFilterPagingRequest filter) {

		DBObject project = new BasicDBObject();
		project.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.endDate"));
		project.put("tender.value.amount", 1);
		project.putAll(filterProjectMap);

		Aggregation agg = Aggregation.newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)),
				getMatchDefaultFilterOperation(filter), new CustomProjectionOperation(project),
				getTopXFilterOperation(filter, "$year").sum("$tender.value.amount").as("totalTenderAmount"),
				sort(Direction.DESC, "totalTenderAmount"), skip(filter.getSkip()), limit(filter.getPageSize()));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

}