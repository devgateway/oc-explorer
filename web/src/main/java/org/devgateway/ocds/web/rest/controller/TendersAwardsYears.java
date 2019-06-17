/**
 *
 */
package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomGroupingOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomSortingOperation;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomUnwindOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */

@Cacheable
@RestController
@CacheConfig(cacheNames = "tendersAwardsYears")
public class TendersAwardsYears extends GenericOCDSController {

    @ApiOperation(value = "Computes all available years from awards.date, tender.tenderPeriod.startDate")
    @RequestMapping(value = "/api/tendersAwardsYears", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> tendersAwardsYears() {

        BasicDBObject project1 = new BasicDBObject();

        project1.put(
                "tenderYear",
                new BasicDBObject(
                        "$cond",
                        Arrays.asList(
                                new BasicDBObject(
                                        "$gt",
                                        Arrays.asList(ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE), null)
                                ),
                                new BasicDBObject("$year", ref(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE)),
                                null
                        )
                )
        );

        project1.put(
                "awardYear",
                new BasicDBObject("$cond", Arrays.asList(
                        new BasicDBObject("$gt", Arrays.asList(ref(MongoConstants.FieldNames.AWARDS_DATE), null)),
                        new BasicDBObject("$year", ref(MongoConstants.FieldNames.AWARDS_DATE)), null
                ))
        );
        project1.put(Fields.UNDERSCORE_ID, 0);

        BasicDBObject project2 = new BasicDBObject();
        project2.put("year", Arrays.asList("$tenderYear", "$awardYear"));

        Aggregation agg = Aggregation.newAggregation(
                project().and(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE)
                        .as(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE)
                        .and(MongoConstants.FieldNames.AWARDS_DATE)
                        .as(MongoConstants.FieldNames.AWARDS_DATE),
                match(new Criteria().orOperator(
                        where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true),
                        where(MongoConstants.FieldNames.AWARDS_DATE).exists(true)
                )),
                new CustomUnwindOperation("$awards", true), new CustomProjectionOperation(project1),
                new CustomProjectionOperation(project2), new CustomUnwindOperation("$year"),
                match(where("year").ne(null)),
                new CustomGroupingOperation(new BasicDBObject(Fields.UNDERSCORE_ID, "$year")),
                new CustomSortingOperation(new BasicDBObject(Fields.UNDERSCORE_ID, 1))
        );

        return releaseAgg(agg);
    }

}