package de.tum.in.ase.eist;

public class Update {
    public PEV pev;
    boolean isAvailable;

    public Update(PEV pev) {
        this.pev = pev;
        this.isAvailable = pev.isAvailable();
    }

    public PEV getPev() {
        return pev;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
