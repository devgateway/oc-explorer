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
package org.devgateway.ocds.web.rest.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.AWARDS_SUPPLIERS_NAME;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.BIDS_DETAILS_TENDERERS_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.BIDS_DETAILS_VALUE_AMOUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.FLAGS_TOTAL_FLAGGED;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.TENDER_PERIOD_START_DATE;
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
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class AwardsWonLostController extends GenericOCDSController {

    protected List<AggregationOperation> suppliersByFlagsGroupPart(final YearFilterPagingRequest filter) {
        List<AggregationOperation> part = new ArrayList<>();
        part.add(match(getYearDefaultFilterCriteria(filter, TENDER_PERIOD_START_DATE)
                .and(FLAGS_TOTAL_FLAGGED).gt(0)));
        part.add(unwind("awards"));
        part.add(unwind("awards.suppliers"));
        part.add(match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                .andOperator(getYearDefaultFilterCriteria(
                        filter.awardFiltering(),
                        TENDER_PERIOD_START_DATE
                ))));
        part.add(group(Fields.from(
                Fields.field("supplierId", AWARDS_SUPPLIERS_ID),
                Fields.field("supplierName", AWARDS_SUPPLIERS_NAME))).sum(FLAGS_TOTAL_FLAGGED)
                .as("countFlags")
        );
        return part;
    }


    @ApiOperation(value = "Suppliers ordered by countFlags>0, descending")
    @RequestMapping(value = "/api/suppliersByFlags",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> suppliersByFlags(@ModelAttribute @Valid final YearFilterPagingRequest filter) {
        List<AggregationOperation> part = suppliersByFlagsGroupPart(filter);
        part.add(sort(Sort.Direction.DESC, "countFlags"));
        part.add(skip(filter.getSkip()));
        part.add(limit(filter.getPageSize()));
        return releaseAgg(newAggregation(part));
    }

    @ApiOperation(value = "Counts Suppliers ordered by countFlags>0, descending")
    @RequestMapping(value = "/api/suppliersByFlags/count",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> suppliersByFlagsCount(@ModelAttribute @Valid final YearFilterPagingRequest filter) {
        List<AggregationOperation> part = suppliersByFlagsGroupPart(filter);
        part.add(group().count().as("count"));
        part.add(project("count").andExclude(Fields.UNDERSCORE_ID));
        return releaseAgg(newAggregation(part));
    }

    @ApiOperation(value = "Counts the won, lost procurements, flags and amounts. Receives any filters, "
            + "but most important here is the supplierId and bidderId. Requires bid extension. Use bidderId instead "
            + "of supplierId.")
    @RequestMapping(value = "/api/procurementsWonLost",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<ProcurementsWonLost> procurementsWonLost(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Assert.notEmpty(filter.getBidderId(), "bidderId must not be empty!");
        Assert.isTrue(
                CollectionUtils.isEmpty(filter.getSupplierId()),
                "supplierId is not allowed here! Use bidderId to show results!"
        );

        //supplier is the same thing as bidder for this particular query
        filter.setSupplierId(filter.getBidderId());

        Map<String, CriteriaDefinition> noSupplierCriteria =
                createDefaultFilterCriteriaMap(
                        filter);
        noSupplierCriteria.remove(MongoConstants.Filters.SUPPLIER_ID);

        Aggregation agg1 = newAggregation(
                match(getYearDefaultFilterCriteria(
                        filter,
                        noSupplierCriteria,
                        TENDER_PERIOD_START_DATE
                )),
                unwind("bids.details"),
                unwind("bids.details.tenderers"),
                match(getYearDefaultFilterCriteria(
                        filter,
                        noSupplierCriteria,
                        TENDER_PERIOD_START_DATE
                )),
                group(BIDS_DETAILS_TENDERERS_ID).count().as("count")
                        .sum(BIDS_DETAILS_VALUE_AMOUNT).as("totalAmount")
                        .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                project("count", "totalAmount", "countFlags")
        );


        List<CountAmountFlags> applied = releaseAgg(agg1, CountAmountFlags.class);


        Aggregation agg2 = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                TENDER_PERIOD_START_DATE
                        ))),
                unwind("awards"),
                unwind("awards.suppliers"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter.awardFiltering(),
                                TENDER_PERIOD_START_DATE
                        ))),
                group(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID).count().as("count")
                        .sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("totalAmount")
                        .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                project("count", "totalAmount", "countFlags")
        );

        List<CountAmountFlags> won = releaseAgg(agg2, CountAmountFlags.class);

        ArrayList<ProcurementsWonLost> ret = new ArrayList<>();

        applied.forEach(a -> {
            ProcurementsWonLost r = new ProcurementsWonLost();
            r.setApplied(a);
            Optional<CountAmountFlags> optWon = won.stream().filter(w -> w.getId().equals(a.getId())).findFirst();
            if (optWon.isPresent()) {
                r.setWon(optWon.get());
                r.setLostAmount(r.getApplied().getTotalAmount().subtract(r.getWon().getTotalAmount()));
                r.setLostCount(r.getApplied().getCount() - r.getWon().getCount());
            } else {
                r.setLostAmount(r.getApplied().getTotalAmount());
                r.setLostCount(r.getLostCount());
            }
            ret.add(r);
        });

        return ret;

    }

    @ApiOperation(value = "Counts the number of wins per supplierId per procuringEntityId, plus shows the flags"
            + " and the awarded total. You must provide supplierId parameter. Any other filter can be used as well.")
    @RequestMapping(value = "/api/supplierWinsPerProcuringEntity",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> procurementsWonLostPerProcuringEntity(@ModelAttribute @Valid final YearFilterPagingRequest
                                                                        filter) {

        Assert.notEmpty(filter.getSupplierId(), "supplierId must not be empty!");

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                TENDER_PERIOD_START_DATE
                        )).and(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID).exists(true)
                        .and(MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_NAME).exists(true)),
                unwind("awards"),
                unwind("awards.suppliers"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter.awardFiltering(),
                                TENDER_PERIOD_START_DATE
                        ))),
                group(Fields.from(
                        Fields.field("supplierId", MongoConstants
                                .FieldNames.AWARDS_SUPPLIERS_ID),
                        Fields.field("procuringEntityName", MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_NAME),
                        Fields.field("procuringEntityId", MongoConstants.FieldNames.TENDER_PROCURING_ENTITY_ID)
                ))
                        .count().as("count")
                        .sum(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).as("totalAmountAwarded")
                        .sum(FLAGS_TOTAL_FLAGGED).as("countFlags"),
                sort(Sort.Direction.DESC, "count")
        );
        return releaseAgg(agg);
    }

    public static class ProcurementsWonLost implements Serializable {
        private CountAmountFlags applied;
        private CountAmountFlags won;
        private Long lostCount = 0L;
        private BigDecimal lostAmount = BigDecimal.ZERO;

        public CountAmountFlags getApplied() {
            return applied;
        }

        public void setApplied(CountAmountFlags applied) {
            this.applied = applied;
        }

        public CountAmountFlags getWon() {
            return won;
        }

        public void setWon(CountAmountFlags won) {
            this.won = won;
        }

        public BigDecimal getLostAmount() {
            return lostAmount;
        }

        public void setLostAmount(BigDecimal lostAmount) {
            this.lostAmount = lostAmount;
        }

        public Long getLostCount() {
            return lostCount;
        }

        public void setLostCount(Long lostCount) {
            this.lostCount = lostCount;
        }
    }

    public static class CountAmountFlags implements Serializable {
        private String id;
        private Long count;
        private BigDecimal totalAmount;
        private Long countFlags;

        @JsonProperty("_id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Long getCountFlags() {
            return countFlags;
        }

        public void setCountFlags(Long countFlags) {
            this.countFlags = countFlags;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }
    }


}