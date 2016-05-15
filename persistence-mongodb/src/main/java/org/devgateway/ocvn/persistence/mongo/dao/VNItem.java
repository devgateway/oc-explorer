/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.dao;

import org.devgateway.ocds.persistence.mongo.Item;

/**
 * @author mihai Extension of {@link Item} to allow extra Vietnam-specific
 *         fields
 */
public class VNItem extends Item {
	String bidPlanItemRefNum;
	String bidPlanItemStyle;
	String bidPlanItemFund;
	String bidPlanItemMethodSelect;
	String bidPlanItemMethod;

	public String getBidPlanItemRefNum() {
		return bidPlanItemRefNum;
	}

	public void setBidPlanItemRefNum(final String bidPlanItemRefNum) {
		this.bidPlanItemRefNum = bidPlanItemRefNum;
	}

	public String getBidPlanItemStyle() {
		return bidPlanItemStyle;
	}

	public void setBidPlanItemStyle(final String bidPlanItemStyle) {
		this.bidPlanItemStyle = bidPlanItemStyle;
	}

	public String getBidPlanItemFund() {
		return bidPlanItemFund;
	}

	public void setBidPlanItemFund(final String bidPlanItemFund) {
		this.bidPlanItemFund = bidPlanItemFund;
	}

	public String getBidPlanItemMethodSelect() {
		return bidPlanItemMethodSelect;
	}

	public void setBidPlanItemMethodSelect(final String bidPlanItemMethodSelect) {
		this.bidPlanItemMethodSelect = bidPlanItemMethodSelect;
	}

	public String getBidPlanItemMethod() {
		return bidPlanItemMethod;
	}

	public void setBidPlanItemMethod(final String bidPlanItemMethod) {
		this.bidPlanItemMethod = bidPlanItemMethod;
	}

}
