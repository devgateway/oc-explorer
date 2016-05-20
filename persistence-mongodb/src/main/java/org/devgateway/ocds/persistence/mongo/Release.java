/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai Release OCDS Entity
 *         http://standard.open-contracting.org/latest/en/schema/release/
 */
@Document
public class Release {
    @Id
    private String id;

    private String ocid;

    @CreatedDate
    private Date date;

    private Set<String> tag = new HashSet<>();

    private String initiationType = "tender";

    private Planning planning;

    private Tender tender;

    private Organization buyer;

    private List<Award> awards = new ArrayList<>();

    private List<Contract> contracts = new ArrayList<>();

    private String language = "en";

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getOcid() {
        return ocid;
    }

    public void setOcid(final String ocid) {
        this.ocid = ocid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getInitiationType() {
        return initiationType;
    }

    public void setInitiationType(final String initiationType) {
        this.initiationType = initiationType;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(final Planning planning) {
        this.planning = planning;
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(final Tender tender) {
        this.tender = tender;
    }

    public Organization getBuyer() {
        return buyer;
    }

    public void setBuyer(final Organization buyer) {
        this.buyer = buyer;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(final List<Award> awards) {
        this.awards = awards;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public Set<String> getTag() {
        return tag;
    }

    public void setTag(final Set<String> tag) {
        this.tag = tag;
    }

}
