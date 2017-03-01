/**
 *
 */
package org.devgateway.ocds.persistence.mongo.flags;

import java.util.List;

/**
 * @author mpostelnicu
 *         Interface designating the wrapper entity holding the flags
 */
public interface FlagsWrappable {

    List<FlagTypeCount> getFlaggedTypeCounts();

    List<FlagTypeCount> getEligibleTypeCounts();

}
