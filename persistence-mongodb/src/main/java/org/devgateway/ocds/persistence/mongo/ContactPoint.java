/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

/**
 * @author mihai
 * ContactPoint OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#contactpoint
 */
public class ContactPoint {
	String name;
	String email;
	String telephone;
	String faxNumber;
	String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
