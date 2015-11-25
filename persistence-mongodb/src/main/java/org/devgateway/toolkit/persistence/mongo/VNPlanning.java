/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo;

import java.util.Date;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Planning;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document
public class VNPlanning extends Planning {
	String bidPlanProjectPlace;
	Date bidPlanProjectDateIssue;
	String bidPlanProjectStyle;
	String bidPlanProjectCompanyIssue;
	String bidPlanProjectType;
	Integer bidPlanProjectFund;
	List<String> bidPlanProjectClassify;
	Date bidPlanProjectDateApprove;
	String bidPlanNm;
	String bidPlanProjectStdClsCd;
	String bidNo;

	public String getBidPlanProjectPlace() {
		return bidPlanProjectPlace;
	}

	public void setBidPlanProjectPlace(String bidPlanProjectPlace) {
		this.bidPlanProjectPlace = bidPlanProjectPlace;
	}

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

}
