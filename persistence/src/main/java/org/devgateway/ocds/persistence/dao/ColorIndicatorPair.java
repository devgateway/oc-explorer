/**
 *
 */
package org.devgateway.ocds.persistence.dao;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author mpostelnicu
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "firstIndicator"), @Index(columnList = "secondIndicator")})
public class ColorIndicatorPair extends AbstractAuditableEntity implements Serializable {

    private String firstIndicator;

    private String secondIndicator;

    private String color;

    public String getFirstIndicator() {
        return firstIndicator;
    }

    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    public String getSecondIndicator() {
        return secondIndicator;
    }

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
