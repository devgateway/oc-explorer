package org.devgateway.toolkit.persistence.repository;


import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.repository.category.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mpostelnicu
 *
 */
@Transactional
public interface GroupRepository extends CategoryRepository<Group>{

	public Group findByLabel(String label);
}
