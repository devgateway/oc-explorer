/**
 *
 */
package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.GroupingFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.TextSearchRequest;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 *
 */
public class GenericOCDSController {

    private static final int LAST_DAY = 31;

    private static final int LAST_MONTH_ZERO = 11;

    protected Map<String, Object> filterProjectMap;

    protected final Logger logger = LoggerFactory.getLogger(GenericOCDSController.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * Gets the date of the first day of the year (01.01.year)
     *
     * @param year
     * @return
     */
    protected Date getStartDate(final int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date start = cal.getTime();
        return start;
    }

    /**
     * Gets the date of the last date of the year (31.12.year)
     *
     * @param year
     * @return
     */
    protected Date getEndDate(final int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, LAST_MONTH_ZERO);
        cal.set(Calendar.DAY_OF_MONTH, LAST_DAY);
        Date end = cal.getTime();
        return end;
    }

    /**
     * Appends the procuring bid type id for this filter, this will fitler based
     * on tender.items.classification._id
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getBidTypeIdFilterCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("tender.items.classification._id", filter.getBidTypeId(), filter);
    }

    /**
     * Appends the contrMethod filter, based on tender.contrMethod
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getContrMethodFilterCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("tender.contrMethod", filter.getContrMethod(), filter);
    }


    protected <S> List<S> genericSearchRequest(TextSearchRequest request, Criteria criteria, Class<S> clazz) {

        PageRequest pageRequest = new PageRequest(request.getPageNumber(), request.getPageSize());

        Query query = null;

        if (request.getText() == null) {
            query = new Query();
        } else {
            query = TextQuery.queryText(new TextCriteria().matching(request.getText())).sortByScore();
        }
        if (criteria != null) {
            query.addCriteria(criteria);
        }

        query.with(pageRequest);

        return mongoTemplate.find(query, clazz);
    }

    private <S> Criteria createFilterCriteria(final String filterName, final List<S> filterValues,
                                              final DefaultFilterPagingRequest filter) {
        if (filterValues == null) {
            return new Criteria();
        }
        return filter.getInvert() ? where(filterName).not().in(filterValues.toArray())
                : where(filterName).in(filterValues.toArray());
    }

    /**
     * Appends the procuring entity id for this filter, this will fitler based
     * on tender.procuringEntity._id
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getProcuringEntityIdCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("tender.procuringEntity._id", filter.getProcuringEntityId(), filter);
    }

    @PostConstruct
    protected void init() {
        filterProjectMap = new ConcurrentHashMap<>();
        filterProjectMap.put("tender.procuringEntity", 1);
        filterProjectMap.put("tender.items.classification", 1);
        filterProjectMap.put("tender.procurementMethodDetails", 1);
        filterProjectMap.put("tender.contrMethod", 1);
    }

    protected Criteria getYearFilterCriteria(final String dateProperty, final YearFilterPagingRequest filter) {
        Criteria[] yearCriteria = null;
        Criteria criteria = new Criteria();

        if (filter.getYear() == null) {
            yearCriteria = new Criteria[1];
            yearCriteria[0] = new Criteria();
        } else {
            yearCriteria = new Criteria[filter.getYear().size()];
            for (int i = 0; i < filter.getYear().size(); i++) {
                yearCriteria[i] = where(dateProperty).gte(getStartDate(filter.getYear().get(i)))
                        .lte(getEndDate(filter.getYear().get(i)));
            }
        }

        return filter.getInvert() ? criteria.norOperator(yearCriteria) : criteria.orOperator(yearCriteria);
    }

    /**
     * Appends the bid selection method to the filter, this will filter based on
     * tender.procurementMethodDetails. It accepts multiple elements
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getBidSelectionMethod(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("tender.procurementMethodDetails", filter.getBidSelectionMethod(), filter);
    }

    protected Criteria getDefaultFilterCriteria(final DefaultFilterPagingRequest filter) {
        return new Criteria().andOperator(getBidTypeIdFilterCriteria(filter), getProcuringEntityIdCriteria(filter),
                getBidSelectionMethod(filter), getContrMethodFilterCriteria(filter));
    }

    protected MatchOperation getMatchDefaultFilterOperation(final DefaultFilterPagingRequest filter) {
        return match(getDefaultFilterCriteria(filter));
    }

    /**
     * Creates a groupby expression that takes into account the filter. It will
     * only use one of the filter options as groupby and ignores the rest.
     *
     * @param filter
     * @param existingGroupBy
     * @return
     */
    protected GroupOperation getTopXFilterOperation(final GroupingFilterPagingRequest filter,
                                                    final String... existingGroupBy) {
        List<String> groupBy = new ArrayList<>();
        if (filter.getGroupByCategory() == null) {
            groupBy.addAll(Arrays.asList(existingGroupBy));
        }

        if (filter.getGroupByCategory() != null) {
            groupBy.add(getGroupByCategory(filter));
        }

        return group(groupBy.toArray(new String[0]));
    }

    @Deprecated
    protected AggregationOperation getTopXFilterOperation(final GroupingFilterPagingRequest filter,
                                                          final DBObject group) {
        if (filter.getGroupByCategory() != null) {
            group.removeField(Fields.UNDERSCORE_ID);
            group.put(Fields.UNDERSCORE_ID, "$" + getGroupByCategory(filter));
        }
        return new CustomOperation(new BasicDBObject("$group", group));
    }

    private String getGroupByCategory(final GroupingFilterPagingRequest filter) {
        if ("bidSelectionMethod".equals(filter.getGroupByCategory())) {
            return "tender.procurementMethodDetails";
        } else if ("bidTypeId".equals(filter.getGroupByCategory())) {
            return "tender.items.classification._id";
        } else if ("procuringEntityId".equals(filter.getGroupByCategory())) {
            return "tender.procuringEntity._id";
        }
        return null;
    }

}
