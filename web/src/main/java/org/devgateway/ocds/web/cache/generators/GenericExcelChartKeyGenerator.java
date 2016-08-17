package org.devgateway.ocds.web.cache.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author idobre
 * @since 8/17/16
 */
public class GenericExcelChartKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(GenericExcelChartKeyGenerator.class);

    final private ObjectMapper objectMapper;

    public GenericExcelChartKeyGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length != 3) {
            throw new RuntimeException(
                    "Wrong parameters received for generating custom GenericExcelChartKeyGenerator key!");
        }

        try {
            return new StringBuilder(method.toString())
                    .append(objectMapper.writeValueAsString(params[0]))
                    .append(objectMapper.writeValueAsString(params[1]))
                    .append(objectMapper.writeValueAsString(params[2]))
                    .toString().hashCode();
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
