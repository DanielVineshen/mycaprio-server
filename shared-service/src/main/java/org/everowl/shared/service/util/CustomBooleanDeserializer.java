package org.everowl.shared.service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Custom JSON deserializer for boolean values.
 * This class extends JsonDeserializer to provide custom deserialization logic for Boolean objects.
 */
public class CustomBooleanDeserializer extends JsonDeserializer<Boolean> {
    /**
     * Deserializes a JSON representation of a boolean value into a Java Boolean object.
     * This method converts the JSON input to a String and then uses Boolean.valueOf() for the conversion.
     *
     * @param p   the JsonParser object used for reading the JSON input
     * @param ctx the DeserializationContext object that provides contextual information for deserialization
     * @return a Boolean object representing the deserialized boolean value
     * @throws IOException if an I/O error occurs during the deserialization process
     */
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String value = p.getText();
        return Boolean.valueOf(value);
    }
}