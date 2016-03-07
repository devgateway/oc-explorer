package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReleaseRepository extends MongoRepository<Release, String> {

	//@Cacheable(value="releases", key="#projectId")
	@Query(value = "{ 'planning.budget.projectID' : ?0 }")
	public Release findByBudgetProjectId(String projectId);

	//@Cacheable(value="releases", key="#bidNo")
	@Query(value = "{ 'planning.bidNo' : ?0 }")
	public Release findByPlanningBidNo(String bidNo);
	
	
	//@Cacheable(value="releases", key="#bidNo")
	public Release findByOcid(String ocid);
}
