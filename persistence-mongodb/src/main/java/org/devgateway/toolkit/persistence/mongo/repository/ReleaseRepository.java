package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReleaseRepository extends MongoRepository<Release, String> {

	
	@Query(value = "{ 'planning.budget.projectID' : ?0 }")
	Release findByBudgetProjectId(String projectId);

	
	@Query(value = "{ 'planning.bidNo' : ?0 }")
	Release findByPlanningBidNo(String bidNo);
	
		
	Release findByOcid(String ocid);
	
	@Override
	<S extends Release> S save(S entity);

}
