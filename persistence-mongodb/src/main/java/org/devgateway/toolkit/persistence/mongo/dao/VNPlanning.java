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
 * @author mihai Extension of {@link Planning} to allow extra Vietnam-specific
 *         fields
 */
@Document(collection = "planning")
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

	List<Location> locations = new ArrayList<>();

	@Indexed()
	String bidNo;

	public Date getBidPlanProjectDateIssue() {
		return bidPlanProjectDateIssue;
	}

	public void setBidPlanProjectDateIssue(final Date bidPlanProjectDateIssue) {
		this.bidPlanProjectDateIssue = bidPlanProjectDateIssue;
	}

	public String getBidPlanProjectStyle() {
		return bidPlanProjectStyle;
	}

	public void setBidPlanProjectStyle(final String bidPlanProjectStyle) {
		this.bidPlanProjectStyle = bidPlanProjectStyle;
	}

	public String getBidPlanProjectCompanyIssue() {
		return bidPlanProjectCompanyIssue;
	}

	public void setBidPlanProjectCompanyIssue(final String bidPlanProjectCompanyIssue) {
		this.bidPlanProjectCompanyIssue = bidPlanProjectCompanyIssue;
	}

	public String getBidPlanProjectType() {
		return bidPlanProjectType;
	}

	public void setBidPlanProjectType(final String bidPlanProjectType) {
		this.bidPlanProjectType = bidPlanProjectType;
	}

	public Integer getBidPlanProjectFund() {
		return bidPlanProjectFund;
	}

	public void setBidPlanProjectFund(final Integer bidPlanProjectFund) {
		this.bidPlanProjectFund = bidPlanProjectFund;
	}

	public Date getBidPlanProjectDateApprove() {
		return bidPlanProjectDateApprove;
	}

	public void setBidPlanProjectDateApprove(final Date bidPlanProjectDateApprove) {
		this.bidPlanProjectDateApprove = bidPlanProjectDateApprove;
	}

	public String getBidPlanNm() {
		return bidPlanNm;
	}

	public void setBidPlanNm(final String bidPlanNm) {
		this.bidPlanNm = bidPlanNm;
	}

	public String getBidPlanProjectStdClsCd() {
		return bidPlanProjectStdClsCd;
	}

	public void setBidPlanProjectStdClsCd(final String bidPlanProjectStdClsCd) {
		this.bidPlanProjectStdClsCd = bidPlanProjectStdClsCd;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(final String bidNo) {
		this.bidNo = bidNo;
	}

	public List<String> getBidPlanProjectClassify() {
		return bidPlanProjectClassify;
	}

	public void setBidPlanProjectClassify(final List<String> bidPlanProjectClassify) {
		this.bidPlanProjectClassify = bidPlanProjectClassify;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(final List<Location> locations) {
		this.locations = locations;
	}

}
