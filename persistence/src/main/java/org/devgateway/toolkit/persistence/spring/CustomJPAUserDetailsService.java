/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.spring;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link UserDetailsService} that uses JPA
 *
 * @author mpostelnicu, krams
 * see krams915.blogspot.fi/2012/01/spring-security-31-implement_3065.html
 */
@Component
public class CustomJPAUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonService personService;

    /**
     * Returns a populated {@link UserDetails} object. The username is first
     * retrieved from the database and then mapped to a {@link UserDetails}
     * object. We are currently using the {@link User} implementation from
     * Spring
     */
    @Override
    public Person loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            final Person domainUser = personService.findByUsername(username);
            final Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(domainUser);

            domainUser.setAuthorities(grantedAuthorities);
            return domainUser;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link GrantedAuthority}.
     *
     * @param domainUser
     * @return a {@link Set} containing the {@link GrantedAuthority}S
     */
    public static Set<GrantedAuthority> getGrantedAuthorities(final Person domainUser) {
        final Set<GrantedAuthority> grantedAuth = new HashSet<>();

        // get user authorities
        for (final Role authority : domainUser.getRoles()) {
            grantedAuth.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }

        return grantedAuth;
    }
}
