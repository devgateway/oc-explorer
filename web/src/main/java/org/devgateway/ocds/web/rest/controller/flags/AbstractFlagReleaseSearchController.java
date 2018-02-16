package org.devgateway.ocds.web.rest.controller.flags;

import com.fasterxml.jackson.annotation.JsonView;
import com.mongodb.DBObject;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.spring.json.Views;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.List;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.AWARDS_VALUE;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.FLAGS_COUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_PERIOD;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_NAME;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_STATUS;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_TITLE;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_VALUE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by mpostelnicu on 12/2/2016.
 */
public abstract class AbstractFlagReleaseSearchController extends AbstractFlagController {


    @JsonView(Views.Internal.class)
    public List<DBObject> releaseFlagSearch(@ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Aggregation agg = newAggregation(
                match(where("flags.flaggedStats.0").exists(true).and(getFlagProperty()).is(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                unwind("flags.flaggedStats"),
                match(where(getFlagProperty()).is(true).andOperator(getFlagTypeFilterCriteria(filter))),
                project("ocid", TENDER_PROCURING_ENTITY_NAME, TENDER_PERIOD, "flags",
                        TENDER_TITLE, "tag", TENDER_STATUS)
                        .and(TENDER_VALUE).as(TENDER_VALUE).and(AWARDS_VALUE).as(AWARDS_VALUE)
                        .and(MongoConstants.FieldNames.AWARDS_STATUS).as(MongoConstants.FieldNames.AWARDS_STATUS)
                        .andExclude(Fields.UNDERSCORE_ID),
                sort(Sort.Direction.DESC, FLAGS_COUNT),
                skip(filter.getSkip()),
                limit(filter.getPageSize())
        );

        return releaseAgg(agg);
    }

    @JsonView(Views.Internal.class)
    public List<DBObject> releaseFlagCount(@ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Aggregation agg = newAggregation(
                match(where("flags.flaggedStats.0").exists(true).and(getFlagProperty()).is(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE))),
                unwind("flags.flaggedStats"),
                match(where(getFlagProperty()).is(true).andOperator(getFlagTypeFilterCriteria(filter))),
                group().count().as("count")
        );
        return releaseAgg(agg);
    }
}
