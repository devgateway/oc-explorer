package org.devgateway.toolkit.persistence.mongo.repository;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReleaseRepository extends MongoRepository<Release, String> {

	@Query(value = "{ 'planning.budget.projectID' : ?0 }")
	public Release findByBudgetProjectId(String projectId);

}
