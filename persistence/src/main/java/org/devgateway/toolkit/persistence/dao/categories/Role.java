package org.devgateway.toolkit.persistence.dao.categories;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;


/**
 * 
 * @author mpostelnicu
 *
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends GenericPersistable implements Serializable, Comparable<Role>, Labelable {
	 
 	/**
 	 * 
 	 */
 	private static final long serialVersionUID = -6007958105920327142L;
	private String authority;
	@Column(name = "authority")
	public String getAuthority() {
		return authority;
	}
 	public Role(){
 	}
 
	public Role(String authority){
		this.authority = authority;
	}
	
	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
 	}
 	
 	@Override
	public String toString() {
		return authority;
 	}
 
	@Override
	public int compareTo(Role o) {
		return this.authority.compareTo(o.getAuthority());
 	}
	/* (non-Javadoc)
	 * @see org.devgateway.ccrs.persistence.dao.Labelable#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		setAuthority(label);
		
	}
	/* (non-Javadoc)
	 * @see org.devgateway.ccrs.persistence.dao.Labelable#getLabel()
	 */
	@Override
	public String getLabel() {
		return getAuthority();
	}

}