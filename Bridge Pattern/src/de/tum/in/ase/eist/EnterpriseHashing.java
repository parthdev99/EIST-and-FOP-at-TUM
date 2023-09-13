package de.tum.in.ase.eist;

public class EnterpriseHashing extends Hashing {

    public EnterpriseHashing() {
        super(new CryptoSecureHashAlgorithm());
    }

    public String hashDocument(String string) {
        return getImplementation().calculateHashCode(string);
    }
}
