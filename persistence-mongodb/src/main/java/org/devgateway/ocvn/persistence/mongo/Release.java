/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * @author mihai
 *
 */
public class Release {

	@Id
	String id;

	String ocid;

	Date date;

	List<String> tag;

	String initiationType = "tender";
	
	Planning planning;
	
	Tender tender;
	
	Organization buyer;
	
	List<Award> awards;
	
	List<Contract> contracts;
	
	String language;

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

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
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

}
