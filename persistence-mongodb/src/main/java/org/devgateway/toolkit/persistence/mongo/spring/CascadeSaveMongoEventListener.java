package org.devgateway.toolkit.persistence.mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.util.ReflectionUtils;

@Deprecated
public class CascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(Object source) {
        ReflectionUtils.doWithFields(source.getClass(),
                new CascadeCallback(source, mongoOperations));
    }
}

