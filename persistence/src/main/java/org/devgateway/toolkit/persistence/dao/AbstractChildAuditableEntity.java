/**
 * 
 */
package org.devgateway.toolkit.persistence.dao;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 
 * @author mpostelnicu
 *
 * @param <P>
 */
@MappedSuperclass
public abstract class AbstractChildAuditableEntity<P extends AbstractAuditableEntity> extends
		AbstractAuditableEntity {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderColumn(name = "index")
	@JoinColumn(name="parent_id", insertable=false, updatable=false)
	protected P parent;

	@Override
	public P getParent() {
		return parent;
	}

	public void setParent(P parent) {
		this.parent = parent;
	}

	
	


}
