package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.net.URI;


/**
 * Budget information
 * <p>
 * This section contain information about the budget line, and associated projects, through which this contracting
 * process is funded. It draws upon data model of the [Fiscal Data Package](http://fiscal.dataprotocols.org/), and
 * should be used to cross-reference to more detailed information held using a Budget Data Package, or, where no
 * linked Budget Data Package is available, to provide enough information to allow a user to manually or
 * automatically cross-reference with another published source of budget and project information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "amount",
        "project",
        "projectID",
        "uri",
        "source"
})
public class Budget {

    /**
     * ID
     * <p>
     * An identifier for the budget line item which provides funds for this contracting process. This identifier
     * should be possible to cross-reference against the provided data source.
     */
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for the budget line item which provides funds for this contracting "
            + "process. This identifier should be possible to cross-reference against the provided data source.")
    private String id;
    /**
     * Budget Source
     * <p>
     * A short free text description of the budget source. May be used to provide the title of the budget line, or
     * the programme used to fund this project.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A short free text description of the budget source. May be used to provide the title of"
            + " the budget line, or the programme used to fund this project.")
    @ExcelExport
    private String description;
    /**
     * Value
     * <p>
     */
    @JsonProperty("amount")
    @ExcelExport
    private Amount amount;
    /**
     * Project title
     * <p>
     * The name of the project that through which this contracting process is funded (if applicable). Some
     * organizations maintain a registry of projects, and the data should use the name by which the project is known
     * in that registry. No translation option is offered for this string, as translated values can be provided in
     * third-party data, linked from the data source above.
     */
    @JsonProperty("project")
    @JsonPropertyDescription("The name of the project that through which this contracting process is funded (if "
            + "applicable). Some organizations maintain a registry of projects, and the data should use the name by "
            + "which the project is known in that registry. No translation option is offered for this string, as "
            + "translated values can be provided in third-party data, linked from the data source above.")
    @ExcelExport
    private String project;
    /**
     * Project identifier
     * <p>
     * An external identifier for the project that this contracting process forms part of, or is funded via (if
     * applicable). Some organizations maintain a registry of projects, and the data should use the identifier from
     * the relevant registry of projects.
     */
    @JsonProperty("projectID")
    @JsonPropertyDescription("An external identifier for the project that this contracting process forms part of, or "
            + "is funded via (if applicable). Some organizations maintain a registry of projects, and the data should"
            + " use the identifier from the relevant registry of projects.")
    private String projectID;
    /**
     * Linked budget information
     * <p>
     * A URI pointing directly to a machine-readable record about the budget line-item or line-items that fund this
     * contracting process. Information may be provided in a range of formats, including using IATI, the Open Fiscal
     * Data Standard or any other standard which provides structured data on budget sources. Human readable documents
     * can be included using the planning.documents block.
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("A URI pointing directly to a machine-readable record about the budget line-item or "
            + "line-items that fund this contracting process. Information may be provided in a range of formats, "
            + "including using IATI, the Open Fiscal Data Standard or any other standard which provides structured "
            + "data on budget sources. Human readable documents can be included using the planning.documents block.")
    private URI uri;
    /**
     * Data Source
     * <p>
     * (Deprecated in 1.1) Used to point either to a corresponding Budget Data Package, or to a machine or
     * human-readable source where users can find further information on the budget line item identifiers, or project
     * identifiers, provided here.
     */
    @JsonProperty("source")
    @JsonPropertyDescription("(Deprecated in 1.1) Used to point either to a corresponding Budget Data Package, or to "
            + "a machine or human-readable source where users can find further information on the budget line item "
            + "identifiers, or project identifiers, provided here.")
    private URI source;


    /**
     * ID
     * <p>
     * An identifier for the budget line item which provides funds for this contracting process. This identifier
     * should be possible to cross-reference against the provided data source.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * An identifier for the budget line item which provides funds for this contracting process. This identifier
     * should be possible to cross-reference against the provided data source.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Budget Source
     * <p>
     * A short free text description of the budget source. May be used to provide the title of the budget line, or
     * the programme used to fund this project.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Budget Source
     * <p>
     * A short free text description of the budget source. May be used to provide the title of the budget line, or
     * the programme used to fund this project.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     * Project title
     * <p>
     * The name of the project that through which this contracting process is funded (if applicable). Some
     * organizations maintain a registry of projects, and the data should use the name by which the project is known
     * in that registry. No translation option is offered for this string, as translated values can be provided in
     * third-party data, linked from the data source above.
     */
    @JsonProperty("project")
    public String getProject() {
        return project;
    }

    /**
     * Project title
     * <p>
     * The name of the project that through which this contracting process is funded (if applicable). Some
     * organizations maintain a registry of projects, and the data should use the name by which the project is known
     * in that registry. No translation option is offered for this string, as translated values can be provided in
     * third-party data, linked from the data source above.
     */
    @JsonProperty("project")
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * Project identifier
     * <p>
     * An external identifier for the project that this contracting process forms part of, or is funded via (if
     * applicable). Some organizations maintain a registry of projects, and the data should use the identifier from
     * the relevant registry of projects.
     */
    @JsonProperty("projectID")
    public String getProjectID() {
        return projectID;
    }

    /**
     * Project identifier
     * <p>
     * An external identifier for the project that this contracting process forms part of, or is funded via (if
     * applicable). Some organizations maintain a registry of projects, and the data should use the identifier from
     * the relevant registry of projects.
     *
     */
    @JsonProperty("projectID")
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * Linked budget information
     * <p>
     * A URI pointing directly to a machine-readable record about the budget line-item or line-items that fund this
     * contracting process. Information may be provided in a range of formats, including using IATI, the Open Fiscal
     * Data Standard or any other standard which provides structured data on budget sources. Human readable documents
     * can be included using the planning.documents block.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Linked budget information
     * <p>
     * A URI pointing directly to a machine-readable record about the budget line-item or line-items that fund this
     * contracting process. Information may be provided in a range of formats, including using IATI, the Open Fiscal
     * Data Standard or any other standard which provides structured data on budget sources. Human readable documents
     * can be included using the planning.documents block.
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Data Source
     * <p>
     * (Deprecated in 1.1) Used to point either to a corresponding Budget Data Package, or to a machine or
     * human-readable source where users can find further information on the budget line item identifiers, or project
     * identifiers, provided here.
     */
    @JsonProperty("source")
    public URI getSource() {
        return source;
    }

    /**
     * Data Source
     * <p>
     * (Deprecated in 1.1) Used to point either to a corresponding Budget Data Package, or to a machine or
     * human-readable source where users can find further information on the budget line item identifiers, or project
     * identifiers, provided here.
     */
    @JsonProperty("source")
    public void setSource(URI source) {
        this.source = source;
    }

   @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("description", description)
                .append("amount", amount)
                .append("project", project)
                .append("projectID", projectID)
                .append("uri", uri)
                .append("source", source)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amount)
                .append(description)
                .append(project)
                .append(id)
                .append(source)
                .append(projectID)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Budget)) {
            Budget rhs = ((Budget) other);
            return new EqualsBuilder().append(amount, rhs.amount)
                    .append(description, rhs.description)
                    .append(project, rhs.project)
                    .append(id, rhs.id)
                    .append(source, rhs.source)
                    .append(projectID, rhs.projectID)
                    .append(uri, rhs.uri)
                    .isEquals();
        } else {
            return false;
        }
    }

}
