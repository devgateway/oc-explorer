package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface Identifiable extends Serializable {

    @JsonIgnore
    Serializable getIdProperty();
}
