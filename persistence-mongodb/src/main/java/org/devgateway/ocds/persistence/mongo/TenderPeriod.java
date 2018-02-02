package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;


/**
 * Period
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "startDate",
        "endDate",
        "maxExtentDate",
        "durationInDays"
})
public class TenderPeriod {

    /**
     * Start date
     * <p>
     * The start date for the period. When known, a precise start date must always be provided.
     */
    @JsonProperty("startDate")
    @JsonPropertyDescription("The start date for the period. When known, a precise start date must always be provided.")
    private Date startDate;
    /**
     * End date
     * <p>
     * The end date for the period. When known, a precise end date must always be provided.
     */
    @JsonProperty("endDate")
    @JsonPropertyDescription("The end date for the period. When known, a precise end date must always be provided.")
    private Date endDate;
    /**
     * Maximum extent
     * <p>
     * The period cannot be extended beyond this date. This field is optional, and can be used to express the maximum
     * available data for extension or renewal of this period.
     */
    @JsonProperty("maxExtentDate")
    @JsonPropertyDescription("The period cannot be extended beyond this date. This field is optional, and can be used"
            + " to express the maximum available data for extension or renewal of this period.")
    private Date maxExtentDate;
    /**
     * Duration (days)
     * <p>
     * The maximum duration of this period in days. A user interface may wish to collect or display this data in
     * months or years as appropriate, but should convert it into days when completing this field. This field can be
     * used when exact dates are not known.  Where a startDate and endDate are given, this field is optional, and
     * should reflect the difference between those two days. Where a startDate and maxExtentDate are given, this
     * field is optional, and should reflect the difference between startDate and maxExtentDate.
     */
    @JsonProperty("durationInDays")
    @JsonPropertyDescription("The maximum duration of this period in days. A user interface may wish to collect or "
            + "display this data in months or years as appropriate, but should convert it into days when completing "
            + "this field. This field can be used when exact dates are not known.  Where a startDate and endDate are "
            + "given, this field is optional, and should reflect the difference between those two days. Where a "
            + "startDate and maxExtentDate are given, this field is optional, and should reflect the difference "
            + "between startDate and maxExtentDate.")
    private Integer durationInDays;

    /**
     * Start date
     * <p>
     * The start date for the period. When known, a precise start date must always be provided.
     */
    @JsonProperty("startDate")
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Start date
     * <p>
     * The start date for the period. When known, a precise start date must always be provided.
     */
    @JsonProperty("startDate")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * End date
     * <p>
     * The end date for the period. When known, a precise end date must always be provided.
     */
    @JsonProperty("endDate")
    public Date getEndDate() {
        return endDate;
    }

    /**
     * End date
     * <p>
     * The end date for the period. When known, a precise end date must always be provided.
     */
    @JsonProperty("endDate")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Maximum extent
     * <p>
     * The period cannot be extended beyond this date. This field is optional, and can be used to express the maximum
     * available data for extension or renewal of this period.
     */
    @JsonProperty("maxExtentDate")
    public Date getMaxExtentDate() {
        return maxExtentDate;
    }

    /**
     * Maximum extent
     * <p>
     * The period cannot be extended beyond this date. This field is optional, and can be used to express the maximum
     * available data for extension or renewal of this period.
     */
    @JsonProperty("maxExtentDate")
    public void setMaxExtentDate(Date maxExtentDate) {
        this.maxExtentDate = maxExtentDate;
    }

    /**
     * Duration (days)
     * <p>
     * The maximum duration of this period in days. A user interface may wish to collect or display this data in
     * months or years as appropriate, but should convert it into days when completing this field. This field can be
     * used when exact dates are not known.  Where a startDate and endDate are given, this field is optional, and
     * should reflect the difference between those two days. Where a startDate and maxExtentDate are given, this
     * field is optional, and should reflect the difference between startDate and maxExtentDate.
     */
    @JsonProperty("durationInDays")
    public Integer getDurationInDays() {
        return durationInDays;
    }

    /**
     * Duration (days)
     * <p>
     * The maximum duration of this period in days. A user interface may wish to collect or display this data in
     * months or years as appropriate, but should convert it into days when completing this field. This field can be
     * used when exact dates are not known.  Where a startDate and endDate are given, this field is optional, and
     * should reflect the difference between those two days. Where a startDate and maxExtentDate are given, this
     * field is optional, and should reflect the difference between startDate and maxExtentDate.
     */
    @JsonProperty("durationInDays")
    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = durationInDays;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("startDate", startDate)
                .append("endDate", endDate)
                .append("maxExtentDate", maxExtentDate)
                .append("durationInDays", durationInDays)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(durationInDays)
                .append(endDate)
                .append(startDate)
                .append(maxExtentDate)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TenderPeriod)) {
            return false;
        }
        TenderPeriod rhs = ((TenderPeriod) other);
        return new EqualsBuilder().append(durationInDays, rhs.durationInDays)
                .append(endDate, rhs.endDate)
                .append(startDate, rhs.startDate)
                .append(maxExtentDate, rhs.maxExtentDate)
                .isEquals();
    }

}
