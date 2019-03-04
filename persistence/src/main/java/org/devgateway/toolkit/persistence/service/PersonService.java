package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class PersonService extends BaseJpaService<Person> {
    @Autowired
    private PersonRepository personRepository;

    public Person findByUsername(final String username) {
        return personRepository.findByUsername(username);
    }

    public Person findByEmail(final String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    protected BaseJpaRepository<Person, Long> repository() {
        return personRepository;
    }

    @Override
    public Optional<Person> newInstance() {
        return Optional.of(new Person());
    }
}
