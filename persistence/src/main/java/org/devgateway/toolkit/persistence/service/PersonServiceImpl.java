package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class PersonServiceImpl extends BaseJpaServiceImpl<Person> implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person findByUsername(final String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    public Person findByEmail(final String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    protected BaseJpaRepository<Person, Long> repository() {
        return personRepository;
    }

    @Override
    public Person newInstance() {
        return new Person();
    }

    @Override
    public TextSearchableRepository<Person, Long> textRepository() {
        return personRepository;
    }
}
