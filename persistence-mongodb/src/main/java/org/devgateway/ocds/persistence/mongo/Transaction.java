package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.Date;


/**
 * Transaction Information
 * <p>
 * A spending transaction related to the contracting process. Draws upon the data models of the
 * [Budget Data Package](https://github.com/openspending/budget-data-package/blob/master/specification.md) and the
 * [International Aid Transpareny Initiative]
 *  (http://iatistandard.org/activity-standard/iati-activities/iati-activity/transaction/)
 *  and should be used to cross-reference to more detailed information held using a Budget Data Package, IATI file,
 *  or to provide enough information to allow a user to manually or automatically cross-reference with
 *  some other published source of transactional spending data.
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#transaction
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "source",
        "date",
        "amount",
        "providerOrganization",
        "receiverOrganization",
        "uri"
})
public class Transaction {

    /**
     * A unique identifier for this transaction. This identifier should be possible to cross-reference against the
     * provided data source. For the budget data package this is the id, for IATI, the transaction reference.
     * (Required)
     *
     */
    @JsonProperty("id")
    private String id;

    /**
     * Data Source
     * <p>
     * Used to point either to a corresponding Budget Data Package, IATI file, or machine or
     * human-readable source where users can find further information on the budget line item identifiers,
     * or project identifiers, provided here.
     *
     */
    @ExcelExport
    @JsonProperty("source")
    private String source;

    /**
     * The date of the transaction
     *
     */
    @ExcelExport
    @JsonProperty("date")
    private Date date;

    @ExcelExport
    @JsonProperty("amount")
    private Amount amount;

    @ExcelExport
    @JsonProperty("providerOrganization")
    private Identifier providerOrganization;

    @ExcelExport
    @JsonProperty("receiverOrganization")
    private Identifier receiverOrganization;

    /**
     * Linked spending information
     * <p>
     * A URI pointing directly to a machine-readable record about this spending transaction.
     *
     */
    @JsonProperty("uri")
    private String uri;

    /**
     * A unique identifier for this transaction. This identifier should be possible to cross-reference against the
     * provided data source. For the budget data package this is the id, for IATI, the transaction reference.
     * (Required)
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A unique identifier for this transaction. This identifier should be possible to cross-reference against the
     * provided data source. For the budget data package this is the id, for IATI, the transaction reference.
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
     * Data Source
     * <p>
     * Used to point either to a corresponding Budget Data Package, IATI file, or machine or
     * human-readable source where users can find further information on the budget line item identifiers,
     * or project identifiers, provided here.
     *
     * @return
     *     The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * Data Source
     * <p>
     * Used to point either to a corresponding Budget Data Package, IATI file, or machine or
     * human-readable source where users can find further information on the budget line item identifiers,
     * or project identifiers, provided here.
     *
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * The date of the transaction
     *
     * @return
     *     The date
     */
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    /**
     * The date of the transaction
     *
     * @param date
     *     The date
     */
    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     *     The amount
     */
    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     *     The amount
     */
    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     *     The providerOrganization
     */
    @JsonProperty("providerOrganization")
    public Identifier getProviderOrganization() {
        return providerOrganization;
    }

    /**
     *
     * @param providerOrganization
     *     The providerOrganization
     */
    @JsonProperty("providerOrganization")
    public void setProviderOrganization(Identifier providerOrganization) {
        this.providerOrganization = providerOrganization;
    }

    /**
     *
     * @return
     *     The receiverOrganization
     */
    @JsonProperty("receiverOrganization")
    public Identifier getReceiverOrganization() {
        return receiverOrganization;
    }

    /**
     *
     * @param receiverOrganization
     *     The receiverOrganization
     */
    @JsonProperty("receiverOrganization")
    public void setReceiverOrganization(Identifier receiverOrganization) {
        this.receiverOrganization = receiverOrganization;
    }

    /**
     * Linked spending information
     * <p>
     * A URI pointing directly to a machine-readable record about this spending transaction.
     *
     * @return
     *     The uri
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    /**
     * Linked spending information
     * <p>
     * A URI pointing directly to a machine-readable record about this spending transaction.
     *
     * @param uri
     *     The uri
     */
    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(id).
                append(source).
                append(date).
                append(amount).
                append(providerOrganization).
                append(receiverOrganization).
                append(uri).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction rhs = ((Transaction) other);
        return new EqualsBuilder().
                append(id, rhs.id).
                append(source, rhs.source).
                append(date, rhs.date).
                append(amount, rhs.amount).
                append(providerOrganization, rhs.providerOrganization).
                append(receiverOrganization, rhs.receiverOrganization).
                append(uri, rhs.uri).
                isEquals();
    }

}
