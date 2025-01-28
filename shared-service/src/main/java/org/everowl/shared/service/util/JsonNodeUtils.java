package org.everowl.shared.service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Utility class for JSON operations, providing methods to convert various object types to JsonNode.
 * This class uses Jackson's ObjectMapper for all conversions.
 */
public class JsonNodeUtils {
    /**
     * Converts a single object to a JsonNode.
     *
     * @param object the object to convert
     * @return the JsonNode representation of the object
     */
    public static JsonNode convertObjectToJsonNode(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(object);
    }

    /**
     * Converts an array of objects to a JsonNode.
     *
     * @param objects The array of objects to be converted
     * @return The JsonNode representation of the array of objects
     */
    public static JsonNode convertArrayToJsonNode(Object[] objects) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(objects);
    }

    /**
     * Converts a List of objects to a JsonNode.
     *
     * @param objects the List of objects to be converted
     * @return the JsonNode representing the converted List of objects
     */
    public static JsonNode convertListToJsonNode(List<Object> objects) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(objects);
    }
}