package de.tum.in.ase.eist;

public abstract class Hashing {
    private HashFunction implementation;

    public Hashing(HashFunction hashFunction) {
        this.implementation = hashFunction;
    }

    public abstract String hashDocument(String string);

    public HashFunction getImplementation() {
        return implementation;
    }
}
