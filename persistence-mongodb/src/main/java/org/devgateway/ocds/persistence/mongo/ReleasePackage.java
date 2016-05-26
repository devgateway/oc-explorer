package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mihai OCDS Package Metadata
 * Releases must be published within a release package, which can contain one or more releases.
 * The release package, modelled on the Data Package protocol, provides meta-data about the release(s) it contains,
 * the publisher, and data licensing information.
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#package-metadata
 */
public class ReleasePackage {
    /**
     * The URI of this package that identifies it uniquely in the world.
     */
    private String uri;

    /**
     * The date that this package was published.
     * Ideally this should be the latest date that there is release information in this package.
     */
    private Date publishedDate;

    /**
     * Information to uniquely identify the publisher of this package.
     * @see Publisher
     */
    private Publisher publisher;

    /**
     * A link to the license that applies to the data in this package.
     * A Public Domain Dedication or Open Definition Conformant license is strongly recommended.
     * The canonical URI of the license should be used.
     * Documents linked from this file may be under other license conditions.
     */
    private String license;

    /**
     * A link to a document describing the publishers publication policy.
     */
    private String publicationPolicy;

    /**
     * @see Release for further details.
     */
    private List<Release> releases = new ArrayList<>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPublicationPolicy() {
        return publicationPolicy;
    }

    public void setPublicationPolicy(String publicationPolicy) {
        this.publicationPolicy = publicationPolicy;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }
}
