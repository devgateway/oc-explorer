/**
 * 
 */
package org.devgateway.toolkit.persistence.dao;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author mpostelnicu
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes= {@Index(columnList="name")})
public class VietnamImportSourceFiles extends AbstractAuditableEntity implements Labelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	
	private String description;
	

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<FileMetadata> prototypeDatabaseFile;

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<FileMetadata> publicInstitutionsSuppliersFile;

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<FileMetadata> locationsFile;


	/* (non-Javadoc)
	 * @see org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity#getParent()
	 */
	@Override
	public AbstractAuditableEntity getParent() {
		// TODO Auto-generated method stub
		return null;
	}


	public Set<FileMetadata> getPrototypeDatabaseFile() {
		return prototypeDatabaseFile;
	}


	public void setPrototypeDatabaseFile(Set<FileMetadata> prototypeDatabaseFile) {
		this.prototypeDatabaseFile = prototypeDatabaseFile;
	}


	public Set<FileMetadata> getPublicInstitutionsSuppliersFile() {
		return publicInstitutionsSuppliersFile;
	}


	public void setPublicInstitutionsSuppliersFile(Set<FileMetadata> publicInstitutionsSuppliersFile) {
		this.publicInstitutionsSuppliersFile = publicInstitutionsSuppliersFile;
	}


	public Set<FileMetadata> getLocationsFile() {
		return locationsFile;
	}


	public void setLocationsFile(Set<FileMetadata> locationFile) {
		this.locationsFile = locationFile;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public void setLabel(String label) {
		this.name = label;
	}


	@Override
	public String getLabel() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	

}
