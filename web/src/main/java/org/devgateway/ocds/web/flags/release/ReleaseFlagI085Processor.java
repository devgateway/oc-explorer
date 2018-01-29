package org.devgateway.ocds.web.flags.release;

import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Detail;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.FlagType;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mpostelnicu
 * <p>
 * i085 Bids are an exact percentage apart
 */
@Component
public class ReleaseFlagI085Processor extends AbstractFlaggedReleaseFlagProcessor {

    public static final BigDecimal I085_LOW_THRESH = BigDecimal.valueOf(0.2);
    public static final BigDecimal I085_HI_THRESH = BigDecimal.valueOf(0.8);

    @PostConstruct
    @Override
    protected void setPredicates() {
        preconditionsPredicates = Collections.synchronizedList(Arrays.asList(
                FlaggedReleasePredicates.ACTIVE_AWARD,
                FlaggedReleasePredicates.ELECTRONIC_SUBMISSION
        ));
    }

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI085(flag);
    }

    @Override
    protected Set<FlagType> flagTypes() {
        return new HashSet<FlagType>(Arrays.asList(FlagType.FRAUD, FlagType.COLLUSION));
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        boolean result = false;

        for (Detail bid : flaggable.getBids().getDetails()) {
            for (Award award : flaggable.getAwards()) {
                if (!Award.Status.active.equals(award.getStatus()) || bid.getValue() == null
                        || bid.getValue().getAmount() == null || award.getValue() == null || award.getValue()
                        .getAmount() == null || !award.getValue().getCurrency().equals(bid.getValue().getCurrency())
                        || award.getValue().getAmount().equals(bid.getValue().getAmount())) {
                    continue;
                }

                BigDecimal d = award.getValue().getAmount().subtract(bid.getValue().getAmount())
                        .abs().divide(award.getValue().getAmount().max(bid.getValue().getAmount()),
                                5, BigDecimal.ROUND_HALF_UP);
                if (d.compareTo(I085_HI_THRESH) > 0 || d.compareTo(I085_LOW_THRESH) < 0) {
                    continue;
                }


                BigDecimal dLeft = relativeDistanceLeft(bid.getValue().getAmount(), award.getValue().getAmount()).
                        multiply(GenericOCDSController.ONE_HUNDRED);

                BigDecimal dRight = relativeDistanceRight(bid.getValue().getAmount(), award.getValue().getAmount()).
                        multiply(GenericOCDSController.ONE_HUNDRED);

                //using the same logic as owen here...
                if (dLeft.doubleValue() % 1 == 0 || dRight.doubleValue() % 1 == 0) {
                    result = true;
                    rationale.append("Award=").append(award.getValue().getAmount().setScale(
                            5, BigDecimal.ROUND_HALF_UP))
                            .append(" with bid=").append(bid.getValue().getAmount().setScale(
                            5, BigDecimal.ROUND_HALF_UP))
                            .append("; ");
                    break;
                }
            }
        }
        return result;
    }

}