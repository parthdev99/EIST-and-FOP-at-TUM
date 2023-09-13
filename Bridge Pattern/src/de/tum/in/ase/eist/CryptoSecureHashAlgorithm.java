package de.tum.in.ase.eist;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoSecureHashAlgorithm implements HashFunction {

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private static final int MAX_BYTE_SIZE = 0xFF;
    private static final int SHIFT_AMOUNT = 4;
    private static final int HALF_BYTE_NUMBER = 0x0F;

    @Override
    public String calculateHashCode(String input) {
        try {
            byte[] hashedBytes = MessageDigest.getInstance("SHA-512").digest(input.getBytes(StandardCharsets.UTF_8));

            // https://stackoverflow.com/a/9855338/5608927
            byte[] hexChars = new byte[hashedBytes.length * 2];
            for (int index = 0; index < hashedBytes.length; index++) {
                int nextValue = hashedBytes[index] & MAX_BYTE_SIZE;
                hexChars[index * 2] = HEX_ARRAY[nextValue >>> SHIFT_AMOUNT];
                hexChars[index * 2 + 1] = HEX_ARRAY[nextValue & HALF_BYTE_NUMBER];
            }
            return new String(hexChars, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
