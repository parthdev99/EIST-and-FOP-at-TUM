package de.tum.in.ase.fop;

import java.util.Objects;

public final class BeaconConnection {
    private final Beacon beacon;
    private final ConnectionType type;

    public BeaconConnection(Beacon beacon, ConnectionType type) {
        this.beacon = Objects.requireNonNull(beacon, "beacon");
        this.type = Objects.requireNonNull(type, "type");
    }

    public Beacon beacon() {
        return beacon;
    }

    public ConnectionType type() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beacon, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BeaconConnection)) {
            return false;
        }
        BeaconConnection other = (BeaconConnection) obj;
        return Objects.equals(beacon, other.beacon) && type == other.type;
    }

    @Override
    public String toString() {
        return "BeaconConnection to " + beacon + "(" + type + ")";
    }
}
