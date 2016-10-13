/**
 * 
 */
package org.devgateway.ocds.persistence.dao;

import java.io.Serializable;

import javax.persistence.Entity;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
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
public class UserDashboard extends AbstractAuditableEntity implements Serializable {

    private static final long serialVersionUID = 5758275706289173304L;
    private String name;
    private String formUrlEncodedBody;

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

}
