package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Schema for an Open Contracting Release
 * <p>
 *
 *  http://standard.open-contracting.org/latest/en/schema/release/
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "ocid",
        "date",
        "tag",
        "initiationType",
        "planning",
        "tender",
        "buyer",
        "awards",
        "contracts",
        "language"
})
@Document
public class Release implements Identifiable {
    /**
     * Release ID
     * <p>
     * A unique identifier that identifies this release. A releaseID must be unique within a release-package
     * and must not contain the # character.
     * (Required)
     *
     */
    @JsonProperty("id")
    @Id
    private String id;

    /**
     * Open Contracting ID
     * <p>
     * A globally unique identifier for this Open Contracting Process. Composed of a publisher prefix and
     * an identifier for the contracting process. For more information see the
     * [Open Contracting Identifier guidance]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/#ocid)
     * (Required)
     *
     */
    @JsonProperty("ocid")
    private String ocid;

    /**
     * Release Date
     * <p>
     * The date this information is released, it may well be the same as the parent publishedDate,
     *  it must not be later than the publishedDate from the parent package. It is used to determine merge order.
     * (Required)
     *
     */
    @JsonProperty("date")
    @CreatedDate
    private Date date;

    /**
     * Release Tag
     * <p>
     * A value from the
     * [releaseTag codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#release-tag)
     * that identifies the nature of the release being made. Tags may be used to filter release, or, in future,
     * for for advanced validation when certain kinds of releases should contain certain fields.
     * (Required)
     *
     */
    @JsonProperty("tag")
    private List<Tag> tag = new ArrayList<Tag>();

    /**
     * Initiation type
     * <p>
     * String specifying the type of initiation process used for this contract, taken from the
     * [initiationType](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#initiation-type)
     * codelist. Currently only tender is supported.
     * (Required)
     *
     */
    @JsonProperty("initiationType")
    private InitiationType initiationType = InitiationType.tender;

    /**
     * Planning
     * <p>
     * Information from the planning phase of the contracting process. Note that many other fields may be filled
     * in a planning release, in the appropriate fields in other schema sections, these would likely be estimates
     * at this stage e.g. totalValue in tender
     *
     */
    @JsonProperty("planning")
    private Planning planning;

    /**
     * Tender
     * <p>
     * Data regarding tender process - publicly inviting prospective contractors to submit bids for evaluation
     * and selecting a winner or winners.
     *
     */
    @JsonProperty("tender")
    private Tender tender;

    /**
     * Organization
     * <p>
     * An organization.
     *
     */
    @JsonProperty("buyer")
    private Organization buyer;

    /**
     * Awards
     * <p>
     * Information from the award phase of the contracting process. There may be more than one award per contracting
     * process e.g. because the contract is split amongst different providers, or because it is a standing offer.
     *
     */
    @JsonProperty("awards")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Award> awards = new LinkedHashSet<Award>();

    /**
     * Contracts
     * <p>
     * Information from the contract creation phase of the procurement process.
     *
     */
    @JsonProperty("contracts")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Contract> contracts = new LinkedHashSet<Contract>();

    /**
     * Release language
     * <p>
     * Specifies the default language of the data using either two-digit ISO 639-1, or extended BCP47 language tags.
     * The use of two-letter codes from ISO 639-1 is strongly recommended.
     *
     */
    @JsonProperty("language")
    private String language = "en";


    /**
     * Open Contracting ID
     * <p>
     * A globally unique identifier for this Open Contracting Process. Composed of a publisher prefix and an identifier
     * for the contracting process. For more information see the
     * [Open Contracting Identifier guidance]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/#ocid)
     * (Required)
     *
     * @return
     *     The ocid
     */
    @JsonProperty("ocid")
    public String getOcid() {
        return ocid;
    }

    /**
     * Open Contracting ID
     * <p>
     * A globally unique identifier for this Open Contracting Process. Composed of a publisher prefix and an identifier
     * for the contracting process. For more information see the
     *  [Open Contracting Identifier guidance]
     *      (http://ocds.open-contracting.org/standard/r/1__0__0/en/key_concepts/identifiers/#ocid)
     * (Required)
     *
     * @param ocid
     *     The ocid
     */
    @JsonProperty("ocid")
    public void setOcid(String ocid) {
        this.ocid = ocid;
    }

    /**
     * Release ID
     * <p>
     * A unique identifier that identifies this release. A releaseID must be unique within a release-package
     * and must not contain the # character.
     * (Required)
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    @Override
    public String getId() {
        return id;
    }

    /**
     * Release ID
     * <p>
     * A unique identifier that identifies this release. A releaseID must be unique within a release-package
     * and must not contain the # character.
     * (Required)
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Release Date
     * <p>
     * The date this information is released, it may well be the same as the parent publishedDate,
     * it must not be later than the publishedDate from the parent package. It is used to determine merge order.
     * (Required)
     *
     * @return
     *     The date
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * Release Date
     * <p>
     * The date this information is released, it may well be the same as the parent publishedDate,
     * it must not be later than the publishedDate from the parent package. It is used to determine merge order.
     * (Required)
     *
     * @param date
     *     The date
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Release Tag
     * <p>
     * A value from the
     * [releaseTag codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#release-tag)
     * that identifies the nature of the release being made. Tags may be used to filter release, or, in future,
     * for for advanced validation when certain kinds of releases should contain certain fields.
     * (Required)
     *
     * @return
     *     The tag
     */
    @JsonProperty("tag")
    public List<Tag> getTag() {
        return tag;
    }

    /**
     * Release Tag
     * <p>
     * A value from the
     * [releaseTag codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#release-tag)
     * that identifies the nature of the release being made. Tags may be used to filter release, or, in future,
     * for for advanced validation when certain kinds of releases should contain certain fields.
     * (Required)
     *
     * @param tag
     *     The tag
     */
    @JsonProperty("tag")
    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    /**
     * Initiation type
     * <p>
     * String specifying the type of initiation process used for this contract, taken from the
     *  [initiationType](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#initiation-type)
     * codelist. Currently only tender is supported.
     * (Required)
     *
     * @return
     *     The initiationType
     */
    @JsonProperty("initiationType")
    public InitiationType getInitiationType() {
        return initiationType;
    }

    /**
     * Initiation type
     * <p>
     * String specifying the type of initiation process used for this contract, taken from the
     *  [initiationType](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#initiation-type)
     * codelist. Currently only tender is supported.
     * (Required)
     *
     * @param initiationType
     *     The initiationType
     */
    @JsonProperty("initiationType")
    public void setInitiationType(InitiationType initiationType) {
        this.initiationType = initiationType;
    }

    /**
     * Planning
     * <p>
     * Information from the planning phase of the contracting process. Note that many other fields may be filled
     * in a planning release, in the appropriate fields in other schema sections, these would likely be estimates at
     * this stage e.g. totalValue in tender
     *
     * @return
     *     The planning
     */
    @JsonProperty("planning")
    public Planning getPlanning() {
        return planning;
    }

    /**
     * Planning
     * <p>
     * Information from the planning phase of the contracting process. Note that many other fields may be filled
     * in a planning release, in the appropriate fields in other schema sections, these would likely be estimates at
     * this stage e.g. totalValue in tender
     *
     * @param planning
     *     The planning
     */
    @JsonProperty("planning")
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    /**
     * Tender
     * <p>
     * Data regarding tender process - publicly inviting prospective contractors to submit bids for evaluation and
     * selecting a winner or winners.
     *
     * @return
     *     The tender
     */
    @JsonProperty("tender")
    public Tender getTender() {
        return tender;
    }

    /**
     * Tender
     * <p>
     * Data regarding tender process - publicly inviting prospective contractors to submit bids for evaluation and
     * selecting a winner or winners.
     *
     * @param tender
     *     The tender
     */
    @JsonProperty("tender")
    public void setTender(Tender tender) {
        this.tender = tender;
    }

    /**
     * Organization
     * <p>
     * An organization.
     *
     * @return
     *     The buyer
     */
    @JsonProperty("buyer")
    public Organization getBuyer() {
        return buyer;
    }

    /**
     * Organization
     * <p>
     * An organization.
     *
     * @param buyer
     *     The buyer
     */
    @JsonProperty("buyer")
    public void setBuyer(Organization buyer) {
        this.buyer = buyer;
    }

    /**
     * Awards
     * <p>
     * Information from the award phase of the contracting process. There may be more than one award per contracting
     * process e.g. because the contract is split amongst different providers, or because it is a standing offer.
     *
     * @return
     *     The awards
     */
    @JsonProperty("awards")
    public Set<Award> getAwards() {
        return awards;
    }

    /**
     * Awards
     * <p>
     * Information from the award phase of the contracting process. There may be more than one award per contracting
     * process e.g. because the contract is split amongst different providers, or because it is a standing offer.
     *
     * @param awards
     *     The awards
     */
    @JsonProperty("awards")
    public void setAwards(Set<Award> awards) {
        this.awards = awards;
    }

    /**
     * Contracts
     * <p>
     * Information from the contract creation phase of the procurement process.
     *
     * @return
     *     The contracts
     */
    @JsonProperty("contracts")
    public Set<Contract> getContracts() {
        return contracts;
    }

    /**
     * Contracts
     * <p>
     * Information from the contract creation phase of the procurement process.
     *
     * @param contracts
     *     The contracts
     */
    @JsonProperty("contracts")
    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * Release language
     * <p>
     * Specifies the default language of the data using either two-digit ISO 639-1, or extended BCP47 language tags.
     * The use of two-letter codes from ISO 639-1 is strongly recommended.
     *
     * @return
     *     The language
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * Release language
     * <p>
     * Specifies the default language of the data using either two-digit ISO 639-1, or extended BCP47 language tags.
     * The use of two-letter codes from ISO 639-1 is strongly recommended.
     *
     * @param language
     *     The language
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(ocid).
                append(id).
                append(date).
                append(tag).
                append(initiationType).
                append(planning).
                append(tender).
                append(buyer).
                append(awards).
                append(contracts).
                append(language).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Release)) {
            return false;
        }
        Release rhs = ((Release) other);
        return new EqualsBuilder().
                append(ocid, rhs.ocid).
                append(id, rhs.id).
                append(date, rhs.date).
                append(tag, rhs.tag).
                append(initiationType, rhs.initiationType).
                append(planning, rhs.planning).
                append(tender, rhs.tender).
                append(buyer, rhs.buyer).
                append(awards, rhs.awards).
                append(contracts, rhs.contracts).
                append(language, rhs.language).
                isEquals();
    }

    public enum InitiationType {
        tender("tender");

        private final String value;

        private static final Map<String, InitiationType> CONSTANTS = new HashMap<>();

        static {
            for (InitiationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        InitiationType(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static InitiationType fromValue(String value) {
            InitiationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }

}
