
package org.devgateway.ocds.generated.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


/**
 * Planning
 * <p>
 * Information from the planning phase of the contracting process. Note that many other fields may be filled in a planning release, in the appropriate fields in other schema sections, these would likely be estimates at this stage e.g. totalValue in tender
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "budget",
    "rationale",
    "documents"
})
public class Planning {

    /**
     * Budget Information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this contracting process is funded. It draws upon data model of the [Budget Data Package](https://github.com/openspending/budget-data-package/blob/master/specification.md), and should be used to cross-reference to more detailed information held using a Budget Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a user to manually or automatically cross-reference with another published source of budget and project information.
     * 
     */
    @JsonProperty("budget")
    private Budget budget;
    /**
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     * 
     */
    @JsonProperty("rationale")
    private String rationale;
    /**
     * A list of documents related to the planning process.
     * 
     */
    @JsonProperty("documents")
    private List<Document> documents = new ArrayList<Document>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Budget Information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this contracting process is funded. It draws upon data model of the [Budget Data Package](https://github.com/openspending/budget-data-package/blob/master/specification.md), and should be used to cross-reference to more detailed information held using a Budget Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a user to manually or automatically cross-reference with another published source of budget and project information.
     * 
     * @return
     *     The budget
     */
    @JsonProperty("budget")
    public Budget getBudget() {
        return budget;
    }

    /**
     * Budget Information
     * <p>
     * This section contain information about the budget line, and associated projects, through which this contracting process is funded. It draws upon data model of the [Budget Data Package](https://github.com/openspending/budget-data-package/blob/master/specification.md), and should be used to cross-reference to more detailed information held using a Budget Data Package, or, where no linked Budget Data Package is available, to provide enough information to allow a user to manually or automatically cross-reference with another published source of budget and project information.
     * 
     * @param budget
     *     The budget
     */
    @JsonProperty("budget")
    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    /**
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     * 
     * @return
     *     The rationale
     */
    @JsonProperty("rationale")
    public String getRationale() {
        return rationale;
    }

    /**
     * The rationale for the procurement provided in free text. More detail can be provided in an attached document.
     * 
     * @param rationale
     *     The rationale
     */
    @JsonProperty("rationale")
    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    /**
     * A list of documents related to the planning process.
     * 
     * @return
     *     The documents
     */
    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * A list of documents related to the planning process.
     * 
     * @param documents
     *     The documents
     */
    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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
        return new HashCodeBuilder().append(budget).append(rationale).append(documents).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Planning) == false) {
            return false;
        }
        Planning rhs = ((Planning) other);
        return new EqualsBuilder().append(budget, rhs.budget).append(rationale, rhs.rationale).append(documents, rhs.documents).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
