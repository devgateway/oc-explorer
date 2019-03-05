package org.devgateway.toolkit.web.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-04
 * <p>
 * {@link KeyGenerator} that uses specific object properties to create a key.
 */
public class GenericKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(GenericKeyGenerator.class);

    private final ObjectMapper objectMapper;

    public GenericKeyGenerator(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        final StringBuilder key = new StringBuilder(target.getClass().getSimpleName());
        key.append(method.getName());

        for (final Object param : params) {
            if (param instanceof List<?>) {
                ((List<?>) param).stream()
                        .forEach(element -> key.append(createKey(element)));
            } else {
                key.append(createKey(param));
            }
        }

        return key.toString().hashCode();
    }

    private String createKey(final Object param) {
        if (param instanceof GenericPersistable) {
            final GenericPersistable persistable = (GenericPersistable) param;
            if (persistable.getId() != null) {
                return Long.toString(persistable.getId());
            } else {
                return persistable.toString();
            }
        } else {
            try {
                return objectMapper.writeValueAsString(param);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

