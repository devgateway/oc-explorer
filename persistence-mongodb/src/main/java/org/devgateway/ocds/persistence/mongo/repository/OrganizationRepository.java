/**
 *
 */
package org.devgateway.ocds.persistence.mongo.repository;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Organization.OrganizationType;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author mpostelnicu
 *
 */
public interface OrganizationRepository extends GenericOrganizationRepository<Organization> {

    @Query(value = "{$and: [ { $or: [ {'_id' : ?0 }, " + "{'name': ?0 } ] }  , { 'types': ?1 } ]}")
    Organization findByIdOrNameAndTypes(String idName, OrganizationType type);

    @Query(value = "{ $or: [ {'_id' : ?0 }, " + "{'name': ?0} ] }")
    Organization findByIdOrName(String idName);

    @Query(value = "{$and: [  { $or: [ {'_id' : ?0 }, "
            + "{'additionalIdentifiers.identifier._id': ?0} ] }, { 'types': ?1 } }")
    Organization findByAllIdsAndType(String id, OrganizationType type);
}
