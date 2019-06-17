package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author idobre
 * @since 2019-03-22
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "test_form_id")})
public class TestFormChild extends AbstractAuditableEntity {
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TestForm testForm;

    private String header;

    private Integer value;

    public TestForm getTestForm() {
        return testForm;
    }

    public void setTestForm(final TestForm testForm) {
        this.testForm = testForm;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return testForm;
    }
}
