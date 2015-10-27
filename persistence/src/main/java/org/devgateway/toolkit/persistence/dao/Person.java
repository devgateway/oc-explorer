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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.dao.categories.Role;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class Person extends AbstractAuditableEntity implements Serializable, UserDetails{

    /**
     *
     */
    private static final long serialVersionUID = 109780377848343674L;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String country;
    private String title;
    //flag if user is first time logged
    private Boolean changePassword = true;
    private Boolean enabled= true;
    private String secret;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) @ManyToOne(fetch=FetchType.EAGER)
    private Group group;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Transient
    private String plainPassword;

    @Transient
    private String plainPasswordCheck;

    //flag if user want to change password
    @Transient
    private boolean changePass;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Role> roles;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * @return the authorities
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPlainPassword() {
        return plainPassword;
    }
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
    public String getPlainPasswordCheck() {
        return plainPasswordCheck;
    }
    public void setPlainPasswordCheck(String plainPasswordCheck) {
        this.plainPasswordCheck = plainPasswordCheck;
    }
    public boolean isChangePass() {
        return changePass;
    }
    public void setChangePass(boolean changePass) {
        this.changePass = changePass;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public Boolean getChangePassword() {
        return changePassword;
    }
    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "[" + username + "," + firstName + "," + lastName + "," + email + "]";
    }
	
	@Override
	public AbstractAuditableEntity getParent() {
		return null;
	}
}
