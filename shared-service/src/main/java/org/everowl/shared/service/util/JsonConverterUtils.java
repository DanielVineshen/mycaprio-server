package org.everowl.shared.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.everowl.shared.service.exception.RunTimeException;

import static org.everowl.shared.service.enums.ErrorCode.JSON_CONVERSION_EXCEPTION;
import static org.everowl.shared.service.enums.ErrorCode.JSON_PROCESSING_EXCEPTION;

@Slf4j
public class JsonConverterUtils {
    public static String convertObjectToJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            throw new RunTimeException(JSON_CONVERSION_EXCEPTION);
        }
    }

    public <T> T convertJsonToGenericType(String jsonString, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            throw new RunTimeException(JSON_PROCESSING_EXCEPTION);
        }
    }
}