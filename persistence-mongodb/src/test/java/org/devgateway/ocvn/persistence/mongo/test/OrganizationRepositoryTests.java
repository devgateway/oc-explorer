/**
 *
 */
package org.devgateway.ocvn.persistence.mongo.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.devgateway.ocds.persistence.mongo.Address;
import org.devgateway.ocds.persistence.mongo.ContactPoint;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Organization.OrganizationType;
import org.devgateway.ocds.persistence.mongo.repository.OrganizationRepository;
import org.devgateway.toolkit.persistence.mongo.spring.MongoPersistenceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mihai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MongoPersistenceApplication.class)
public class OrganizationRepositoryTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationRepositoryTests.class);

    @Autowired
    protected OrganizationRepository vnOrganizationRepository;

    @Before
    public void init() {
        vnOrganizationRepository.deleteAll();
    }

    @Test
    public void saveOrganization() {

        Organization o = new Organization();
        Address a = new Address();
        a.setCountryName("United States");
        a.setLocality("Washington");
        a.setRegion("DC");
        a.setPostalCode("20005");
        a.setStreetAddress("1110 Vermont Ave. NW, Suite 500");
        o.setAddress(a);

        ContactPoint cp = new ContactPoint();
        cp.setEmail("info@developmentgateway.org");
        cp.setName("John Doe");
        cp.setTelephone("555-1234567");
        cp.setUrl("http://developmentgateway.org");
        cp.setFaxNumber("555-7654321");
        o.setContactPoint(cp);

        Identifier i = new Identifier();
        i.setId("DG");
        i.setLegalName("Development Gateway");
        o.setIdentifier(i);

        o.getTypes().add(OrganizationType.procuringEntity);

        o.getAdditionalIdentifiers().add(i);

        Organization save = vnOrganizationRepository.save(o);

        assertThat(save.getId(), is(not(nullValue())));

        LOGGER.info(save.getId());

    }

}
