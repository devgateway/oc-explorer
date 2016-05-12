/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import java.util.ArrayList;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Budget;
import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mpostelnicu
 *
 */
@Document(collection = "budget")
public class VNBudget extends Budget {

	private String bidPlanProjectStyle;

	private String bidPlanProjectType;

	private Classification projectClassification = new Classification();

	private List<VNLocation> projectLocation = new ArrayList<>();

	public Classification getProjectClassification() {
		return projectClassification;
	}

	public void setProjectClassification(Classification projectClassification) {
		this.projectClassification = projectClassification;
	}

	public List<VNLocation> getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(List<VNLocation> projectLocation) {
		this.projectLocation = projectLocation;
	}

	public String getBidPlanProjectStyle() {
		return bidPlanProjectStyle;
	}

	public void setBidPlanProjectStyle(String bidPlanProjectStyle) {
		this.bidPlanProjectStyle = bidPlanProjectStyle;
	}

	public String getBidPlanProjectType() {
		return bidPlanProjectType;
	}

	public void setBidPlanProjectType(String bidPlanProjectType) {
		this.bidPlanProjectType = bidPlanProjectType;
	}

}
