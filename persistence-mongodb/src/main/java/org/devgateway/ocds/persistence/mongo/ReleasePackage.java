package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mihai OCDS Package Metadata
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         package-metadata
 */
public class ReleasePackage {
    private String uri;

    private Date publishedDate;

    private Publisher publisher;

    private String license;

    private String publicationPolicy;

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
