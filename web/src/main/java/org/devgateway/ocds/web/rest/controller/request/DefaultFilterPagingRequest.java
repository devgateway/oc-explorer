/**
 * 
 */
package org.devgateway.ocds.web.rest.controller.request;

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
	
	@ApiModelProperty(value = "This will filter after tender.items.deliveryLocation._id")
	private List<String> tenderLoc;

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

	public void setTenderLoc(List<String> tenderDeliveryLocationGazetteerIdentifier) {
		this.tenderLoc = tenderDeliveryLocationGazetteerIdentifier;
	}



}
