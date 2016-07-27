package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Amendment information
 * <p>
 *      http://standard.open-contracting.org/latest/en/schema/reference/#amendment
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "changes",
        "rationale"
})
public class Amendment {

    /**
     * Amendment Date
     * <p>
     * The data of this amendment.
     *
     */

    @JsonProperty("date")
    private Date date;

    /**
     * Amended fields
     * <p>
     * Comma-seperated list of affected fields.
     *
     */
    @JsonProperty("changes")
    private List<Change> changes = new ArrayList<Change>();

    /**
     * An explanation for the amendment.
     *
     */
    @JsonProperty("rationale")
    private String rationale;

    /**
     * Amendment Date
     * <p>
     * The data of this amendment.
     *
     * @return
     *     The date
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * Amendment Date
     * <p>
     * The data of this amendment.
     *
     * @param date
     *     The date
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Amended fields
     * <p>
     * Comma-seperated list of affected fields.
     *
     * @return
     *     The changes
     */
    @JsonProperty("changes")
    public List<Change> getChanges() {
        return changes;
    }

    /**
     * Amended fields
     * <p>
     * Comma-seperated list of affected fields.
     *
     * @param changes
     *     The changes
     */
    @JsonProperty("changes")
    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    /**
     * An explanation for the amendment.
     *
     * @return
     *     The rationale
     */
    @JsonProperty("rationale")
    public String getRationale() {
        return rationale;
    }

    /**
     * An explanation for the amendment.
     *
     * @param rationale
     *     The rationale
     */
    @JsonProperty("rationale")
    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(date).
                append(changes).
                append(rationale).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Amendment)) {
            return false;
        }
        Amendment rhs = ((Amendment) other);
        return new EqualsBuilder().
                append(date, rhs.date).
                append(changes, rhs.changes).
                append(rationale, rhs.rationale).
                isEquals();
    }

}
