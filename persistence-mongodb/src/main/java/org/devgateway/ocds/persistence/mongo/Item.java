package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A good, service, or work to be contracted.
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#item
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "classification",
        "additionalClassifications",
        "quantity",
        "unit"
})
public class Item {

    /**
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     *
     */
    @ExcelExport
    @JsonProperty("id")
    private String id;

    /**
     * A description of the goods, services to be provided.
     *
     */
    @ExcelExport
    @JsonProperty("description")
    private String description;

    @ExcelExport
    @JsonProperty("classification")
    private Classification classification;

    /**
     * An array of additional classifications for the item. See the
     * [itemClassificationScheme]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#item-classification-scheme)
     *  codelist for common options to use in OCDS.
     *  This may also be used to present codes from an internal classification scheme.
     *
     */
    @JsonProperty("additionalClassifications")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Classification> additionalClassifications = new LinkedHashSet<Classification>();

    /**
     * The number of units required
     *
     */
    @ExcelExport
    @JsonProperty("quantity")
    private Integer quantity;

    /**
     * Description of the unit which the good comes in e.g. hours, kilograms.
     * Made up of a unit name, and the value of a single unit.
     *
     */
    @JsonProperty("unit")
    private Unit unit;

    /**
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A local identifier to reference and merge the items by. Must be unique within a given array of items.
     * (Required)
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A description of the goods, services to be provided.
     *
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * A description of the goods, services to be provided.
     *
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     *     The classification
     */
    @JsonProperty("classification")
    public Classification getClassification() {
        return classification;
    }

    /**
     *
     * @param classification
     *     The classification
     */
    @JsonProperty("classification")
    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    /**
     * An array of additional classifications for the item. See the
     * [itemClassificationScheme]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#item-classification-scheme)
     *  codelist for common options to use in OCDS.
     *  This may also be used to present codes from an internal classification scheme.
     *
     * @return
     *     The additionalClassifications
     */
    @JsonProperty("additionalClassifications")
    public Set<Classification> getAdditionalClassifications() {
        return additionalClassifications;
    }

    /**
     * An array of additional classifications for the item. See the
     * [itemClassificationScheme]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#item-classification-scheme)
     *  codelist for common options to use in OCDS.
     *  This may also be used to present codes from an internal classification scheme.
     *
     * @param additionalClassifications
     *     The additionalClassifications
     */
    @JsonProperty("additionalClassifications")
    public void setAdditionalClassifications(Set<Classification> additionalClassifications) {
        this.additionalClassifications = additionalClassifications;
    }

    /**
     * The number of units required
     *
     * @return
     *     The quantity
     */
    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * The number of units required
     *
     * @param quantity
     *     The quantity
     */
    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Description of the unit which the good comes in e.g. hours, kilograms.
     * Made up of a unit name, and the value of a single unit.
     *
     * @return
     *     The unit
     */
    @JsonProperty("unit")
    public Unit getUnit() {
        return unit;
    }

    /**
     * Description of the unit which the good comes in e.g. hours, kilograms.
     * Made up of a unit name, and the value of a single unit.
     *
     * @param unit
     *     The unit
     */
    @JsonProperty("unit")
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(id).
                append(description).
                append(classification).
                append(additionalClassifications).
                append(quantity).
                append(unit).
                toHashCode();
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
        return new EqualsBuilder().
                append(id, rhs.id).
                append(description, rhs.description).
                append(classification, rhs.classification).
                append(additionalClassifications, rhs.additionalClassifications).
                append(quantity, rhs.quantity).
                append(unit, rhs.unit).
                isEquals();
    }

}
