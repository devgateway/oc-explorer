package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort.Direction;
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
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TenderPriceByTypeYearController extends GenericOCDSController {

    public static final class Keys {
        public static final String YEAR = "year";
        public static final String TOTAL_TENDER_AMOUNT = "totalTenderAmount";
        public static final String PROCUREMENT_METHOD = "procurementMethod";
    }

    @ApiOperation(value = "Returns the tender price by OCDS type (procurementMethod), by year. "
            + "The OCDS type is read from tender.procurementMethod. The tender price is read from "
            + MongoConstants.FieldNames.TENDER_VALUE_AMOUNT)
    @RequestMapping(value = "/api/tenderPriceByProcurementMethod", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<DBObject> tenderPriceByProcurementMethod(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        project.put("tender." + Keys.PROCUREMENT_METHOD, 1);
        project.put("tender.value", 1);

        Aggregation agg = newAggregation(
                match(where("awards").elemMatch(where("status").is(Award.Status.active.toString()))
                        .and("tender.value")
                        .exists(true)
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                new CustomProjectionOperation(project), group("tender." + Keys.PROCUREMENT_METHOD)
                        .sum(ref(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT)).as(Keys.TOTAL_TENDER_AMOUNT),
                project().and(Fields.UNDERSCORE_ID).as(Keys.PROCUREMENT_METHOD).andInclude(Keys.TOTAL_TENDER_AMOUNT)
                        .andExclude(Fields.UNDERSCORE_ID),
                sort(Direction.DESC, Keys.TOTAL_TENDER_AMOUNT)
        );

        return releaseAgg(agg);
    }

}