/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.dao;

import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Value;

/**
 * @author mihai
 * Extension of {@link Award} to allow extra Vietnam-specific fields
 */
public class VNAward extends Award {

	String inelibigleYN;
	String ineligibleRson;
	Integer bidOpenRank;

	Integer bidType;
	Integer bidSuccMethod;
	Value bidPrice;
	String contractTime;

	public String getInelibigleYN() {
		return inelibigleYN;
	}

	public void setInelibigleYN(String inelibigleYN) {
		this.inelibigleYN = inelibigleYN;
	}

	public Integer getBidOpenRank() {
		return bidOpenRank;
	}

	public void setBidOpenRank(Integer bidOpenRank) {
		this.bidOpenRank = bidOpenRank;
	}

	public Integer getBidType() {
		return bidType;
	}

	public void setBidType(Integer bidType) {
		this.bidType = bidType;
	}

	public Integer getBidSuccMethod() {
		return bidSuccMethod;
	}

	public void setBidSuccMethod(Integer bidSuccMethod) {
		this.bidSuccMethod = bidSuccMethod;
	}

	public Value getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(Value bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getContractTime() {
		return contractTime;
	}

	public void setContractTime(String contractTime) {
		this.contractTime = contractTime;
	}

	public String getIneligibleRson() {
		return ineligibleRson;
	}

	public void setIneligibleRson(String ineligibleRson) {
		this.ineligibleRson = ineligibleRson;
	}

}
