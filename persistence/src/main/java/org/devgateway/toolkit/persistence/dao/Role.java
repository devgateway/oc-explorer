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
package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends GenericPersistable implements Serializable, Comparable<Role>, Labelable {
    private static final long serialVersionUID = -6007958105920327142L;

    private String authority;

    @Column(name = "authority")
    public String getAuthority() {
        return authority;
    }

    public Role() {
    }

    public Role(final String authority) {
        this.authority = authority;
    }

    /**
     * @param authority the authority to set
     */
    public void setAuthority(final String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return authority;
    }

    @Override
    public int compareTo(final Role o) {
        return this.authority.compareTo(o.getAuthority());
    }

    @Override
    public void setLabel(final String label) {
        setAuthority(label);

    }

    @Override
    public String getLabel() {
        return getAuthority();
    }

}