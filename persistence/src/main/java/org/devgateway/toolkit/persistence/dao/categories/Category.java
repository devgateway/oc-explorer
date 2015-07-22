package org.devgateway.toolkit.persistence.dao.categories;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author idobre
 * @since 11/18/14
 */

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorColumn(length = 100)
@Table(indexes= {@Index(columnList="label"),@Index(columnList="DTYPE")
})
public class Category extends AbstractAuditableEntity implements Serializable, Labelable {

	private static final long serialVersionUID = 1L;

	protected String label;

    protected String description;

    public Category(String label) {
        this.label = label;
    }

    public Category() {

    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getLabel();
    }



    /* (non-Javadoc)
         * @see org.devgateway.ccrs.persistence.dao.AbstractAuditableEntity#getParent()
         */
	@Override
	public AbstractAuditableEntity getParent() {
		return null;
	}

}
