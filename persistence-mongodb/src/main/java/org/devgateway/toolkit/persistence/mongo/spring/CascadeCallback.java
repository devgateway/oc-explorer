package org.devgateway.toolkit.persistence.mongo.spring;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

@Deprecated
public class CascadeCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private MongoOperations mongoOperations;

    public CascadeCallback(final Object source, final MongoOperations mongoOperations) {
        this.source = source;
        this.setMongoOperations(mongoOperations);
    }

    public void saveValue(Object fieldValue) {
        final FieldCallback callback = new FieldCallback();

        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);

        getMongoOperations().save(fieldValue);
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(getSource());
            if (fieldValue != null) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    Collection<?> c = (Collection<?>) fieldValue;
                    for (Object o : c) {
                        saveValue(o);
                    }
                } else {
                    saveValue(fieldValue);
                }
            }
        }

    }

    public Object getSource() {
        return source;
    }

    public void setSource(final Object source) {
        this.source = source;
    }

    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }

    public void setMongoOperations(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
}
