package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Schema for an Open Contracting Record package
 * <p>
 * The record package contains a list of records along with some publishing metadata. The records pull together all
 * the releases under a single Open Contracting ID and compile them into the latest version of the information along
 * with the history of any data changes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uri",
        "version",
        "extensions",
        "publisher",
        "license",
        "publicationPolicy",
        "publishedDate",
        "packages",
        "records"
})
public class RecordPackage {

    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world.
     * (Required)
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("The URI of this package that identifies it uniquely in the world.")
    private URI uri;
    /**
     * OCDS schema version
     * <p>
     * The version of the OCDS schema used in this package, expressed as major.minor For example: 1.0 or 1.1
     * (Required)
     */
    @JsonProperty("version")
    @JsonPropertyDescription("The version of the OCDS schema used in this package, expressed as major.minor For "
            + "example: 1.0 or 1.1")
    private String version;
    /**
     * OCDS extensions
     * <p>
     * An array of OCDS extensions used in this package. Each entry should be a URL to the extension.json file for
     * that extension.
     */
    @JsonProperty("extensions")
    @JsonPropertyDescription("An array of OCDS extensions used in this package. Each entry should be a URL to the "
            + "extension.json file for that extension.")
    private List<URI> extensions = new ArrayList<URI>();
    /**
     * Information to uniquely identify the publisher of this package.
     * (Required)
     */
    @JsonProperty("publisher")
    @JsonPropertyDescription("Information to uniquely identify the publisher of this package.")
    private Publisher publisher;
    /**
     * License
     * <p>
     * A link to the license that applies to the data in this data package. [Open Definition Conformant]
     * (http://opendefinition.org/licenses/) licenses are strongly recommended. The canonical URI of the license
     * should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    @JsonPropertyDescription("A link to the license that applies to the data in this data package. [Open Definition "
            + "Conformant](http://opendefinition.org/licenses/) licenses are strongly recommended. The canonical URI "
            + "of the license should be used. Documents linked from this file may be under other license conditions.")
    private URI license;
    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers publication policy.
     */
    @JsonProperty("publicationPolicy")
    @JsonPropertyDescription("A link to a document describing the publishers publication policy.")
    private URI publicationPolicy;
    /**
     * Published date
     * <p>
     * The date that this package was published. If this package is generated 'on demand', this date should reflect
     * the date of the last change to the underlying contents of the package.
     * (Required)
     */
    @JsonProperty("publishedDate")
    @JsonPropertyDescription("The date that this package was published. If this package is generated 'on demand', "
            + "this date should reflect the date of the last change to the underlying contents of the package.")
    private Date publishedDate;
    /**
     * Packages
     * <p>
     * A list of URIs of all the release packages that were used to create this record package.
     */
    @JsonProperty("packages")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("A list of URIs of all the release packages that were used to create this record package.")
    private Set<URI> packages = new LinkedHashSet<URI>();
    /**
     * Records
     * <p>
     * The records for this data package.
     * (Required)
     */
    @JsonProperty("records")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("The records for this data package.")
    private Set<Record> records = new LinkedHashSet<Record>();


    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world.
     * (Required)
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world.
     * (Required)
     */
    @JsonProperty("uri")
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * OCDS schema version
     * <p>
     * The version of the OCDS schema used in this package, expressed as major.minor For example: 1.0 or 1.1
     * (Required)
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * OCDS schema version
     * <p>
     * The version of the OCDS schema used in this package, expressed as major.minor For example: 1.0 or 1.1
     * (Required)
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * OCDS extensions
     * <p>
     * An array of OCDS extensions used in this package. Each entry should be a URL to the extension.json file for
     * that extension.
     */
    @JsonProperty("extensions")
    public List<URI> getExtensions() {
        return extensions;
    }

    /**
     * OCDS extensions
     * <p>
     * An array of OCDS extensions used in this package. Each entry should be a URL to the extension.json file for
     * that extension.
     */
    @JsonProperty("extensions")
    public void setExtensions(List<URI> extensions) {
        this.extensions = extensions;
    }

    /**
     * Information to uniquely identify the publisher of this package.
     * (Required)
     */
    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Information to uniquely identify the publisher of this package.
     * (Required)
     */
    @JsonProperty("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * License
     * <p>
     * A link to the license that applies to the data in this data package. [Open Definition Conformant]
     * (http://opendefinition.org/licenses/) licenses are strongly recommended. The canonical URI of the license
     * should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    public URI getLicense() {
        return license;
    }

    /**
     * License
     * <p>
     * A link to the license that applies to the data in this data package. [Open Definition Conformant]
     * (http://opendefinition.org/licenses/) licenses are strongly recommended. The canonical URI of the license
     * should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    public void setLicense(URI license) {
        this.license = license;
    }

    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers publication policy.
     */
    @JsonProperty("publicationPolicy")
    public URI getPublicationPolicy() {
        return publicationPolicy;
    }

    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers publication policy.
     */
    @JsonProperty("publicationPolicy")
    public void setPublicationPolicy(URI publicationPolicy) {
        this.publicationPolicy = publicationPolicy;
    }

    /**
     * Published date
     * <p>
     * The date that this package was published. If this package is generated 'on demand', this date should reflect
     * the date of the last change to the underlying contents of the package.
     * (Required)
     */
    @JsonProperty("publishedDate")
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Published date
     * <p>
     * The date that this package was published. If this package is generated 'on demand', this date should reflect
     * the date of the last change to the underlying contents of the package.
     * (Required)
     */
    @JsonProperty("publishedDate")
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * Packages
     * <p>
     * A list of URIs of all the release packages that were used to create this record package.
     */
    @JsonProperty("packages")
    public Set<URI> getPackages() {
        return packages;
    }

    /**
     * Packages
     * <p>
     * A list of URIs of all the release packages that were used to create this record package.
     */
    @JsonProperty("packages")
    public void setPackages(Set<URI> packages) {
        this.packages = packages;
    }

    /**
     * Records
     * <p>
     * The records for this data package.
     * (Required)
     */
    @JsonProperty("records")
    public Set<Record> getRecords() {
        return records;
    }

    /**
     * Records
     * <p>
     * The records for this data package.
     * (Required)
     */
    @JsonProperty("records")
    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("uri", uri)
                .append("version", version)
                .append("extensions", extensions)
                .append("publisher", publisher)
                .append("license", license)
                .append("publicationPolicy", publicationPolicy)
                .append("publishedDate", publishedDate)
                .append("packages", packages)
                .append("records", records)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(publicationPolicy)
                .append(license)
                .append(extensions)
                .append(records)
                .append(publisher)
                .append(publishedDate)
                .append(packages)
                .append(uri)
                .append(version)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RecordPackage)) {
            return false;
        }
        RecordPackage rhs = ((RecordPackage) other);
        return new EqualsBuilder().append(publicationPolicy, rhs.publicationPolicy)
                .append(license, rhs.license)
                .append(extensions, rhs.extensions)
                .append(records, rhs.records)
                .append(publisher, rhs.publisher)
                .append(publishedDate, rhs.publishedDate)
                .append(packages, rhs.packages)
                .append(uri, rhs.uri)
                .append(version, rhs.version)
                .isEquals();
    }

}
