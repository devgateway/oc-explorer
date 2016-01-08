/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Planning;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document(collection="planning")
public class VNPlanning extends Planning {
	
	Date bidPlanProjectDateIssue;
	String bidPlanProjectStyle;
	String bidPlanProjectCompanyIssue;
	String bidPlanProjectType;
	Integer bidPlanProjectFund;
	List<String> bidPlanProjectClassify;
	
	@Indexed
	Date bidPlanProjectDateApprove;
	String bidPlanNm;
	String bidPlanProjectStdClsCd;
	
	List<Location> locations=new ArrayList<>();

	@Indexed()
	String bidNo;

	public Date getBidPlanProjectDateIssue() {
		return bidPlanProjectDateIssue;
	}

	public void setBidPlanProjectDateIssue(Date bidPlanProjectDateIssue) {
		this.bidPlanProjectDateIssue = bidPlanProjectDateIssue;
	}

	public String getBidPlanProjectStyle() {
		return bidPlanProjectStyle;
	}

	public void setBidPlanProjectStyle(String bidPlanProjectStyle) {
		this.bidPlanProjectStyle = bidPlanProjectStyle;
	}

	public String getBidPlanProjectCompanyIssue() {
		return bidPlanProjectCompanyIssue;
	}

	public void setBidPlanProjectCompanyIssue(String bidPlanProjectCompanyIssue) {
		this.bidPlanProjectCompanyIssue = bidPlanProjectCompanyIssue;
	}

	public String getBidPlanProjectType() {
		return bidPlanProjectType;
	}

	public void setBidPlanProjectType(String bidPlanProjectType) {
		this.bidPlanProjectType = bidPlanProjectType;
	}

	public Integer getBidPlanProjectFund() {
		return bidPlanProjectFund;
	}

	public void setBidPlanProjectFund(Integer bidPlanProjectFund) {
		this.bidPlanProjectFund = bidPlanProjectFund;
	}

	public Date getBidPlanProjectDateApprove() {
		return bidPlanProjectDateApprove;
	}

	public void setBidPlanProjectDateApprove(Date bidPlanProjectDateApprove) {
		this.bidPlanProjectDateApprove = bidPlanProjectDateApprove;
	}

	public String getBidPlanNm() {
		return bidPlanNm;
	}

	public void setBidPlanNm(String bidPlanNm) {
		this.bidPlanNm = bidPlanNm;
	}

	public String getBidPlanProjectStdClsCd() {
		return bidPlanProjectStdClsCd;
	}

	public void setBidPlanProjectStdClsCd(String bidPlanProjectStdClsCd) {
		this.bidPlanProjectStdClsCd = bidPlanProjectStdClsCd;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public List<String> getBidPlanProjectClassify() {
		return bidPlanProjectClassify;
	}

	public void setBidPlanProjectClassify(List<String> bidPlanProjectClassify) {
		this.bidPlanProjectClassify = bidPlanProjectClassify;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

}
