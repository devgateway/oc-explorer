package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * Related Process
 * <p>
 * A reference to a related contracting process: generally one preceding or following on from the current process.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "relationship",
        "title",
        "scheme",
        "identifier",
        "uri"
})
public class RelatedProcess {

    /**
     * Relationship ID
     * <p>
     * A local identifier for this relationship, unique within this array.
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this relationship, unique within this array.")
    private String id;
    /**
     * Relationship
     * <p>
     * Specify the type of relationship using the [related process codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#related-process).
     */
    @JsonProperty("relationship")
    @JsonPropertyDescription("Specify the type of relationship using the [related process codelist](http://standard"
            + ".open-contracting.org/latest/en/schema/codelists/#related-process).")
    private List<String> relationship = new ArrayList<String>();
    /**
     * Related process title
     * <p>
     * The title of the related process, where referencing an open contracting process, this field should match the
     * tender/title field in the related process.
     */
    @JsonProperty("title")
    @JsonPropertyDescription("The title of the related process, where referencing an open contracting process, this "
            + "field should match the tender/title field in the related process.")
    private String title;
    /**
     * Scheme
     * <p>
     * The identification scheme used by this cross-reference from the [related process scheme codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#related-process-scheme) codelist. When
     * cross-referencing information also published using OCDS, an Open Contracting ID (ocid) should be used.
     */
    @JsonProperty("scheme")
    @JsonPropertyDescription("The identification scheme used by this cross-reference from the [related process scheme"
            + " codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#related-process-scheme) "
            + "codelist. When cross-referencing information also published using OCDS, an Open Contracting ID (ocid) "
            + "should be used.")

    private String scheme;
    /**
     * Identifier
     * <p>
     * The identifier of the related process. When cross-referencing information also published using OCDS, this
     * should be the Open Contracting ID (ocid).
     */
    @JsonProperty("identifier")
    @JsonPropertyDescription("The identifier of the related process. When cross-referencing information also "
            + "published using OCDS, this should be the Open Contracting ID (ocid).")
    private String identifier;
    /**
     * Related process URI
     * <p>
     * A URI pointing to a machine-readable document, release or record package containing the identified related
     * process.
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("A URI pointing to a machine-readable document, release or record package containing the"
            + " identified related process.")
    private URI uri;

    /**
     * Relationship ID
     * <p>
     * A local identifier for this relationship, unique within this array.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Relationship ID
     * <p>
     * A local identifier for this relationship, unique within this array.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Relationship
     * <p>
     * Specify the type of relationship using the [related process codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#related-process).
     */
    @JsonProperty("relationship")
    public List<String> getRelationship() {
        return relationship;
    }

    /**
     * Relationship
     * <p>
     * Specify the type of relationship using the [related process codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#related-process).
     */
    @JsonProperty("relationship")
    public void setRelationship(List<String> relationship) {
        this.relationship = relationship;
    }

    /**
     * Related process title
     * <p>
     * The title of the related process, where referencing an open contracting process, this field should match the
     * tender/title field in the related process.
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Related process title
     * <p>
     * The title of the related process, where referencing an open contracting process, this field should match the
     * tender/title field in the related process.
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Scheme
     * <p>
     * The identification scheme used by this cross-reference from the [related process scheme codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#related-process-scheme) codelist. When
     * cross-referencing information also published using OCDS, an Open Contracting ID (ocid) should be used.
     */
    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    /**
     * Scheme
     * <p>
     * The identification scheme used by this cross-reference from the [related process scheme codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#related-process-scheme) codelist. When
     * cross-referencing information also published using OCDS, an Open Contracting ID (ocid) should be used.
     */
    @JsonProperty("scheme")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Identifier
     * <p>
     * The identifier of the related process. When cross-referencing information also published using OCDS, this
     * should be the Open Contracting ID (ocid).
     */
    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Identifier
     * <p>
     * The identifier of the related process. When cross-referencing information also published using OCDS, this
     * should be the Open Contracting ID (ocid).
     */
    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Related process URI
     * <p>
     * A URI pointing to a machine-readable document, release or record package containing the identified related
     * process.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Related process URI
     * <p>
     * A URI pointing to a machine-readable document, release or record package containing the identified related
     * process.
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("relationship", relationship)
                .append("title", title)
                .append("scheme", scheme)
                .append("identifier", identifier)
                .append("uri", uri)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(identifier)
                .append(scheme)
                .append(id)
                .append(relationship)
                .append(title)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RelatedProcess)) {
            return false;
        }
        RelatedProcess rhs = ((RelatedProcess) other);
        return new EqualsBuilder().append(identifier, rhs.identifier)
                .append(scheme, rhs.scheme)
                .append(id, rhs.id)
                .append(relationship, rhs.relationship)
                .append(title, rhs.title)
                .append(uri, rhs.uri)
                .isEquals();
    }

}
