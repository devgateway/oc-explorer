/**
 * 
 */
package org.devgateway.ocds.persistence.mongo.repository;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Organization.OrganizationType;

/**
 * @author mihai
 *
 */
public interface OrganizationRepository extends GenericOrganizationRepository<Organization> {

	Organization findByIdAndTypes(String id, OrganizationType type);

}
