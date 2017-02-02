/**
 * 
 */
package org.devgateway.ocds.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author mpost
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = { @Index(name = "name_idx", columnList = "name", unique = true) })
public class UserDashboard extends AbstractAuditableEntity implements Serializable, Labelable {

    public static final String NAME_REGEXP = "^[A-Za-z0-9\\-]+$";
    public static final String NAME_REGEXP_MESSAGE =
            "Only alphanumeric characters allowed plus the minus sign, and no spaces.";

    private static final long serialVersionUID = 5758275706289173304L;

    @NotNull(message = "Cannot be Null")
    @Pattern(regexp = NAME_REGEXP,
            message = NAME_REGEXP_MESSAGE)
    private String name;
    @NotNull(message = "Cannot be Null")
    private String formUrlEncodedBody;

    public String getFormUrlEncodedBody() {
        return formUrlEncodedBody;
    }

    public void setFormUrlEncodedBody(String formUrlEncodedBody) {
        this.formUrlEncodedBody = formUrlEncodedBody;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultDashboard")
    @RestResource(exported = false)
    @JsonIgnore
    private Set<Person> defaultDashboardUsers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @RestResource(exported = false)
    @JsonIgnore
    private Set<Person> users = new HashSet<>();

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

    public Set<Person> getUsers() {
        return users;
    }

    public void setUsers(Set<Person> users) {
        this.users = users;
    }

}
