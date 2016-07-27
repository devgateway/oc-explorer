package org.devgateway.toolkit.forms.wicket.components.table;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

/**
 * Created by Octavian on 01.07.2016.
 */
public class JpaFilterState<T> implements Serializable {

    public Specification<T> getSpecification() {
        return (root, query, cb) -> cb.and();
    }
}
