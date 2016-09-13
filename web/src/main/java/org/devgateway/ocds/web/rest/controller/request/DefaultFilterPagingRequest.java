/**
 * 
 */
package org.devgateway.ocds.web.rest.controller.request;

import java.math.BigDecimal;
import java.util.List;

import cz.jirutka.validator.collection.constraints.EachPattern;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author mihai Filtering bean applied to all endpoints
 */
public class DefaultFilterPagingRequest extends GenericPagingRequest {

	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	@ApiModelProperty(value = "This corresponds to the tender.items.classification._id")
	private List<String> bidTypeId;

	@EachPattern(regexp = "^[a-zA-Z0-9]*$")
	@ApiModelProperty(value = "This is the id of the organization/procuring entity. "
			+ "Corresponds to the OCDS Organization.identifier")
	private List<String> procuringEntityId;
	
	//@EachPattern(regexp = "^[\\p{L}0-9]*$")
	@ApiModelProperty(value = "This is the id of the organization/supplier entity. "
			+ "Corresponds to the OCDS Organization.identifier")
	private List<String> supplierId;
	
	@ApiModelProperty(value = "This will filter after tender.items.deliveryLocation._id")
	private List<String> tenderLoc;
	
	@ApiModelProperty(value = "This will filter after tender.value.amount and will specify a minimum"
			+ "Use /api/tenderValueInterval to get the minimum allowed.")
	private BigDecimal minTenderValue;
	
	@ApiModelProperty(value = "This will filter after tender.value.amount and will specify a maximum."
			+ "Use /api/tenderValueInterval to get the maximum allowed.")
	private BigDecimal maxTenderValue;
		
	@ApiModelProperty(value = "This will filter after awards.value.amount and will specify a minimum"
			+ "Use /api/awardValueInterval to get the minimum allowed.")
	private BigDecimal minAwardValue;
	
	@ApiModelProperty(value = "This will filter after awards.value.amount and will specify a maximum."
			+ "Use /api/awardValueInterval to get the maximum allowed.")
	private BigDecimal maxAwardValue;

	/**
	 * This parameter will invert (negate) all existing filtering parameters. So
	 * A IN B turns into A NOT IN B. A IN B AND AN IN C turns into A NOT IN B
	 * AND A NOT IN C. So this is NOT exactly a logical *not*, the correct way
	 * would be !(A && B) = !A || !B. Which is not what we do here, but we
	 * actually dont use multiple parameters anywhere, so it should not matter
	 * now
	 */
	@ApiModelProperty(value = "This parameter will invert (negate) all existing filtering parameters."
			+ "So A IN B turns into A NOT IN B. A IN B AND AN IN C turns into A NOT IN B"
			+ " AND A NOT IN C. So this is NOT exactly a logical *not*, the correct way "
			+ "  would be !(A && B) = !A || !B.")
	private Boolean invert = false;

	public DefaultFilterPagingRequest() {
		super();
	}

	public List<String> getBidTypeId() {
		return bidTypeId;
	}

	public void setBidTypeId(final List<String> bidTypeId) {
		this.bidTypeId = bidTypeId;
	}

	public List<String> getProcuringEntityId() {
		return procuringEntityId;
	}

	public void setProcuringEntityId(final List<String> procuringEntityId) {
		this.procuringEntityId = procuringEntityId;
	}



	public Boolean getInvert() {
		return invert;
	}

	public void setInvert(final Boolean invert) {
		this.invert = invert;
	}

	public List<String> getTenderLoc() {
		return tenderLoc;
	}

	public void setTenderLoc(final List<String> tenderDeliveryLocationGazetteerIdentifier) {
		this.tenderLoc = tenderDeliveryLocationGazetteerIdentifier;
	}

	public BigDecimal getMinTenderValue() {
		return minTenderValue;
	}

	public void setMinTenderValue(final BigDecimal minTenderValueAmount) {
		this.minTenderValue = minTenderValueAmount;
	}

	public BigDecimal getMaxTenderValue() {
		return maxTenderValue;
	}

	public void setMaxTenderValue(final BigDecimal maxTenderValueAmount) {
		this.maxTenderValue = maxTenderValueAmount;
	}

	public BigDecimal getMinAwardValue() {
		return minAwardValue;
	}

	public void setMinAwardValue(final BigDecimal minAwardValue) {
		this.minAwardValue = minAwardValue;
	}

	public BigDecimal getMaxAwardValue() {
		return maxAwardValue;
	}

	public void setMaxAwardValue(final BigDecimal maxAwardValue) {
		this.maxAwardValue = maxAwardValue;
	}

	public List<String> getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(final List<String> supplierId) {
		this.supplierId = supplierId;
	}



}
