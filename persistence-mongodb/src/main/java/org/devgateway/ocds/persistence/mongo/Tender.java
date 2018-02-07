package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Tender
 * <p>
 * Data regarding tender process - publicly inviting prospective contractors to submit bids for evaluation and
 * selecting a winner or winners.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "procuringEntity",
        "items",
        "value",
        "minValue",
        "procurementMethod",
        "procurementMethodDetails",
        "procurementMethodRationale",
        "mainProcurementCategory",
        "additionalProcurementCategories",
        "awardCriteria",
        "awardCriteriaDetails",
        "submissionMethod",
        "submissionMethodDetails",
        "tenderPeriod",
        "enquiryPeriod",
        "hasEnquiries",
        "eligibilityCriteria",
        "awardPeriod",
        "contractPeriod",
        "numberOfTenderers",
        "tenderers",
        "documents",
        "milestones",
        "amendments",
        "amendment"
})
public class Tender {

    /**
     * Tender ID
     * <p>
     * An identifier for this tender process. This may be the same as the ocid, or may be drawn from an internally
     * held identifier for this tender.
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this tender process. This may be the same as the ocid, or may be "
            + "drawn from an internally held identifier for this tender.")
    @ExcelExport
    private String id;
    /**
     * Tender title
     * <p>
     * A title for this tender. This will often be used by applications as a headline to attract interest, and to
     * help analysts understand the nature of this procurement.
     */
    @JsonProperty("title")
    @JsonPropertyDescription("A title for this tender. This will often be used by applications as a headline to "
            + "attract interest, and to help analysts understand the nature of this procurement.")
    @ExcelExport
    private String title;
    /**
     * Tender description
     * <p>
     * A summary description of the tender. This should complement structured information provided using the items
     * array. Descriptions should be short and easy to read. Avoid using ALL CAPS.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A summary description of the tender. This should complement structured information "
            + "provided using the items array. Descriptions should be short and easy to read. Avoid using ALL CAPS. ")
    @ExcelExport
    private String description;
    /**
     * Tender status
     * <p>
     * The current status of the tender based on the [tenderStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#tender-status)
     */
    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the tender based on the [tenderStatus codelist](http://standard"
            + ".open-contracting.org/latest/en/schema/codelists/#tender-status)")
    @ExcelExport
    private Status status;
    /**
     * Organization reference
     * <p>
     * The id and name of the party being referenced. Used to cross-reference to the parties section
     */
    @JsonProperty("procuringEntity")
    @JsonPropertyDescription("The id and name of the party being referenced. Used to cross-reference to the parties "
            + "section")
    @ExcelExport
    private Organization procuringEntity;
    /**
     * Items to be procured
     * <p>
     * The goods and services to be purchased, broken into line items wherever possible. Items should not be
     * duplicated, but a quantity of 2 specified instead.
     */
    @JsonProperty("items")
    @ExcelExport
    @ExcelExportSepareteSheet
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services to be purchased, broken into line items wherever possible. Items"
            + " should not be duplicated, but a quantity of 2 specified instead.")
    private Set<Item> items = new LinkedHashSet<Item>();
    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    @ExcelExport
    private Amount value;
    /**
     * Value
     * <p>
     */
    @JsonProperty("minValue")
    @ExcelExport
    private Amount minValue;
    /**
     * Procurement method
     * <p>
     * Specify tendering method using the [method codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#method). This is a closed codelist. Local method types should be mapped to
     * this list.
     */
    @JsonProperty("procurementMethod")
    @JsonPropertyDescription("Specify tendering method using the [method codelist](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#method). This is a closed codelist. Local method types should be "
            + "mapped to this list.")
    @ExcelExport
    private ProcurementMethod procurementMethod;
    /**
     * Procurement method details
     * <p>
     * Additional detail on the procurement method used. This field may be used to provide the local name of the
     * particular procurement method used.
     */
    @JsonProperty("procurementMethodDetails")
    @JsonPropertyDescription("Additional detail on the procurement method used. This field may be used to provide the"
            + " local name of the particular procurement method used.")
    private String procurementMethodDetails;
    /**
     * Procurement method rationale
     * <p>
     * Rationale for the chosen procurement method. This is especially important to provide a justification in the
     * case of limited tenders or direct awards.
     */
    @JsonProperty("procurementMethodRationale")
    @JsonPropertyDescription("Rationale for the chosen procurement method. This is especially important to provide a "
            + "justification in the case of limited tenders or direct awards.")
    @ExcelExport
    private String procurementMethodRationale;
    /**
     * Main procurement category
     * <p>
     * The primary category describing the main object of this contracting process from the [procurementCategory]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#procurement-category) codelist. This is a
     * closed codelist. Local classifications should be mapped to this list.
     */
    @JsonProperty("mainProcurementCategory")
    @JsonPropertyDescription("The primary category describing the main object of this contracting process from the "
            + "[procurementCategory](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#procurement-category) codelist. This is a closed codelist. Local "
            + "classifications should be mapped to this list.")
    private MainProcurementCategory mainProcurementCategory;
    /**
     * Additional procurement categories
     * <p>
     * Any additional categories which describe the objects of this contracting process, from the
     * [extendedProcurementCategory](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#extended-procurement-category) codelist. This is an open codelist. Local
     * categories can be included in this list.
     */
    @JsonProperty("additionalProcurementCategories")
    @JsonPropertyDescription("Any additional categories which describe the objects of this contracting process, from "
            + "the [extendedProcurementCategory](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#extended-procurement-category) codelist. This is an open codelist. "
            + "Local categories can be included in this list.")

    private List<String> additionalProcurementCategories = new ArrayList<String>();
    /**
     * Award criteria
     * <p>
     * Specify the award criteria for the procurement, using the [award criteria codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#award-criteria)
     */
    @JsonProperty("awardCriteria")
    @JsonPropertyDescription("Specify the award criteria for the procurement, using the [award criteria codelist]"
            + "(http://standard.open-contracting.org/latest/en/schema/codelists/#award-criteria)")
    @ExcelExport
    private String awardCriteria;
    /**
     * Award criteria details
     * <p>
     * Any detailed or further information on the award or selection criteria.
     */
    @JsonProperty("awardCriteriaDetails")
    @JsonPropertyDescription("Any detailed or further information on the award or selection criteria.")
    private String awardCriteriaDetails;
    /**
     * Submission method
     * <p>
     * Specify the method by which bids must be submitted, in person, written, or electronic auction. Using the
     * [submission method codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#submission-method)
     */
    @JsonProperty("submissionMethod")
    @JsonPropertyDescription("Specify the method by which bids must be submitted, in person, written, or electronic "
            + "auction. Using the [submission method codelist](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#submission-method)")
    @ExcelExport
    private List<String> submissionMethod = new ArrayList<String>();
    /**
     * Submission method details
     * <p>
     * Any detailed or further information on the submission method. This may include the address, e-mail address or
     * online service to which bids should be submitted, and any special requirements to be followed for submissions.
     */
    @JsonProperty("submissionMethodDetails")
    @JsonPropertyDescription("Any detailed or further information on the submission method. This may include the "
            + "address, e-mail address or online service to which bids should be submitted, and any special "
            + "requirements to be followed for submissions.")
    @ExcelExport
    private String submissionMethodDetails;
    /**
     * Period
     * <p>
     */
    @JsonProperty("tenderPeriod")
    @JsonPropertyDescription("    ")
    @ExcelExport
    private Period tenderPeriod;
    /**
     * Period
     * <p>
     */
    @JsonProperty("enquiryPeriod")
    @JsonPropertyDescription("    ")
    @ExcelExport
    private Period enquiryPeriod;
    /**
     * Has enquiries?
     * <p>
     * A true/false field to indicate whether any enquiries were received during the tender process. Structured
     * information on enquiries that were received, and responses to them, can be provided using the enquiries
     * extension.
     */
    @JsonProperty("hasEnquiries")
    @JsonPropertyDescription("A true/false field to indicate whether any enquiries were received during the tender "
            + "process. Structured information on enquiries that were received, and responses to them, can be "
            + "provided using the enquiries extension.")
    private Boolean hasEnquiries;
    /**
     * Eligibility criteria
     * <p>
     * A description of any eligibility criteria for potential suppliers.
     */
    @JsonProperty("eligibilityCriteria")
    @JsonPropertyDescription("A description of any eligibility criteria for potential suppliers.")
    @ExcelExport
    private String eligibilityCriteria;
    /**
     * Period
     * <p>
     */
    @JsonProperty("awardPeriod")
    @JsonPropertyDescription("    ")
    @ExcelExport
    private Period awardPeriod;
    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    @JsonPropertyDescription("    ")
    @ExcelExport
    private Period contractPeriod;
    /**
     * Number of tenderers
     * <p>
     * The number of parties who submit a bid.
     */
    @JsonProperty("numberOfTenderers")
    @ExcelExport
    @JsonPropertyDescription("The number of parties who submit a bid.")
    private Integer numberOfTenderers;
    /**
     * Tenderers
     * <p>
     * All parties who submit a bid on a tender. More detailed information on bids and the bidding organization can
     * be provided using the optional bid extension.
     */
    @JsonProperty("tenderers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All parties who submit a bid on a tender. More detailed information on bids and the "
            + "bidding organization can be provided using the optional bid extension.")
    @ExcelExport
    private Set<Organization> tenderers = new LinkedHashSet<Organization>();
    /**
     * Documents
     * <p>
     * All documents and attachments related to the tender, including any notices. See the [documentType codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#document-type) for details of potential
     * documents to include. Common documents include official legal notices of tender, technical specifications,
     * evaluation criteria, and, as a tender process progresses, clarifications and replies to queries.
     */
    @JsonProperty("documents")
    @JsonPropertyDescription("All documents and attachments related to the tender, including any notices. See the "
            + "[documentType codelist](http://standard.open-contracting"
            + ".org/latest/en/schema/codelists/#document-type) for details of potential documents to include. Common "
            + "documents include official legal notices of tender, technical specifications, evaluation criteria, "
            + "and, as a tender process progresses, clarifications and replies to queries.")
    @ExcelExport
    private List<Document> documents = new ArrayList<Document>();
    /**
     * Milestones
     * <p>
     * A list of milestones associated with the tender.
     */
    @JsonProperty("milestones")
    @JsonPropertyDescription("A list of milestones associated with the tender.")
    private List<Milestone> milestones = new ArrayList<Milestone>();
    /**
     * Amendments
     * <p>
     * A tender amendment is a formal change to the tender, and generally involves the publication of a new tender
     * notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    @JsonPropertyDescription("A tender amendment is a formal change to the tender, and generally involves the "
            + "publication of a new tender notice/release. The rationale and a description of the changes made can be"
            + " provided here.")
    private List<Amendment> amendments = new ArrayList<Amendment>();
    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    private Amendment amendment;

    /**
     * Tender ID
     * <p>
     * An identifier for this tender process. This may be the same as the ocid, or may be drawn from an internally
     * held identifier for this tender.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Tender ID
     * <p>
     * An identifier for this tender process. This may be the same as the ocid, or may be drawn from an internally
     * held identifier for this tender.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Tender title
     * <p>
     * A title for this tender. This will often be used by applications as a headline to attract interest, and to
     * help analysts understand the nature of this procurement.
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Tender title
     * <p>
     * A title for this tender. This will often be used by applications as a headline to attract interest, and to
     * help analysts understand the nature of this procurement.
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Tender description
     * <p>
     * A summary description of the tender. This should complement structured information provided using the items
     * array. Descriptions should be short and easy to read. Avoid using ALL CAPS.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Tender description
     * <p>
     * A summary description of the tender. This should complement structured information provided using the items
     * array. Descriptions should be short and easy to read. Avoid using ALL CAPS.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Tender status
     * <p>
     * The current status of the tender based on the [tenderStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#tender-status)
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Tender status
     * <p>
     * The current status of the tender based on the [tenderStatus codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#tender-status)
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Organization reference
     * <p>
     * The id and name of the party being referenced. Used to cross-reference to the parties section
     */
    @JsonProperty("procuringEntity")
    public Organization getProcuringEntity() {
        return procuringEntity;
    }

    /**
     * Organization reference
     * <p>
     * The id and name of the party being referenced. Used to cross-reference to the parties section
     */
    @JsonProperty("procuringEntity")
    public void setProcuringEntity(Organization procuringEntity) {
        this.procuringEntity = procuringEntity;
    }

    /**
     * Items to be procured
     * <p>
     * The goods and services to be purchased, broken into line items wherever possible. Items should not be
     * duplicated, but a quantity of 2 specified instead.
     */
    @JsonProperty("items")
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Items to be procured
     * <p>
     * The goods and services to be purchased, broken into line items wherever possible. Items should not be
     * duplicated, but a quantity of 2 specified instead.
     */
    @JsonProperty("items")
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    public Amount getValue() {
        return value;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("value")
    public void setValue(Amount value) {
        this.value = value;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("minValue")
    public Amount getMinValue() {
        return minValue;
    }

    /**
     * Value
     * <p>
     */
    @JsonProperty("minValue")
    public void setMinValue(Amount minValue) {
        this.minValue = minValue;
    }

    /**
     * Procurement method
     * <p>
     * Specify tendering method using the [method codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#method). This is a closed codelist. Local method types should be mapped to
     * this list.
     */
    @JsonProperty("procurementMethod")
    public ProcurementMethod getProcurementMethod() {
        return procurementMethod;
    }

    /**
     * Procurement method
     * <p>
     * Specify tendering method using the [method codelist](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#method). This is a closed codelist. Local method types should be mapped to
     * this list.
     */
    @JsonProperty("procurementMethod")
    public void setProcurementMethod(ProcurementMethod procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    /**
     * Procurement method details
     * <p>
     * Additional detail on the procurement method used. This field may be used to provide the local name of the
     * particular procurement method used.
     */
    @JsonProperty("procurementMethodDetails")
    public String getProcurementMethodDetails() {
        return procurementMethodDetails;
    }

    /**
     * Procurement method details
     * <p>
     * Additional detail on the procurement method used. This field may be used to provide the local name of the
     * particular procurement method used.
     */
    @JsonProperty("procurementMethodDetails")
    public void setProcurementMethodDetails(String procurementMethodDetails) {
        this.procurementMethodDetails = procurementMethodDetails;
    }

    /**
     * Procurement method rationale
     * <p>
     * Rationale for the chosen procurement method. This is especially important to provide a justification in the
     * case of limited tenders or direct awards.
     */
    @JsonProperty("procurementMethodRationale")
    public String getProcurementMethodRationale() {
        return procurementMethodRationale;
    }

    /**
     * Procurement method rationale
     * <p>
     * Rationale for the chosen procurement method. This is especially important to provide a justification in the
     * case of limited tenders or direct awards.
     */
    @JsonProperty("procurementMethodRationale")
    public void setProcurementMethodRationale(String procurementMethodRationale) {
        this.procurementMethodRationale = procurementMethodRationale;
    }

    /**
     * Main procurement category
     * <p>
     * The primary category describing the main object of this contracting process from the [procurementCategory]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#procurement-category) codelist. This is a
     * closed codelist. Local classifications should be mapped to this list.
     */
    @JsonProperty("mainProcurementCategory")
    public MainProcurementCategory getMainProcurementCategory() {
        return mainProcurementCategory;
    }

    /**
     * Main procurement category
     * <p>
     * The primary category describing the main object of this contracting process from the [procurementCategory]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#procurement-category) codelist. This is a
     * closed codelist. Local classifications should be mapped to this list.
     */
    @JsonProperty("mainProcurementCategory")
    public void setMainProcurementCategory(MainProcurementCategory mainProcurementCategory) {
        this.mainProcurementCategory = mainProcurementCategory;
    }

    /**
     * Additional procurement categories
     * <p>
     * Any additional categories which describe the objects of this contracting process, from the
     * [extendedProcurementCategory](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#extended-procurement-category) codelist. This is an open codelist. Local
     * categories can be included in this list.
     */
    @JsonProperty("additionalProcurementCategories")
    public List<String> getAdditionalProcurementCategories() {
        return additionalProcurementCategories;
    }

    /**
     * Additional procurement categories
     * <p>
     * Any additional categories which describe the objects of this contracting process, from the
     * [extendedProcurementCategory](http://standard.open-contracting
     * .org/latest/en/schema/codelists/#extended-procurement-category) codelist. This is an open codelist. Local
     * categories can be included in this list.
     */
    @JsonProperty("additionalProcurementCategories")
    public void setAdditionalProcurementCategories(List<String> additionalProcurementCategories) {
        this.additionalProcurementCategories = additionalProcurementCategories;
    }

    /**
     * Award criteria
     * <p>
     * Specify the award criteria for the procurement, using the [award criteria codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#award-criteria)
     */
    @JsonProperty("awardCriteria")
    public String getAwardCriteria() {
        return awardCriteria;
    }

    /**
     * Award criteria
     * <p>
     * Specify the award criteria for the procurement, using the [award criteria codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#award-criteria)
     */
    @JsonProperty("awardCriteria")
    public void setAwardCriteria(String awardCriteria) {
        this.awardCriteria = awardCriteria;
    }

    /**
     * Award criteria details
     * <p>
     * Any detailed or further information on the award or selection criteria.
     */
    @JsonProperty("awardCriteriaDetails")
    public String getAwardCriteriaDetails() {
        return awardCriteriaDetails;
    }

    /**
     * Award criteria details
     * <p>
     * Any detailed or further information on the award or selection criteria.
     */
    @JsonProperty("awardCriteriaDetails")
    public void setAwardCriteriaDetails(String awardCriteriaDetails) {
        this.awardCriteriaDetails = awardCriteriaDetails;
    }

    /**
     * Submission method
     * <p>
     * Specify the method by which bids must be submitted, in person, written, or electronic auction. Using the
     * [submission method codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#submission-method)
     */
    @JsonProperty("submissionMethod")
    public List<String> getSubmissionMethod() {
        return submissionMethod;
    }

    /**
     * Submission method
     * <p>
     * Specify the method by which bids must be submitted, in person, written, or electronic auction. Using the
     * [submission method codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#submission-method)
     */
    @JsonProperty("submissionMethod")
    public void setSubmissionMethod(List<String> submissionMethod) {
        this.submissionMethod = submissionMethod;
    }

    /**
     * Submission method details
     * <p>
     * Any detailed or further information on the submission method. This may include the address, e-mail address or
     * online service to which bids should be submitted, and any special requirements to be followed for submissions.
     */
    @JsonProperty("submissionMethodDetails")
    public String getSubmissionMethodDetails() {
        return submissionMethodDetails;
    }

    /**
     * Submission method details
     * <p>
     * Any detailed or further information on the submission method. This may include the address, e-mail address or
     * online service to which bids should be submitted, and any special requirements to be followed for submissions.
     */
    @JsonProperty("submissionMethodDetails")
    public void setSubmissionMethodDetails(String submissionMethodDetails) {
        this.submissionMethodDetails = submissionMethodDetails;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("tenderPeriod")
    public Period getTenderPeriod() {
        return tenderPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("tenderPeriod")
    public void setTenderPeriod(Period tenderPeriod) {
        this.tenderPeriod = tenderPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("enquiryPeriod")
    public Period getEnquiryPeriod() {
        return enquiryPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("enquiryPeriod")
    public void setEnquiryPeriod(Period enquiryPeriod) {
        this.enquiryPeriod = enquiryPeriod;
    }

    /**
     * Has enquiries?
     * <p>
     * A true/false field to indicate whether any enquiries were received during the tender process. Structured
     * information on enquiries that were received, and responses to them, can be provided using the enquiries
     * extension.
     */
    @JsonProperty("hasEnquiries")
    public Boolean getHasEnquiries() {
        return hasEnquiries;
    }

    /**
     * Has enquiries?
     * <p>
     * A true/false field to indicate whether any enquiries were received during the tender process. Structured
     * information on enquiries that were received, and responses to them, can be provided using the enquiries
     * extension.
     */
    @JsonProperty("hasEnquiries")
    public void setHasEnquiries(Boolean hasEnquiries) {
        this.hasEnquiries = hasEnquiries;
    }

    /**
     * Eligibility criteria
     * <p>
     * A description of any eligibility criteria for potential suppliers.
     */
    @JsonProperty("eligibilityCriteria")
    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    /**
     * Eligibility criteria
     * <p>
     * A description of any eligibility criteria for potential suppliers.
     */
    @JsonProperty("eligibilityCriteria")
    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("awardPeriod")
    public Period getAwardPeriod() {
        return awardPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("awardPeriod")
    public void setAwardPeriod(Period awardPeriod) {
        this.awardPeriod = awardPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    public Period getContractPeriod() {
        return contractPeriod;
    }

    /**
     * Period
     * <p>
     */
    @JsonProperty("contractPeriod")
    public void setContractPeriod(Period contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    /**
     * Number of tenderers
     * <p>
     * The number of parties who submit a bid.
     */
    @JsonProperty("numberOfTenderers")
    public Integer getNumberOfTenderers() {
        return numberOfTenderers;
    }

    /**
     * Number of tenderers
     * <p>
     * The number of parties who submit a bid.
     */
    @JsonProperty("numberOfTenderers")
    public void setNumberOfTenderers(Integer numberOfTenderers) {
        this.numberOfTenderers = numberOfTenderers;
    }

    /**
     * Tenderers
     * <p>
     * All parties who submit a bid on a tender. More detailed information on bids and the bidding organization can
     * be provided using the optional bid extension.
     */
    @JsonProperty("tenderers")
    public Set<Organization> getTenderers() {
        return tenderers;
    }

    /**
     * Tenderers
     * <p>
     * All parties who submit a bid on a tender. More detailed information on bids and the bidding organization can
     * be provided using the optional bid extension.
     */
    @JsonProperty("tenderers")
    public void setTenderers(Set<Organization> tenderers) {
        this.tenderers = tenderers;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the tender, including any notices. See the [documentType codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#document-type) for details of potential
     * documents to include. Common documents include official legal notices of tender, technical specifications,
     * evaluation criteria, and, as a tender process progresses, clarifications and replies to queries.
     */
    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * Documents
     * <p>
     * All documents and attachments related to the tender, including any notices. See the [documentType codelist]
     * (http://standard.open-contracting.org/latest/en/schema/codelists/#document-type) for details of potential
     * documents to include. Common documents include official legal notices of tender, technical specifications,
     * evaluation criteria, and, as a tender process progresses, clarifications and replies to queries.
     */
    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * Milestones
     * <p>
     * A list of milestones associated with the tender.
     */
    @JsonProperty("milestones")
    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Milestones
     * <p>
     * A list of milestones associated with the tender.
     */
    @JsonProperty("milestones")
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

    /**
     * Amendments
     * <p>
     * A tender amendment is a formal change to the tender, and generally involves the publication of a new tender
     * notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    public List<Amendment> getAmendments() {
        return amendments;
    }

    /**
     * Amendments
     * <p>
     * A tender amendment is a formal change to the tender, and generally involves the publication of a new tender
     * notice/release. The rationale and a description of the changes made can be provided here.
     */
    @JsonProperty("amendments")
    public void setAmendments(List<Amendment> amendments) {
        this.amendments = amendments;
    }

    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    public Amendment getAmendment() {
        return amendment;
    }

    /**
     * Amendment
     * <p>
     * Amendment information
     */
    @JsonProperty("amendment")
    public void setAmendment(Amendment amendment) {
        this.amendment = amendment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("title", title)
                .append("description", description)
                .append("status", status)
                .append("procuringEntity", procuringEntity)
                .append("items", items)
                .append("value", value)
                .append("minValue", minValue)
                .append("procurementMethod", procurementMethod)
                .append("procurementMethodDetails", procurementMethodDetails)
                .append("procurementMethodRationale", procurementMethodRationale)
                .append("mainProcurementCategory", mainProcurementCategory)
                .append("additionalProcurementCategories", additionalProcurementCategories)
                .append("awardCriteria", awardCriteria)
                .append("awardCriteriaDetails", awardCriteriaDetails)
                .append("submissionMethod", submissionMethod)
                .append("submissionMethodDetails", submissionMethodDetails)
                .append("tenderPeriod", tenderPeriod)
                .append("enquiryPeriod", enquiryPeriod)
                .append("hasEnquiries", hasEnquiries)
                .append("eligibilityCriteria", eligibilityCriteria)
                .append("awardPeriod", awardPeriod)
                .append("contractPeriod", contractPeriod)
                .append("numberOfTenderers", numberOfTenderers)
                .append("tenderers", tenderers)
                .append("documents", documents)
                .append("milestones", milestones)
                .append("amendments", amendments)
                .append("amendment", amendment)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amendment)
                .append(documents)
                .append(awardPeriod)
                .append(description)
                .append(amendments)
                .append(mainProcurementCategory)
                .append(title)
                .append(procurementMethodDetails)
                .append(additionalProcurementCategories)
                .append(minValue)
                .append(procurementMethod)
                .append(enquiryPeriod)
                .append(awardCriteria)
                .append(eligibilityCriteria)
                .append(id)
                .append(value)
                .append(tenderPeriod)
                .append(procurementMethodRationale)
                .append(procuringEntity)
                .append(submissionMethod)
                .append(hasEnquiries)
                .append(contractPeriod)
                .append(numberOfTenderers)
                .append(submissionMethodDetails)
                .append(awardCriteriaDetails)
                .append(milestones)
                .append(items)
                .append(tenderers)
                .append(status)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Tender)) {
            return false;
        }
        Tender rhs = ((Tender) other);
        return new EqualsBuilder().append(amendment, rhs.amendment)
                .append(documents, rhs.documents)
                .append(awardPeriod, rhs.awardPeriod)
                .append(description, rhs.description)
                .append(amendments, rhs.amendments)
                .append(mainProcurementCategory, rhs.mainProcurementCategory)
                .append(title, rhs.title)
                .append(procurementMethodDetails, rhs.procurementMethodDetails)
                .append(additionalProcurementCategories, rhs.additionalProcurementCategories)
                .append(minValue, rhs.minValue)
                .append(procurementMethod, rhs.procurementMethod)
                .append(enquiryPeriod, rhs.enquiryPeriod)
                .append(awardCriteria, rhs.awardCriteria)
                .append(eligibilityCriteria, rhs.eligibilityCriteria)
                .append(id, rhs.id)
                .append(value, rhs.value)
                .append(tenderPeriod, rhs.tenderPeriod)
                .append(procurementMethodRationale, rhs.procurementMethodRationale)
                .append(procuringEntity, rhs.procuringEntity)
                .append(submissionMethod, rhs.submissionMethod)
                .append(hasEnquiries, rhs.hasEnquiries)
                .append(contractPeriod, rhs.contractPeriod)
                .append(numberOfTenderers, rhs.numberOfTenderers)
                .append(submissionMethodDetails, rhs.submissionMethodDetails)
                .append(awardCriteriaDetails, rhs.awardCriteriaDetails)
                .append(milestones, rhs.milestones)
                .append(items, rhs.items)
                .append(tenderers, rhs.tenderers)
                .append(status, rhs.status)
                .isEquals();
    }

    public enum MainProcurementCategory {

        GOODS("goods"),
        WORKS("works"),
        SERVICES("services");
        private final String value;
        private static final Map<String, MainProcurementCategory> CONSTANTS = new HashMap<String,
                MainProcurementCategory>();

        static {
            for (MainProcurementCategory c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MainProcurementCategory(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static MainProcurementCategory fromValue(String value) {
            MainProcurementCategory constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SubmissionMethod {
        electronicAuction("electronicAuction"),
        electronicSubmission("electronicSubmission"),
        written("written"),
        inPerson("inPerson");

        private final String value;

        private static final Map<String, SubmissionMethod> CONSTANTS = new HashMap<String, SubmissionMethod>();

        static {
            for (SubmissionMethod c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SubmissionMethod(final String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }
    }


    public enum ProcurementMethod {

        open("open"),
        selective("selective"),
        limited("limited"),
        direct("direct");
        private final String value;
        private static final Map<String, ProcurementMethod> CONSTANTS = new HashMap<String, ProcurementMethod>();

        static {
            for (ProcurementMethod c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ProcurementMethod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static ProcurementMethod fromValue(String value) {
            ProcurementMethod constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Status {

        planning("planning"),
        planned("planned"),
        active("active"),
        cancelled("cancelled"),
        unsuccessful("unsuccessful"),
        complete("complete"),
        withdrawn("withdrawn");
        private final String value;
        private static final Map<String, Status> CONSTANTS = new HashMap<String, Status>();

        static {
            for (Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Status fromValue(String value) {
            Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
