package de.tum.in.ase.eist;

public class PreviewHashing extends Hashing {
    private final int LIMIT = 1000;

    public PreviewHashing() {
        super(new SimpleHashAlgorithm());
    }

    public String hashDocument(String string) {
        try {
            if (string.length() > LIMIT) {
                throw new IllegalStateException("Hashing this file with preview hashing should not work!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return getImplementation().calculateHashCode(string);
    }
}
