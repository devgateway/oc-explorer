package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "property",
        "former_value"
})
public class Change {

    /**
     * Property
     * <p>
     * The property name that has been changed relative to the place the amendment is. For example if the contract
     * value has changed, then the property under changes within the contract.amendment would be value.amount.
     * (Deprecated in 1.1)
     */
    @JsonProperty("property")
    @JsonPropertyDescription("The property name that has been changed relative to the place the amendment is. For "
            + "example if the contract value has changed, then the property under changes within the contract"
            + ".amendment would be value.amount. (Deprecated in 1.1)")
    private String property;
    /**
     * Former Value
     * <p>
     * The previous value of the changed property, in whatever type the property is. (Deprecated in 1.1)
     */
    @JsonProperty("former_value")
    @JsonPropertyDescription("The previous value of the changed property, in whatever type the property is. "
            + "(Deprecated in 1.1)")
    private String formerValue;

    /**
     * Property
     * <p>
     * The property name that has been changed relative to the place the amendment is. For example if the contract
     * value has changed, then the property under changes within the contract.amendment would be value.amount.
     * (Deprecated in 1.1)
     */
    @JsonProperty("property")
    public String getProperty() {
        return property;
    }

    /**
     * Property
     * <p>
     * The property name that has been changed relative to the place the amendment is. For example if the contract
     * value has changed, then the property under changes within the contract.amendment would be value.amount.
     * (Deprecated in 1.1)
     */
    @JsonProperty("property")
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Former Value
     * <p>
     * The previous value of the changed property, in whatever type the property is. (Deprecated in 1.1)
     */
    @JsonProperty("former_value")
    public String getFormerValue() {
        return formerValue;
    }

    /**
     * Former Value
     * <p>
     * The previous value of the changed property, in whatever type the property is. (Deprecated in 1.1)
     */
    @JsonProperty("former_value")
    public void setFormerValue(String formerValue) {
        this.formerValue = formerValue;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("property", property)
                .append("formerValue", formerValue)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(property).append(formerValue).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Change)) {
            return false;
        }
        Change rhs = ((Change) other);
        return new EqualsBuilder().append(property, rhs.property)
                .append(formerValue, rhs.formerValue)
                .isEquals();
    }

}
