package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Item
 * <p>
 * A good, service, or work to be contracted.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "classification",
        "additionalClassifications",
        "quantity",
        "unit",
        "deliveryLocation"
})
public class Item {

    /**
     * ID
     * <p>
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier to reference and merge the items by. Must be unique within a given "
            + "array of items.")
    @ExcelExport
    private String id;



    /**
     * This is part of the OCDS location extension. We have decided to plug this
     * into the OCDS standard since it seems this will be rolled into OCDS 1.1
     * see https://jira.dgfoundation.org/browse/OCE-35
     */
    @SuppressWarnings("rawtypes")
    private DefaultLocation deliveryLocation;


    /**
     * Description
     * <p>
     * A description of the goods, services to be provided.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A description of the goods, services to be provided.")
    @ExcelExport
    private String description;
    /**
     * Classification
     * <p>
     */
    @JsonProperty("classification")
    @ExcelExport
    private Classification classification;
    /**
     * Additional classifications
     * <p>
     * An array of additional classifications for the item. See the [itemClassificationScheme](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist for common options to
     * use in OCDS. This may also be used to present codes from an internal classification scheme.
     */
    @JsonProperty("additionalClassifications")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("An array of additional classifications for the item. See the [itemClassificationScheme]"
            + "(http://standard.open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist"
            + " for common options to use in OCDS. This may also be used to present codes from an internal "
            + "classification scheme.")
    private Set<Classification> additionalClassifications = new LinkedHashSet<Classification>();
    /**
     * Quantity
     * <p>
     * The number of units required
     */
    @JsonProperty("quantity")
    @ExcelExport
    @JsonPropertyDescription("The number of units required")
    private Double quantity;
    /**
     * Unit
     * <p>
     * A description of the unit in which the supplies, services or works are provided (e.g. hours, kilograms) and
     * the unit-price. For comparability, an established list of units can be used.
     */
    @JsonProperty("unit")
    @JsonPropertyDescription("A description of the unit in which the supplies, services or works are provided (e.g. "
            + "hours, kilograms) and the unit-price. For comparability, an established list of units can be used.  ")
    private Unit unit;

    /**
     * ID
     * <p>
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Description
     * <p>
     * A description of the goods, services to be provided.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * A description of the goods, services to be provided.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Classification
     * <p>
     */
    @JsonProperty("classification")
    public Classification getClassification() {
        return classification;
    }

    /**
     * Classification
     * <p>
     */
    @JsonProperty("classification")
    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    /**
     * Additional classifications
     * <p>
     * An array of additional classifications for the item. See the [itemClassificationScheme](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist for common options to
     * use in OCDS. This may also be used to present codes from an internal classification scheme.
     */
    @JsonProperty("additionalClassifications")
    public Set<Classification> getAdditionalClassifications() {
        return additionalClassifications;
    }

    /**
     * Additional classifications
     * <p>
     * An array of additional classifications for the item. See the [itemClassificationScheme](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist for common options to
     * use in OCDS. This may also be used to present codes from an internal classification scheme.
     */
    @JsonProperty("additionalClassifications")
    public void setAdditionalClassifications(Set<Classification> additionalClassifications) {
        this.additionalClassifications = additionalClassifications;
    }

    /**
     * Quantity
     * <p>
     * The number of units required
     */
    @JsonProperty("quantity")
    public Double getQuantity() {
        return quantity;
    }

    /**
     * Quantity
     * <p>
     * The number of units required
     */
    @JsonProperty("quantity")
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * Unit
     * <p>
     * A description of the unit in which the supplies, services or works are provided (e.g. hours, kilograms) and
     * the unit-price. For comparability, an established list of units can be used.
     */
    @JsonProperty("unit")
    public Unit getUnit() {
        return unit;
    }

    /**
     * Unit
     * <p>
     * A description of the unit in which the supplies, services or works are provided (e.g. hours, kilograms) and
     * the unit-price. For comparability, an established list of units can be used.
     */
    @JsonProperty("unit")
    public void setUnit(Unit unit) {
        this.unit = unit;
    }


    public DefaultLocation getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(DefaultLocation deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("description", description)
                .append("classification", classification)
                .append("additionalClassifications", additionalClassifications)
                .append("quantity", quantity)
                .append("unit", unit)
                .append("deliveryLocation", deliveryLocation)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalClassifications)
                .append(unit)
                .append(quantity)
                .append(description)
                .append(id)
                .append(classification)
                .append(deliveryLocation)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Item)) {
            return false;
        }
        Item rhs = ((Item) other);
        return new EqualsBuilder().append(additionalClassifications, rhs.additionalClassifications)
                .append(unit, rhs.unit)
                .append(quantity, rhs.quantity)
                .append(description, rhs.description)
                .append(id, rhs.id)
                .append(classification, rhs.classification)
                .append(deliveryLocation, rhs.deliveryLocation)
                .isEquals();
    }

}
