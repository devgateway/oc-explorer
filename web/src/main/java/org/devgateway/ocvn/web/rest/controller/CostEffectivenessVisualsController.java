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
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class CostEffectivenessVisualsController extends GenericOcvnController {


	@RequestMapping("/api/costEffectivenessAwardAmount/{year:^\\d{4}$}")
	public List<DBObject> costEffectivenessAwardAmount(@PathVariable Integer year) {

		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)
						.and("tender.tenderPeriod.endDate").gte(getStartDate(year)).lte(getEndDate(year))),
				unwind("$awards"), match(where("awards.status").is("active")),
				group().sum("$awards.value.amount").as("totalAwardAmount"));
	
		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

	@RequestMapping("/api/costEffectivenessTenderAmount/{year:^\\d{4}$}")
	public List<DBObject> costEffectivenessTenderAmount(@PathVariable Integer year) {

		Aggregation agg = newAggregation(
				match(where("awards").elemMatch(where("status").is("active")).and("tender.value").exists(true)
						.and("tender.tenderPeriod.endDate").gte(getStartDate(year)).lte(getEndDate(year))),
				group().sum("$tender.value.amount").as("totalTenderAmount"));
		

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> tagCount = results.getMappedResults();
		return tagCount;

	}

}