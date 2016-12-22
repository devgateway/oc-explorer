package org.devgateway.ocds.persistence.mongo.flags.processors.release;

import com.google.common.collect.ImmutableMap;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author mpostelnicu
 *         <p>
 *         i004 Sole source award above the threshold
 */
public abstract class ReleaseFlagI004Processor extends AbstractFlaggedReleaseFlagProcessor {

    public abstract ImmutableMap<String, BigDecimal> getSolesourceLimits();

    public ReleaseFlagI004Processor() {
        preconditionsPredicates = Collections.synchronizedList(Arrays.asList(
                FlaggedReleasePredicates.ACTIVE_AWARD,
                FlaggedReleasePredicates.LIMITED_PROCUREMENT_METHOD,
                FlaggedReleasePredicates.AWARDED_AMOUNT,
                FlaggedReleasePredicates.TENDER_ITEMS_CLASSIFICATION
        ));
    }

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI004(flag);
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        //classificationIds are the same for all items, so we just get the 1st
        String classificationId = flaggable.getTender().getItems().stream()
                .findFirst().get().getClassification().getId();

        BigDecimal limit = getSolesourceLimits().get(classificationId);
        if (limit == null) {
            rationale.append("Classification is null");
            return false;
        }
        boolean limitReached = flaggable.getAwards().stream().anyMatch(a -> a.getValue() != null
                && limit.compareTo(a.getValue().getAmount()) == -1);

        rationale.append("Classification ").append(classificationId).append(" limit of ")
                .append(limit).append(limitReached ? " reached." : " not reached");

        return limitReached;
    }


}