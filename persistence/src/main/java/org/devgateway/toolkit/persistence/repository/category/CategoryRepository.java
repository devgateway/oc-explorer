package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mpostelnicu
 *
 * @param <T>
 */
@Transactional
public interface CategoryRepository<T extends Category> extends TextSearchableRepository<T, Long> {

	@Override
	@Query("select cat from  #{#entityName} cat where lower(cat.label) like %?1%")
	Page<T> searchText(String code, Pageable page);
}
