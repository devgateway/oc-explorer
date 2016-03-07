/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document
public class Release {

	@Id
	String id;

	@Indexed
	String ocid;

	@CreatedDate
	Date date;

	Set<String> tag = new HashSet<>();

	String initiationType = "tender";

	Planning planning;


	Tender tender;

	Organization buyer;

	List<Award> awards = new ArrayList<>();

	List<Contract> contracts = new ArrayList<>();

	String language="en";	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOcid() {
		return ocid;
	}

	public void setOcid(String ocid) {
		this.ocid = ocid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

	public String getInitiationType() {
		return initiationType;
	}

	public void setInitiationType(String initiationType) {
		this.initiationType = initiationType;
	}

	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

	public Tender getTender() {
		return tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
	}

	public Organization getBuyer() {
		return buyer;
	}

	public void setBuyer(Organization buyer) {
		this.buyer = buyer;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set<String> getTag() {
		return tag;
	}

	public void setTag(Set<String> tag) {
		this.tag = tag;
	}

}
