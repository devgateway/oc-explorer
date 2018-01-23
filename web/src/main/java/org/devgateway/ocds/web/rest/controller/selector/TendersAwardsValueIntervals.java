/**
 *
 */
package org.devgateway.ocds.web.rest.controller.selector;

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@Cacheable
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
public class TendersAwardsValueIntervals extends GenericOCDSController {
    @ApiOperation(value = "Returns the min and max of tender.value.amount")
    @RequestMapping(value = "/api/tenderValueInterval", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<DBObject> tenderValueInterval(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = Aggregation.newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT).exists(true).
                        andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                project().and(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT)
                        .as(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT),
                group().min(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT).as("minTenderValue").
                        max(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT).as("maxTenderValue"),
                project().andInclude("minTenderValue", "maxTenderValue").andExclude(
                        Fields.UNDERSCORE_ID)
        );

        return releaseAgg(agg);
    }


    @ApiOperation(value = "Returns the min and max of awards.value.amount")
    @RequestMapping(value = "/api/awardValueInterval", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<DBObject> awardValueInterval(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = Aggregation.newAggregation(
                unwind("awards"),
                match(where(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).exists(true).
                        andOperator(getYearDefaultFilterCriteria(
                                filter.awardFiltering(),
                                MongoConstants.FieldNames.AWARDS_DATE
                        ))),
                project().and(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT)
                        .as(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT),
                group().min(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("minAwardValue")
                        .max(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("maxAwardValue"),
                project().andInclude("minAwardValue", "maxAwardValue").andExclude(
                        Fields.UNDERSCORE_ID)
        );
        return releaseAgg(agg);
    }


}
