package org.devgateway.ocds.persistence.mongo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Schema for an Open Contracting Release Package
 * <p>
 * Note that all releases within a release package must have a unique releaseID within this release package.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uri",
        "version",
        "extensions",
        "publishedDate",
        "releases",
        "publisher",
        "license",
        "publicationPolicy"
})
public class ReleasePackage implements Identifiable {

    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world. Recommended practice is to use a
     * dereferenceable URI, where a persistent copy of this package is available.
     * (Required)
     */
    @JsonProperty("uri")
    @JsonPropertyDescription("The URI of this package that identifies it uniquely in the world. Recommended practice "
            + "is to use a dereferenceable URI, where a persistent copy of this package is available.")
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
     * Releases
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("releases")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Release> releases = new LinkedHashSet<Release>();
    /**
     * Publisher
     * <p>
     * Information to uniquely identify the publisher of this package.
     * (Required)
     */
    @JsonProperty("publisher")
    @JsonPropertyDescription("Information to uniquely identify the publisher of this package.")
    private Publisher publisher;
    /**
     * License
     * <p>
     * A link to the license that applies to the data in this package. A Public Domain Dedication or [Open Definition
     * Conformant](http://opendefinition.org/licenses/) license is strongly recommended. The canonical URI of the
     * license should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    @JsonPropertyDescription("A link to the license that applies to the data in this package. A Public Domain "
            + "Dedication or [Open Definition Conformant](http://opendefinition.org/licenses/) license is strongly "
            + "recommended. The canonical URI of the license should be used. Documents linked from this file may be "
            + "under other license conditions. ")
    private URI license;
    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers [publication policy](http://standard.open-contracting
     * .org/latest/en/implementation/publication_policy/).
     */
    @JsonProperty("publicationPolicy")
    @JsonPropertyDescription("A link to a document describing the publishers [publication policy](http://standard"
            + ".open-contracting.org/latest/en/implementation/publication_policy/).")
    private URI publicationPolicy;

    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world. Recommended practice is to use a
     * dereferenceable URI, where a persistent copy of this package is available.
     * (Required)
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Package identifier
     * <p>
     * The URI of this package that identifies it uniquely in the world. Recommended practice is to use a
     * dereferenceable URI, where a persistent copy of this package is available.
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
     * Releases
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("releases")
    public Set<Release> getReleases() {
        return releases;
    }

    /**
     * Releases
     * <p>
     * <p>
     * (Required)
     */
    @JsonProperty("releases")
    public void setReleases(Set<Release> releases) {
        this.releases = releases;
    }

    /**
     * Publisher
     * <p>
     * Information to uniquely identify the publisher of this package.
     * (Required)
     */
    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Publisher
     * <p>
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
     * A link to the license that applies to the data in this package. A Public Domain Dedication or [Open Definition
     * Conformant](http://opendefinition.org/licenses/) license is strongly recommended. The canonical URI of the
     * license should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    public URI getLicense() {
        return license;
    }

    /**
     * License
     * <p>
     * A link to the license that applies to the data in this package. A Public Domain Dedication or [Open Definition
     * Conformant](http://opendefinition.org/licenses/) license is strongly recommended. The canonical URI of the
     * license should be used. Documents linked from this file may be under other license conditions.
     */
    @JsonProperty("license")
    public void setLicense(URI license) {
        this.license = license;
    }

    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers [publication policy](http://standard.open-contracting
     * .org/latest/en/implementation/publication_policy/).
     */
    @JsonProperty("publicationPolicy")
    public URI getPublicationPolicy() {
        return publicationPolicy;
    }

    /**
     * Publication policy
     * <p>
     * A link to a document describing the publishers [publication policy](http://standard.open-contracting
     * .org/latest/en/implementation/publication_policy/).
     */
    @JsonProperty("publicationPolicy")
    public void setPublicationPolicy(URI publicationPolicy) {
        this.publicationPolicy = publicationPolicy;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("uri", uri)
                .append("version", version)
                .append("extensions", extensions)
                .append("publishedDate", publishedDate)
                .append("releases", releases)
                .append("publisher", publisher)
                .append("license", license)
                .append("publicationPolicy", publicationPolicy)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(publicationPolicy)
                .append(license)
                .append(extensions)
                .append(publisher)
                .append(publishedDate)
                .append(uri)
                .append(version)
                .append(releases)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ReleasePackage)) {
            return false;
        }
        ReleasePackage rhs = ((ReleasePackage) other);
        return new EqualsBuilder().append(publicationPolicy, rhs.publicationPolicy)
                .append(license, rhs.license)
                .append(extensions, rhs.extensions)
                .append(publisher, rhs.publisher)
                .append(publishedDate, rhs.publishedDate)
                .append(uri, rhs.uri)
                .append(version, rhs.version)
                .append(releases, rhs.releases)
                .isEquals();
    }

    @Override
    public Serializable getIdProperty() {
        return uri;
    }
}
