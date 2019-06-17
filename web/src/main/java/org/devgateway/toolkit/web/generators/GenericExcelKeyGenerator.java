package org.devgateway.toolkit.web.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author idobre
 * @since 16/11/2017
 * <p>
 * {@link KeyGenerator} that uses some parameters to create a key.
 * This KeyGenerator is used for Excel File Generator.
 */
public class GenericExcelKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(GenericExcelKeyGenerator.class);

    private final ObjectMapper objectMapper;

    public GenericExcelKeyGenerator(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        if (params.length < 2) {
            throw new RuntimeException(
                    "Wrong parameters received for generating custom GenericExcelKeyGenerator key!");
        }

        try {
            StringBuilder key = new StringBuilder(method.toString());
            key.append(params[0].getClass().getGenericInterfaces()[0].getTypeName()); // add the BaseJpaRepository class
            key.append(params[1].getClass().getSimpleName());               // add Specification class

            for (int i = 1; i < params.length; i++) {
                key.append(objectMapper.writeValueAsString(params[i]));
            }

            return key.toString().hashCode();
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
