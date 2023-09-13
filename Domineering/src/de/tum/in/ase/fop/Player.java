package de.tum.in.ase.fop;

public enum Player {
    HORIZONTAL, VERTICAL;

    public Player getOtherPlayer() {
        return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
    }
}
