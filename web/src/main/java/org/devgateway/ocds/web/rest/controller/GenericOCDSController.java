/**
 *
 */
package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.ArrayUtils;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.GroupingFilterPagingRequest;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomSortingOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.FLAGS_TOTAL_FLAGGED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
public abstract class GenericOCDSController {

    private static final int LAST_MONTH_ZERO = 11;
    public static final int BIGDECIMAL_SCALE = 15;
    public static final int DAY_MS = 86400000;

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);


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

    protected <Z> List<Z> releaseAgg(Aggregation agg, AggregationOptions options, Class<Z> clazz) {
        return mongoTemplate.aggregate(agg.withOptions(options), "release", clazz)
                .getMappedResults();
    }

    protected List<DBObject> releaseAgg(Aggregation agg) {
        return releaseAgg(agg, DBObject.class);
    }

    protected List<DBObject> releaseAgg(Aggregation agg, AggregationOptions options) {
        return releaseAgg(agg, options, DBObject.class);
    }

    protected <Z> List<Z> releaseAgg(Aggregation agg, Class<Z> clazz) {
        return releaseAgg(agg, Aggregation.newAggregationOptions().allowDiskUse(true).build(), clazz);
    }

    /**
     * Creates a mongo $cond that calculates percentage of expression1/expression2.
     * Checks for division by zero.
     *
     * @param expression1
     * @param expression2
     * @return
     */
    protected DBObject getPercentageMongoOp(String expression1, String expression2) {
        return new BasicDBObject(
                "$cond",
                Arrays.asList(new BasicDBObject("$eq", Arrays.asList(
                        ref(expression2),
                        0
                        )), new BasicDBObject("$literal", 0),
                        new BasicDBObject(
                                "$multiply",
                                Arrays.asList(new BasicDBObject(
                                        "$divide",
                                        Arrays.asList(
                                                ref(expression1),
                                                ref(expression2)
                                        )
                                ), 100)
                        )
                )
        );

    }

    /**
     * This is used to build the start date filter query when a monthly filter is used.
     *
     * @param year
     * @param month
     * @return
     */
    protected Date getMonthStartDate(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        return start;
    }

    /**
     * This is used to build the end date filter query when a monthly filter is used.
     *
     * @param year
     * @param month
     * @return
     */
    protected Date getMonthEndDate(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date end = cal.getTime();
        return end;
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
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date end = cal.getTime();
        return end;
    }

    protected String ref(String field) {
        return "$" + field;
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

    protected Criteria getTotalFlaggedCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria(FLAGS_TOTAL_FLAGGED, filter.getTotalFlagged(), filter);
    }

    /**
     * Appends flags.flaggedStats.type filter
     */
    protected Criteria getFlagTypeFilterCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("flags.flaggedStats.type", filter.getFlagType(), filter);
    }

    protected Criteria getAwardStatusFilterCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria(MongoConstants.FieldNames.AWARDS_STATUS, filter.getAwardStatus(), filter);
    }


    /**
     * Adds monthly projection operation, when needed, if the
     * {@link YearFilterPagingRequest#getMonthly()}
     *
     * @param filter
     * @param project
     * @param field
     */
    protected void addYearlyMonthlyProjection(YearFilterPagingRequest filter, DBObject project, String field) {
        project.put("year", new BasicDBObject("$year", field));
        if (filter.getMonthly()) {
            project.put(("month"), new BasicDBObject("$month", field));
        }
    }

    protected CustomSortingOperation getSortByYearMonth(YearFilterPagingRequest filter) {
        DBObject sort = new BasicDBObject();
        if (filter.getMonthly()) {
            sort.put("_id.year", 1);
            sort.put("_id.month", 1);
        } else {
            sort.put("year", 1);
        }
        return new CustomSortingOperation(sort);
    }

    /**
     * Similar to {@link #getSortByYearMonth(YearFilterPagingRequest)} but it can be used
     * if additional grouping elements are present, besides month and year
     *
     * @param filter
     * @return
     */
    protected CustomSortingOperation getSortByYearMonthWhenOtherGroups(YearFilterPagingRequest filter,
                                                                       String... otherSort) {
        DBObject sort = new BasicDBObject();
        if (filter.getMonthly()) {
            sort.put("_id.year", 1);
            sort.put("_id.month", 1);
        } else {
            sort.put("_id.year", 1);
        }
        if (otherSort != null) {
            Arrays.asList(otherSort).forEach(s -> sort.put(s, 1));
        }
        return new CustomSortingOperation(sort);
    }


    protected void addYearlyMonthlyReferenceToGroup(YearFilterPagingRequest filter, DBObject group) {
        if (filter.getMonthly()) {
            group.put(Fields.UNDERSCORE_ID, new BasicDBObject("year", "$year").append("month", "$month"));
        } else {
            group.put(Fields.UNDERSCORE_ID, "$year");
        }
    }

    /**
     * Returns the grouping fields based on the {@link YearFilterPagingRequest#getMonthly()} setting
     *
     * @param filter
     * @return
     */
    protected String[] getYearlyMonthlyGroupingFields(YearFilterPagingRequest filter) {
        if (filter.getMonthly()) {
            return new String[]{"$year", "$month"};
        } else {
            return new String[]{"$year"};
        }
    }

    /**
     * @param filter
     * @param extraGroups adds extra groups
     * @return
     * @see #getYearlyMonthlyGroupingFields(YearFilterPagingRequest)
     */
    protected String[] getYearlyMonthlyGroupingFields(YearFilterPagingRequest filter, String... extraGroups) {
        return ArrayUtils.addAll(getYearlyMonthlyGroupingFields(filter), extraGroups);
    }

    protected GroupOperation getYearlyMonthlyGroupingOperation(YearFilterPagingRequest filter) {
        return group(getYearlyMonthlyGroupingFields(filter));
    }

    protected ProjectionOperation transformYearlyGrouping(YearFilterPagingRequest filter) {
        if (filter.getMonthly()) {
            return project();
        } else {
            return project(Fields.from(
                    Fields.field("year", org.springframework.data
                            .mongodb.core.aggregation.Fields.UNDERSCORE_ID_REF)))
                    .andExclude(Fields.UNDERSCORE_ID);
        }
    }

    protected void addYearlyMonthlyGroupingOperationFirst(YearFilterPagingRequest filter, DBObject group) {
        group.put("year", new BasicDBObject("$first", "$year"));
        if (filter.getMonthly()) {
            group.put("month", new BasicDBObject("$first", "$month"));
        }
    }

    protected Criteria getNotBidTypeIdFilterCriteria(final DefaultFilterPagingRequest filter) {
        return createNotFilterCriteria("tender.items.classification._id", filter.getNotBidTypeId(), filter);
    }


    /**
     * Appends the tender.items.deliveryLocation._id
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getByTenderDeliveryLocationIdentifier(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("tender.items.deliveryLocation._id",
                filter.getTenderLoc(), filter
        );
    }

    /**
     * Creates a search criteria filter based on tender.value.amount and uses
     * {@link DefaultFilterPagingRequest#getMinTenderValue()} and
     * {@link DefaultFilterPagingRequest#getMaxTenderValue()} to create
     * interval search
     *
     * @param filter
     * @return
     */
    protected Criteria getByTenderAmountIntervalCriteria(final DefaultFilterPagingRequest filter) {
        if (filter.getMaxTenderValue() == null && filter.getMinTenderValue() == null) {
            return new Criteria();
        }
        Criteria criteria = where(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT);
        if (filter.getMinTenderValue() != null) {
            criteria = criteria.gte(filter.getMinTenderValue().doubleValue());
        }
        if (filter.getMaxTenderValue() != null) {
            criteria = criteria.lte(filter.getMaxTenderValue().doubleValue());
        }
        return criteria;
    }

    /**
     * Creates a search criteria filter based on awards.value.amount and uses
     * {@link DefaultFilterPagingRequest#getMinAwardValue()} and
     * {@link DefaultFilterPagingRequest#getMaxAwardValue()} to create
     * interval search
     *
     * @param filter
     * @return
     */
    protected Criteria getByAwardAmountIntervalCriteria(final DefaultFilterPagingRequest filter) {
        if (filter.getMaxAwardValue() == null && filter.getMinAwardValue() == null) {
            return new Criteria();
        }
        Criteria elem = where("value.amount");

        if (filter.getMinAwardValue() != null) {
            elem = elem.gte(filter.getMinAwardValue().doubleValue());
        }
        if (filter.getMaxAwardValue() != null) {
            elem = elem.lte(filter.getMaxAwardValue().doubleValue());
        }
        return where("awards").elemMatch(elem);
    }

    private <S> Criteria createFilterCriteria(final String filterName, final Set<S> filterValues,
                                              final DefaultFilterPagingRequest filter) {
        if (filterValues == null) {
            return new Criteria();
        }
        return where(filterName).in(filterValues.toArray());
    }

    private <S> Criteria createNotFilterCriteria(final String filterName, final Set<S> filterValues,
                                                 final DefaultFilterPagingRequest filter) {
        if (filterValues == null) {
            return new Criteria();
        }
        return where(filterName).not().in(filterValues.toArray());
    }

    /**
     * Appends the procuring entity id for this filter, this will fitler based
     * on tender.procuringEntity._id
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getProcuringEntityIdCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria(
                MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID, filter.getProcuringEntityId(), filter);
    }

    protected CriteriaDefinition getTextCriteria(DefaultFilterPagingRequest filter) {
        if (ObjectUtils.isEmpty(filter.getText()) || filter.getAwardFiltering()) {
            return new Criteria();
        } else {
            return TextCriteria.forLanguage(MongoConstants.MONGO_LANGUAGE).matchingAny(filter.getText());
        }
    }

    /**
     * Adds the filter by electronic submission criteria for tender.submissionMethod.
     *
     * @param filter
     * @return
     */
    protected Criteria getElectronicSubmissionCriteria(final DefaultFilterPagingRequest filter) {
        if (filter.getElectronicSubmission() != null && filter.getElectronicSubmission()) {
            return where(MongoConstants.FieldNames.TENDER_SUBMISSION_METHOD).is(
                    Tender.SubmissionMethod.electronicSubmission.toString());
        }

        return new Criteria();
    }

    /**
     * Add the filter by flagged
     *
     * @param filter
     * @return
     */
    protected Criteria getFlaggedCriteria(final DefaultFilterPagingRequest filter) {
        if (filter.getFlagged() != null && filter.getFlagged()) {
            return where("flags.flaggedStats.0").exists(true);
        }

        return new Criteria();
    }

    protected Criteria getNotProcuringEntityIdCriteria(final DefaultFilterPagingRequest filter) {
        return createNotFilterCriteria(
                MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID, filter.getNotProcuringEntityId(), filter);
    }


    /**
     * Appends the supplier entity id for this filter, this will fitler based
     * on tender.procuringEntity._id
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getSupplierIdCriteria(final DefaultFilterPagingRequest filter) {
        if (filter.getAwardFiltering()) {
            return createFilterCriteria(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID, filter.getSupplierId(), filter);
        }
        if (filter.getSupplierId() == null) {
            return new Criteria();
        }
        return where("awards").elemMatch(
                where("status").is(Award.Status.active.toString()).and("suppliers._id").in(filter.getSupplierId()));
    }


    protected Criteria getBidderIdCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria("bids.details.tenderers._id", filter.getBidderId(), filter);
    }

    /**
     * Appends the procurement method for this filter, this will fitler based
     * on tender.procurementMethod
     *
     * @param filter
     * @return the {@link Criteria} for this filter
     */
    protected Criteria getProcurementMethodCriteria(final DefaultFilterPagingRequest filter) {
        return createFilterCriteria(
                MongoConstants.FieldNames.TENDER_PROC_METHOD, filter.getProcurementMethod(), filter);
    }

    @PostConstruct
    protected void init() {
        Map<String, Object> tmpMap = new HashMap<>();
        tmpMap.put(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID, 1);
        tmpMap.put(MongoConstants.FieldNames.TENDER_PROC_METHOD, 1);
        tmpMap.put(MongoConstants.FieldNames.TENDER_SUBMISSION_METHOD, 1);
        tmpMap.put(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID, 1);
        tmpMap.put("tender.items.classification._id", 1);
        tmpMap.put("tender.items.deliveryLocation._id", 1);
        tmpMap.put(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT, 1);
        tmpMap.put(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT, 1);

        filterProjectMap = Collections.unmodifiableMap(tmpMap);
    }

    protected Criteria getYearFilterCriteria(final YearFilterPagingRequest filter, final String dateProperty) {
        Criteria[] yearCriteria = null;
        Criteria criteria = new Criteria();

        if (filter.getYear() == null) {
            yearCriteria = new Criteria[1];
            yearCriteria[0] = new Criteria();
        } else {
            yearCriteria = new Criteria[filter.getYear().size()];
            Integer[] yearArray = filter.getYear().toArray(new Integer[0]);
            for (int i = 0; i < yearArray.length; i++) {
                yearCriteria[i] = where(dateProperty).gte(getStartDate(yearArray[i]))
                        .lte(getEndDate(yearArray[i]));
            }
            criteria = criteria.orOperator(yearCriteria);

            if (filter.getMonth() != null && filter.getYear().size() == 1) {
                Integer[] monthArray = filter.getMonth().toArray(new Integer[0]);
                criteria = new Criteria(); //we reset the criteria because we use only one year
                Criteria[] monthCriteria = new Criteria[filter.getMonth().size()];
                for (int i = 0; i < monthArray.length; i++) {
                    monthCriteria[i] = where(dateProperty).gte(getMonthStartDate(
                            yearArray[0],
                            monthArray[i]
                    ))
                            .lte(getMonthEndDate(
                                    yearArray[0],
                                    monthArray[i]
                            ));
                }
                criteria = criteria.orOperator(monthCriteria);
            }
        }

//        logger.info("Criteria=" + criteria.getCriteriaObject());

        return criteria;
    }


    protected Map<String, CriteriaDefinition> createDefaultFilterCriteriaMap(final DefaultFilterPagingRequest filter) {
        HashMap<String, CriteriaDefinition> map = new HashMap<>();
        map.put(MongoConstants.Filters.BID_TYPE_ID, getBidTypeIdFilterCriteria(filter));
        map.put(MongoConstants.Filters.NOT_BID_TYPE_ID, getNotBidTypeIdFilterCriteria(filter));
        map.put(MongoConstants.Filters.PROCURING_ENTITY_ID, getProcuringEntityIdCriteria(filter));
        map.put(MongoConstants.Filters.NOT_PROCURING_ENTITY_ID, getNotProcuringEntityIdCriteria(filter));
        map.put(MongoConstants.Filters.SUPPLIER_ID, getSupplierIdCriteria(filter));
        map.put(MongoConstants.Filters.PROCUREMENT_METHOD, getProcurementMethodCriteria(filter));
        map.put(MongoConstants.Filters.TENDER_LOC, getByTenderDeliveryLocationIdentifier(filter));
        map.put(MongoConstants.Filters.TENDER_VALUE, getByTenderAmountIntervalCriteria(filter));
        map.put(MongoConstants.Filters.AWARD_VALUE, getByAwardAmountIntervalCriteria(filter));
        map.put(MongoConstants.Filters.FLAGGED, getFlaggedCriteria(filter));
        map.put(MongoConstants.Filters.FLAG_TYPE, getFlagTypeFilterCriteria(filter));
        map.put(MongoConstants.Filters.ELECTRONIC_SUBMISSION, getElectronicSubmissionCriteria(filter));
        map.put(MongoConstants.Filters.AWARD_STATUS, getAwardStatusFilterCriteria(filter));
        map.put(MongoConstants.Filters.BIDDER_ID, getBidderIdCriteria(filter));
        map.put(MongoConstants.Filters.TOTAL_FLAGGED, getTotalFlaggedCriteria(filter));
        map.put(MongoConstants.Filters.TEXT, getTextCriteria(filter));
        return map;
    }


    /**
     * Removes {@link Criteria} objects that were generated empty because they are not needed and they seem to slow
     * down the mongodb query engine by a lot.
     *
     * @param values
     * @return
     */
    protected CriteriaDefinition[] getEmptyFilteredCriteria(Collection<CriteriaDefinition> values) {
        return values.stream().distinct().toArray(CriteriaDefinition[]::new);
    }

    /**
     * We ensure {@link TextCriteria} is always returned first before {@link Criteria}
     *
     * @param criteria
     * @return
     * @see Criteria#createCriteriaList(Criteria[])
     */
    private BasicDBList createTextSearchFriendlyCriteriaList(CriteriaDefinition[] criteria) {
        BasicDBList bsonList = new BasicDBList();
        Arrays.stream(criteria).sorted((c1, c2) -> c2.getClass().getSimpleName()
                .compareTo(c1.getClass().getSimpleName()))
                .map(CriteriaDefinition::getCriteriaObject).collect(Collectors.toCollection(() -> bsonList));
        return bsonList;
    }

    protected Criteria getDefaultFilterCriteria(final DefaultFilterPagingRequest filter,
                                                Map<String, CriteriaDefinition> map) {
        return getAndOperatorCriteriaDefinition(map.values());
    }

    /**
     * For some reason the mongodb spring team did not add support for this, probably because the $text
     * criteria has some limitations, has to be always first in a list of criteria, etc...
     *
     * @param criteriaDefinitions
     * @return
     */
    protected Criteria getAndOperatorCriteriaDefinition(Collection<CriteriaDefinition> criteriaDefinitions) {
        return new Criteria("$and").is(createTextSearchFriendlyCriteriaList(getEmptyFilteredCriteria(
                criteriaDefinitions)));
    }

    protected Criteria getDefaultFilterCriteria(final DefaultFilterPagingRequest filter) {
        return getDefaultFilterCriteria(filter, createDefaultFilterCriteriaMap(filter));
    }

    protected Criteria getYearDefaultFilterCriteria(final YearFilterPagingRequest filter, final String dateProperty) {
        return getYearDefaultFilterCriteria(filter, createDefaultFilterCriteriaMap(filter), dateProperty);
    }

    protected Criteria getYearDefaultFilterCriteria(final YearFilterPagingRequest filter,
                                                    Map<String, CriteriaDefinition> map,
                                                    final String dateProperty) {
        map.put(MongoConstants.Filters.YEAR, getYearFilterCriteria(filter, dateProperty));
        return getAndOperatorCriteriaDefinition(map.values());
    }

    protected MatchOperation getMatchDefaultFilterOperation(final DefaultFilterPagingRequest filter) {
        return match(getDefaultFilterCriteria(filter));
    }


    protected MatchOperation getMatchYearDefaultFilterOperation(final YearFilterPagingRequest filter,
                                                                final String dateProperty) {
        return match(getYearDefaultFilterCriteria(filter, dateProperty));
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

    private String getGroupByCategory(final GroupingFilterPagingRequest filter) {
        if ("bidTypeId".equals(filter.getGroupByCategory())) {
            return "tender.items.classification._id".replace(".", "");
        } else if ("procuringEntityId".equals(filter.getGroupByCategory())) {
            return MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID.replace(".", "");
        }
        return null;
    }

}
