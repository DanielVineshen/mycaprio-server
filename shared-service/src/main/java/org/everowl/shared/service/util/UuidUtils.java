package org.everowl.shared.service.util;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * Utility class for generating and manipulating UUIDs.
 * This class provides methods to create base64-encoded UUIDs.
 */
public class UuidUtils {
    /**
     * Generates a base64-encoded UUID.
     * This method creates a random UUID, converts it to bytes, and then encodes it to a base64 string.
     * The resulting string is more compact than the standard UUID representation.
     *
     * @return The base64-encoded UUID as a {@code String}.
     */
    public static String generateBase64Uuid() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Create a ByteBuffer to hold the 16 bytes of the UUID
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);

        // Put the most and least significant bits into the ByteBuffer
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        // Encode the byte array to a base64 string
        return Base64.getEncoder().encodeToString(bb.array());
    }
}