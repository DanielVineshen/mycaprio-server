package org.everowl.shared.service.util;

import java.math.BigInteger;

public class Base62 {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    public static String encode(byte[] input) {
        BigInteger bi = new BigInteger(1, input);
        StringBuilder sb = new StringBuilder();
        while (bi.compareTo(BigInteger.ZERO) > 0) {
            int remainder = bi.mod(BigInteger.valueOf(BASE)).intValue();
            sb.insert(0, ALPHABET.charAt(remainder));
            bi = bi.divide(BigInteger.valueOf(BASE));
        }
        // Pad with leading zeros if necessary
        while (sb.length() < input.length) {
            sb.insert(0, ALPHABET.charAt(0));
        }
        return sb.toString();
    }

    public static byte[] decode(String input) {
        BigInteger bi = BigInteger.ZERO;
        for (char c : input.toCharArray()) {
            bi = bi.multiply(BigInteger.valueOf(BASE));
            bi = bi.add(BigInteger.valueOf(ALPHABET.indexOf(c)));
        }
        byte[] bytes = bi.toByteArray();
        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }
        return bytes;
    }
}