package org.devgateway.toolkit.persistence.mongo.dao;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author mihai Extension of {@link Organization} to allow extra
 *         Vietnam-specific fields
 */
@Document(collection = "organization")
public class VNOrganization extends Organization {
	@Indexed
	@JsonIgnore
	private Boolean procuringEntity;

	public Boolean getProcuringEntity() {
		return procuringEntity;
	}

	public void setProcuringEntity(final Boolean procuringEntity) {
		this.procuringEntity = procuringEntity;
	}
}
