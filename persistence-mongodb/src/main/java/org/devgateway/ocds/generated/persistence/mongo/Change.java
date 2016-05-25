
package org.devgateway.ocds.generated.persistence.mongo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "property",
    "former_value"
})
public class Change {

    /**
     * The property name that has been changed relative to the place the amendment is. For example if the contract value has changed, then the property under changes within the contract.amendment would be value.amount. 
     * 
     */
    @JsonProperty("property")
    private String property;
    /**
     * The previous value of the changed property, in whatever type the property is.
     * 
     */
    @JsonProperty("former_value")
    private String formerValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The property name that has been changed relative to the place the amendment is. For example if the contract value has changed, then the property under changes within the contract.amendment would be value.amount. 
     * 
     * @return
     *     The property
     */
    @JsonProperty("property")
    public String getProperty() {
        return property;
    }

    /**
     * The property name that has been changed relative to the place the amendment is. For example if the contract value has changed, then the property under changes within the contract.amendment would be value.amount. 
     * 
     * @param property
     *     The property
     */
    @JsonProperty("property")
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The previous value of the changed property, in whatever type the property is.
     * 
     * @return
     *     The formerValue
     */
    @JsonProperty("former_value")
    public String getFormerValue() {
        return formerValue;
    }

    /**
     * The previous value of the changed property, in whatever type the property is.
     * 
     * @param formerValue
     *     The former_value
     */
    @JsonProperty("former_value")
    public void setFormerValue(String formerValue) {
        this.formerValue = formerValue;
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
        return new HashCodeBuilder().append(property).append(formerValue).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Change) == false) {
            return false;
        }
        Change rhs = ((Change) other);
        return new EqualsBuilder().append(property, rhs.property).append(formerValue, rhs.formerValue).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
