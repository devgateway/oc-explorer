/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai
 * Organization OCDS entity http://standard.open-contracting.org/latest/en/schema/reference/#organization
 */
@Document
public class Organization {

	@Id
	String id;

	Identifier identifier;
	List<Identifier> additionalIdentifiers = new ArrayList<>();
	String name;
	Address address;
	ContactPoint contactPoint;

	@Override
	public String toString() {
		return name + " with id=" + identifier.getId();
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
		this.id = identifier.getId();
	}

	public List<Identifier> getAdditionalIdentifiers() {
		return additionalIdentifiers;
	}

	public void setAdditionalIdentifiers(List<Identifier> additionalIdentifiers) {
		this.additionalIdentifiers = additionalIdentifiers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ContactPoint getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(ContactPoint contactPoint) {
		this.contactPoint = contactPoint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
