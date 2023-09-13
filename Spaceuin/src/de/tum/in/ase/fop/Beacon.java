package de.tum.in.ase.fop;

import java.util.List;
import java.util.Objects;

public final class Beacon {
    private final Space space;
    private final String name;

    public Beacon(Space space, String name) {
        this.space = Objects.requireNonNull(space, "space");
        this.name = Objects.requireNonNull(name, "name");
    }

    /**
     * Name of the Beacon, never null.
     */
    public String name() {
        return name;
    }

    /**
     * List of Connections to other Beacons, never null
     */
    public List<BeaconConnection> connections() {
        return Objects.requireNonNullElse(space.getBeaconConnections().get(this), List.of());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Beacon)) {
            return false;
        }
        return Objects.equals(name, ((Beacon) obj).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
