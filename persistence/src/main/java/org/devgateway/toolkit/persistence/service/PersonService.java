package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Person;

public interface PersonService extends BaseJpaService<Person> {
    Person findByUsername(String username);

    Person findByEmail(String email);
}
