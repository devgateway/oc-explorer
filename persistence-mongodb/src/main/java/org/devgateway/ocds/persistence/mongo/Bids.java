package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.merge.Merge;
import org.devgateway.ocds.persistence.mongo.merge.MergeStrategy;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Bids
 * <p>
 * Summary and detailed information about bids received and evaluated as part of this contracting process.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "statistics",
        "details"
})
public class Bids {

    /**
     * Statistics
     * <p>
     * Summary statistics on the number and nature of bids received. Where information is provided on individual
     * bids, these statistics should match those that can be calculated from the bid details array.
     */
    @JsonProperty("statistics")
    @JsonPropertyDescription("Summary statistics on the number and nature of bids received. Where information"
            + " is provided on individual bids, these statistics should match those that can be calculated from the "
            + "bid details array.")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @Merge(MergeStrategy.ocdsVersion)
    private Set<Statistic> statistics = new LinkedHashSet<>();
    /**
     * Bid details
     * <p>
     * An array of bids, providing information on the bidders, and where applicable, bid status, bid values and
     * related documents. The extent to which this information can be disclosed varies from jurisdiction
     * to jurisdiction.
     */
    @JsonProperty("details")
    @JsonPropertyDescription("An array of bids, providing information on the bidders, and where applicable,"
            + " bid status, bid values and related documents. The extent to which this information can be disclosed "
            + "varies from jurisdiction to jurisdiction.")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @Merge(MergeStrategy.ocdsVersion)
    private Set<Detail> details = new LinkedHashSet<>();

    /**
     * Statistics
     * <p>
     * Summary statistics on the number and nature of bids received. Where information is provided on individual
     * bids, these statistics should match those that can be calculated from the bid details array.
     */
    @JsonProperty("statistics")
    public Set<Statistic> getStatistics() {
        return statistics;
    }

    /**
     * Statistics
     * <p>
     * Summary statistics on the number and nature of bids received. Where information is provided on individual
     * bids, these statistics should match those that can be calculated from the bid details array.
     */
    @JsonProperty("statistics")
    public void setStatistics(Set<Statistic> statistics) {
        this.statistics = statistics;
    }

    /**
     * Bid details
     * <p>
     * An array of bids, providing information on the bidders, and where applicable, bid status, bid values
     * and related documents. The extent to which this information can be disclosed varies from jurisdiction
     * to jurisdiction.
     */
    @JsonProperty("details")
    public Set<Detail> getDetails() {
        return details;
    }

    /**
     * Bid details
     * <p>
     * An array of bids, providing information on the bidders, and where applicable, bid status, bid values
     * and related documents. The extent to which this information can be disclosed varies from jurisdiction
     * to jurisdiction.
     */
    @JsonProperty("details")
    public void setDetails(Set<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(statistics).append(details).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Bids)) {
            return false;
        }
        Bids rhs = ((Bids) other);
        return new EqualsBuilder().append(statistics, rhs.statistics).append(details, rhs.details)
                .isEquals();
    }

}
