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
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocvn.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
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
public class CostEffectivenessVisualsController extends GenericOcvnController {


	@RequestMapping("/api/costEffectivenessAwardAmount")
	public List<DBObject> costEffectivenessAwardAmount(@Valid DefaultFilterPagingRequest filter) {

		DBObject project = new BasicDBObject();
		project.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.endDate"));
		project.put("awards.value.amount", 1);
		

		
		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)),
						getFilterOperation(filter)
						,unwind("$awards"), match(where("awards.status").is("active").and("awards.value").exists(true)),
						new CustomProjectionOperation(project),
				group("$year").sum("$awards.value.amount").as("totalAwardAmount"),
				sort(Direction.ASC,Fields.UNDERSCORE_ID)
				);
	
		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

	@RequestMapping("/api/costEffectivenessTenderAmount")
	public List<DBObject> costEffectivenessTenderAmount(@Valid DefaultFilterPagingRequest filter) {

		DBObject project = new BasicDBObject();
		project.put("year", new BasicDBObject("$year", "$tender.tenderPeriod.endDate"));
		project.put("tender.value.amount", 1);
		project.put("tender.items",1);
		project.put("tender.procuringEntity", 1);

		
		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)),
				new CustomProjectionOperation(project),
				getFilterOperation(filter),
				group("$year").sum("$tender.value.amount").as("totalTenderAmount"),
				sort(Direction.ASC,Fields.UNDERSCORE_ID)
				);
		

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

}