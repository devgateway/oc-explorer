package org.devgateway.toolkit.persistence.repository;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mpostelnicu
 *
 */
@Transactional
public interface PersonRepository extends JpaRepository<Person, Long> {

	@Query("select p from Person p where p.username = ?1")
	List<Person> findByName(String username);
	
	Person findByUsername(String username);
	
	Person findByEmail(String email);

}