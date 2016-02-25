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
package org.devgateway.ocvn.web.rest.controller.selector;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.List;

import org.devgateway.ocvn.web.rest.controller.GenericOcvnController;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
public class BidSelectionMethodSearchController extends GenericOcvnController {

	/**
	 * db.release.aggregate([ {$project : {"tender.procurementMethodDetails":1} },
	 * {$group: {_id: "$tender.procurementMethodDetails" }} ])
	 * 
	 * @return
	 */
	@RequestMapping("/api/ocds/bidSelectionMethod/all")
	public List<DBObject> bidSelectionMethods() {

		DBObject project = new BasicDBObject("tender.procurementMethodDetails", 1);

		Aggregation agg = newAggregation(new CustomOperation(new BasicDBObject("$project", project)),
				group("$tender.procurementMethodDetails"));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);

		List<DBObject> mappedResults = results.getMappedResults();

		return mappedResults;

	}

}