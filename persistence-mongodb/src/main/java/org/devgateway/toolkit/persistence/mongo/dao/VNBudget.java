/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import org.devgateway.ocvn.persistence.mongo.ocds.Budget;

/**
 * @author mpostelnicu
 *
 */
public class VNBudget extends Budget {

	private String bidPlanProjectStyle;
	
	private String bidPlanProjectType;

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
