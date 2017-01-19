package org.devgateway.ocds.web.flags.release;


import java.util.Arrays;
import java.util.Collections;
import javax.annotation.PostConstruct;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.springframework.stereotype.Component;

/**
 * @author mpostelnicu
 * 
 * i077 High number of contract awards to one supplier within a given time period by a single procurement entity
 */
@Component
public class ReleaseFlagI077Processor extends AbstractFlaggedReleaseFlagProcessor {

//    @Autowired
  //  FrequentSuppliersTimeIntervalController frequentSuppliersTimeIntervalController;



    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI077(flag);
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        return false;
    }

    @PostConstruct
    @Override
    protected void setPredicates() {
        preconditionsPredicates = Collections.synchronizedList(
                Arrays.asList(FlaggedReleasePredicates.ACTIVE_AWARD_WITH_DATE,
                        FlaggedReleasePredicates.TENDER_PROCURING_ENTITY));
    }

}