/**
 * 
 */
package org.devgateway.ocds.persistence.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author mpost
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class UserDashboard extends AbstractAuditableEntity implements Serializable, Labelable {

    private static final long serialVersionUID = 5758275706289173304L;
    private String name;
    private String formUrlEncodedBody;

    public String getFormUrlEncodedBody() {
        return formUrlEncodedBody;
    }

    public void setFormUrlEncodedBody(String formUrlEncodedBody) {
        this.formUrlEncodedBody = formUrlEncodedBody;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "defaultDashboard")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Person> defaultDashboardUsers = new HashSet<>();

    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserDashboard user;
    

    
    @Override
    public AbstractAuditableEntity getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormUrlEncoded() {
        return formUrlEncodedBody;
    }

    public void setFormUrlEncoded(String formUrlEncoded) {
        this.formUrlEncodedBody = formUrlEncoded;
    }

    public Set<Person> getDefaultDashboardUsers() {
        return defaultDashboardUsers;
    }

    public void setDefaultDashboardUsers(Set<Person> defaultDashboardUsers) {
        this.defaultDashboardUsers = defaultDashboardUsers;
    }

    @Override
    public void setLabel(String label) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getLabel() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public UserDashboard getUser() {
        return user;
    }

    public void setUser(UserDashboard user) {
        this.user = user;
    }

}
