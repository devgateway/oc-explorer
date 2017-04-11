package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private List<Statistic> statistics = new ArrayList<Statistic>();
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
    private List<Detail> details = new ArrayList<Detail>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Statistics
     * <p>
     * Summary statistics on the number and nature of bids received. Where information is provided on individual
     * bids, these statistics should match those that can be calculated from the bid details array.
     */
    @JsonProperty("statistics")
    public List<Statistic> getStatistics() {
        return statistics;
    }

    /**
     * Statistics
     * <p>
     * Summary statistics on the number and nature of bids received. Where information is provided on individual
     * bids, these statistics should match those that can be calculated from the bid details array.
     */
    @JsonProperty("statistics")
    public void setStatistics(List<Statistic> statistics) {
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
    public List<Detail> getDetails() {
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
    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(statistics).append(details).append(additionalProperties).toHashCode();
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
                .append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
