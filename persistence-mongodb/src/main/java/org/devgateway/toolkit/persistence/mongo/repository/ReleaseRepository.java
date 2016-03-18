package org.devgateway.toolkit.persistence.mongo.repository;

import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReleaseRepository extends MongoRepository<Release, String> {

	
	@Query(value = "{ 'planning.budget.projectID' : ?0 }")
	public Release findByBudgetProjectId(String projectId);

	
	@Query(value = "{ 'planning.bidNo' : ?0 }")
	public Release findByPlanningBidNo(String bidNo);
	
		
	public Release findByOcid(String ocid);
	
	@Override
	public <S extends Release> List<S> save(Iterable<S> entites);	
	
	@Override
	public <S extends Release> S save(S entity);

}
