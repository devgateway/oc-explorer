/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.dao;

import org.devgateway.ocvn.persistence.mongo.ocds.Item;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 *
 */
@Document(collection="item")
public class VNItem extends Item {
	String bidPlanItemRefNum;
	String bidPlanItemStyle;
	String bidPlanItemFund;
	String bidPlanItemMethodSelect;
	String bidPlanItemMethod;

	public String getBidPlanItemRefNum() {
		return bidPlanItemRefNum;
	}

	public void setBidPlanItemRefNum(String bidPlanItemRefNum) {
		this.bidPlanItemRefNum = bidPlanItemRefNum;
	}

	public String getBidPlanItemStyle() {
		return bidPlanItemStyle;
	}

	public void setBidPlanItemStyle(String bidPlanItemStyle) {
		this.bidPlanItemStyle = bidPlanItemStyle;
	}

	public String getBidPlanItemFund() {
		return bidPlanItemFund;
	}

	public void setBidPlanItemFund(String bidPlanItemFund) {
		this.bidPlanItemFund = bidPlanItemFund;
	}

	public String getBidPlanItemMethodSelect() {
		return bidPlanItemMethodSelect;
	}

	public void setBidPlanItemMethodSelect(String bidPlanItemMethodSelect) {
		this.bidPlanItemMethodSelect = bidPlanItemMethodSelect;
	}

	public String getBidPlanItemMethod() {
		return bidPlanItemMethod;
	}

	public void setBidPlanItemMethod(String bidPlanItemMethod) {
		this.bidPlanItemMethod = bidPlanItemMethod;
	}

}
