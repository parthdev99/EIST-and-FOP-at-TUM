package de.tum.in.ase.eist;

public class SimpleHashAlgorithm implements HashFunction {

    private static final int SIMPLE_HASH_MAXIMUM = 255;
    private static final int HEXADECIMAL = 16;

    private int maxLength;

    public SimpleHashAlgorithm() {
        maxLength = SIMPLE_HASH_MAXIMUM;
    }

    @Override
    public String calculateHashCode(String input) {
        int hash = 0;
        char[] charArray = input.toCharArray();
        for (char c : charArray) {
            hash += c;
        }
        return Integer.toString(hash % maxLength, HEXADECIMAL);
    }

}
