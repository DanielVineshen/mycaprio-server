package org.everowl.core.service.service.shared;

import org.everowl.shared.service.util.Base62;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Component
public class EncryptionService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 6;
    private static final int GCM_TAG_LENGTH = 12;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${encryption.key}")
    private String secretKey;

    public String encryptCompact(String data) throws Exception {
        // Generate IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        RANDOM.nextBytes(iv);

        // Initialize cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = getKey();
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        // Encrypt
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Combine IV and encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        // Convert to Base62 (more compact than Base64)
        return Base62.encode(combined);
    }

    public String decryptCompact(String encryptedData) throws Exception {
        // Decode from Base62 to bytes
        byte[] combined = Base62.decode(encryptedData);

        // Split IV and encrypted data
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(combined, GCM_IV_LENGTH, encrypted, 0, encrypted.length);

        // Initialize cipher for decryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = getKey();
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        // Decrypt
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    private SecretKey getKey() {
        // Convert hex string to byte array
        byte[] keyBytes = Hex.decode(secretKey);
        return new SecretKeySpec(keyBytes, "AES");
    }
}