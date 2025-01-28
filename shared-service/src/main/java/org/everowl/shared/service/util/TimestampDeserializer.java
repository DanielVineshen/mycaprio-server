package org.everowl.shared.service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Custom JSON deserializer for converting Unix timestamps to formatted date strings.
 * This class extends JsonDeserializer to provide custom deserialization logic
 * for timestamp values in JSON.
 */
public class TimestampDeserializer extends JsonDeserializer<String> {
    /**
     * DateTimeFormatter used to format the deserialized timestamp.
     * The format is set to "yyyy-MM-dd HH:mm:ss" in the system's default time zone.
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    /**
     * Deserializes a Unix timestamp (milliseconds since epoch) to a formatted date string.
     *
     * @param p    The JsonParser, providing access to the JSON content.
     * @param ctxt The deserialization context.
     * @return A formatted date string representing the timestamp.
     * @throws IOException If an I/O error occurs during deserialization.
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Parse the JSON value as a long (Unix timestamp in milliseconds)
        long timestamp = p.getLongValue();

        // Convert the timestamp to an Instant and format it as a string
        return FORMATTER.format(Instant.ofEpochMilli(timestamp));
    }
}