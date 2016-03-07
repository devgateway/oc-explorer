package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReleasePackage {

	String uri;
	
	Date publishedDate;

	Publisher publisher;

	String license;

	String publicationPolicy;

	List<Release> releases=new ArrayList<>();

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
