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

import java.util.Arrays;
import java.util.List;

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
public class AverageTenderAndAwardPeriods extends GenericOcvnController {

	/**
	 * db.release.aggregate( [ { "$match" : { $and: [
	 * {"tender.tenderPeriod.startDate" : { "$ne" : null} } ,
	 * {"tender.tenderPeriod.endDate" : { "$ne" : null} }
	 * 
	 * ] } } , { "$project" : { "_id": false, "tenderLengthDays" : { $let: {
	 * vars: { endDate: "$tender.tenderPeriod.endDate", startDate:
	 * "$tender.tenderPeriod.startDate" }, in: round ({ $divide : [ {$subtract:
	 * [ "$$endDate" , "$$startDate" ]} , 86400000 ] },0) } }, year: {$year :
	 * "$tender.tenderPeriod.startDate"}
	 * 
	 * }
	 * 
	 * }, {$group: { _id: "$year", averageTenderDays: { $avg:
	 * "$tenderLengthDays" } } }, {$sort : { _id:1} } ] );
	 * 
	 * @return
	 */
	@RequestMapping("/api/averageTenderPeriod")
	public List<DBObject> averageTenderPeriod() {

		DBObject year = new BasicDBObject("$year", "$tender.tenderPeriod.startDate");

		DBObject tenderLengthDays = new BasicDBObject("$divide",
				Arrays.asList(
						new BasicDBObject("$subtract",
								Arrays.asList("$tender.tenderPeriod.endDate", "$tender.tenderPeriod.startDate")),
						86400000));

		DBObject project = new BasicDBObject();
		project.put(Fields.UNDERSCORE_ID, 0);
		project.put("year", year);
		project.put("tenderLengthDays", tenderLengthDays);

		Aggregation agg = newAggregation(
				match(where("tender.tenderPeriod.startDate").exists(true).and("tender.tenderPeriod.endDate")
						.exists(true)),
				new CustomOperation(new BasicDBObject("$project", project)),
				group("$year").avg("$tenderLengthDays").as("averageTenderDays"),
				sort(Direction.ASC, Fields.UNDERSCORE_ID));

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> list = results.getMappedResults();
		return list;
	}

	/**
	 * db.release.aggregate( [ {$project: {"awards.date":1,
	 * "tender.tenderPeriod.endDate":1} }, {$unwind : "$awards"}, { $match : {
	 * $and: [ {"tender.tenderPeriod.endDate" : { "$ne" : null} },
	 * {"awards.date" : { "$ne" : null} },
	 * 
	 * ] } } , { "$project" : { "_id": 0, year: {$year : "$awards.date"},
	 * awardLengthDays: { $divide : [ {$subtract: [ "$awards.date" ,
	 * "$tender.tenderPeriod.endDate" ]} , 86400000 ] } } }, {$group: { _id:
	 * "$year", averageAwardDays: { $avg: "$awardLengthDays" } } }, ])
	 * 
	 * @return
	 */
	@RequestMapping("/api/averageAwardPeriod")
	public List<DBObject> averageAwardPeriod() {
		DBObject year = new BasicDBObject("$year", "$awards.date");

		DBObject awardLengthDays = new BasicDBObject("$divide",
				Arrays.asList(
						new BasicDBObject("$subtract", Arrays.asList("$awards.date", "$tender.tenderPeriod.endDate")),
						86400000));

		DBObject project = new BasicDBObject();
		project.put(Fields.UNDERSCORE_ID, 0);
		project.put("year", year);
		project.put("awardLengthDays", awardLengthDays);
		
		DBObject group = new BasicDBObject();
		group.put(Fields.UNDERSCORE_ID,"$year");
		group.put("averageAwardDays",new BasicDBObject("$avg", "$awardLengthDays" ));
		
		DBObject sort= new BasicDBObject();
		sort.put(Fields.UNDERSCORE_ID,1);
		
		DBObject project2 = new BasicDBObject();
		project2.put("awards.date", 1);
		//project2.put("awards.status", 1);
		project2.put("tender.tenderPeriod.endDate", 1);
		
		

		Aggregation agg = newAggregation(
				unwind("$awards"),
				new CustomOperation(new BasicDBObject("$project",project2)),
				match(where("tender.tenderPeriod.endDate").exists(true).and("awards.date").exists(true)),
				new CustomOperation(new BasicDBObject("$project", project)),
				new CustomOperation(new BasicDBObject("$group", group)),
				new CustomOperation(new BasicDBObject("$sort", sort)));
		

		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
		List<DBObject> list = results.getMappedResults();
		return list;
	}

}