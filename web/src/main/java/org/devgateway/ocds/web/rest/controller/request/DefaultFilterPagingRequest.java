/**
 *
 */
package org.devgateway.ocds.web.rest.controller.request;

import cz.jirutka.validator.collection.constraints.EachPattern;
import cz.jirutka.validator.collection.constraints.EachRange;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.TreeSet;

/**
 * @author mpostelnicu Filtering bean applied to all endpoints
 */
public class DefaultFilterPagingRequest extends GenericPagingRequest {

    @ApiModelProperty(value = "Full text search of of release entities")
    private String text;

    @ApiModelProperty(value = "Filter by award.status, possible values are available from the OCDS standard page"
            + "http://standard.open-contracting.org/latest/en/schema/codelists/#award-status")
    private TreeSet<String> awardStatus;

    @EachPattern(regexp = "^[a-zA-Z0-9\\-]*$")
    @ApiModelProperty(value = "This corresponds to the tender.items.classification._id")
    private TreeSet<String> bidTypeId;

    @EachPattern(regexp = "^[a-zA-Z0-9\\-]*$")
    @ApiModelProperty(value =
            "This corresponds the negated bidTypeId filter, matches elements that are NOT in the TreeSet of Ids")
    private TreeSet<String> notBidTypeId;

    @EachPattern(regexp = "^[a-zA-Z0-9\\-]*$")
    @ApiModelProperty(value = "This is the id of the organization/procuring entity. "
            + "Corresponds to the OCDS Organization.identifier")
    private TreeSet<String> procuringEntityId;

    @EachPattern(regexp = "^[a-zA-Z0-9\\-]*$")
    @ApiModelProperty(value = "This corresponds the negated procuringEntityId filter,"
            + " matches elements that are NOT in the TreeSet of Ids")
    private TreeSet<String> notProcuringEntityId;

    @EachPattern(regexp = "^[a-zA-Z0-9\\-]*$")
    @ApiModelProperty(value = "This is the id of the organization/supplier entity. "
            + "Corresponds to the OCDS Organization.identifier")
    private TreeSet<String> supplierId;

    @ApiModelProperty(value = "This is the new bidder format bids.details.tenderers._id")
    private TreeSet<String> bidderId;

    @ApiModelProperty(value = "This will filter after tender.items.deliveryLocation._id")
    private TreeSet<String> tenderLoc;

    @ApiModelProperty(value = "This will filter after tender.procurementMethod")
    private TreeSet<String> procurementMethod;

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

    @EachPattern(regexp = "^[a-zA-Z0-9]*$")
    @ApiModelProperty(value = "This will filter releases that were flagged with a specific flag type "
            + ", by flags.flaggedStats.type, so it can filter by FRAUD, RIGGING, etc...")
    private TreeSet<String> flagType;

    @ApiModelProperty(value = "This will filter releases based on the count of the flags PER RELEASE, which is stored "
            + "in flags.totalFlagged. 0 (zero) is not allowed here, if you want to see all the releases where there "
            + "are no flags, just completely omit this filter.")
    @EachRange(min = 1)
    private TreeSet<Integer> totalFlagged;

    @ApiModelProperty(value = "Filters after tender.submissionMethod='electronicSubmission', also known as"
            + " eBids")
    private Boolean electronicSubmission;

    @ApiModelProperty(value = "Only show the releases that were flagged by at least one indicator")
    private Boolean flagged;

    @ApiModelProperty(hidden = true)
    private Boolean awardFiltering = false;

    public DefaultFilterPagingRequest awardFiltering() {
        awardFiltering = true;
        return this;
    }

    public Boolean getAwardFiltering() {
        return awardFiltering;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TreeSet<String> getFlagType() {
        return flagType;
    }

    public void setFlagType(TreeSet<String> flagType) {
        this.flagType = flagType;
    }

    public DefaultFilterPagingRequest() {
        super();
    }

    public TreeSet<String> getBidTypeId() {
        return bidTypeId;
    }

    public void setBidTypeId(final TreeSet<String> bidTypeId) {
        this.bidTypeId = bidTypeId;
    }

    public TreeSet<String> getProcuringEntityId() {
        return procuringEntityId;
    }

    public void setProcuringEntityId(final TreeSet<String> procuringEntityId) {
        this.procuringEntityId = procuringEntityId;
    }

    public TreeSet<String> getTenderLoc() {
        return tenderLoc;
    }

    public void setTenderLoc(final TreeSet<String> tenderDeliveryLocationGazetteerIdentifier) {
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

    public TreeSet<String> getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final TreeSet<String> supplierId) {
        this.supplierId = supplierId;
    }

    public TreeSet<String> getNotBidTypeId() {
        return notBidTypeId;
    }

    public void setNotBidTypeId(TreeSet<String> notBidTypeId) {
        this.notBidTypeId = notBidTypeId;
    }

    public TreeSet<String> getNotProcuringEntityId() {
        return notProcuringEntityId;
    }

    public void setNotProcuringEntityId(TreeSet<String> notProcuringEntityId) {
        this.notProcuringEntityId = notProcuringEntityId;
    }

    public Boolean getElectronicSubmission() {
        return electronicSubmission;
    }

    public void setElectronicSubmission(Boolean electronicSubmission) {
        this.electronicSubmission = electronicSubmission;
    }

    public TreeSet<String> getProcurementMethod() {
        return procurementMethod;
    }

    public void setProcurementMethod(TreeSet<String> procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }


    public TreeSet<String> getAwardStatus() {
        return awardStatus;
    }

    public void setAwardStatus(TreeSet<String> awardStatus) {
        this.awardStatus = awardStatus;
    }

    public TreeSet<String> getBidderId() {
        return bidderId;
    }

    public void setBidderId(TreeSet<String> bidderId) {
        this.bidderId = bidderId;
    }

    public TreeSet<Integer> getTotalFlagged() {
        return totalFlagged;
    }

    public void setTotalFlagged(TreeSet<Integer> totalFlagged) {
        this.totalFlagged = totalFlagged;
    }
}
