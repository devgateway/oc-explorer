package org.devgateway.toolkit.persistence.mongo.dao;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "organization")
public class VNOrganization extends Organization {
	@Indexed
	public Boolean procuringEntity;

	public Boolean getProcuringEntity() {
		return procuringEntity;
	}

	public void setProcuringEntity(Boolean procuringEntity) {
		this.procuringEntity = procuringEntity;
	}
}
