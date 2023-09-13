package de.tum.in.ase.eist;

import java.time.LocalDateTime;

public class DriversLicense {

    private final LocalDateTime validUntil;
    private final String name;

    public DriversLicense(LocalDateTime validUntil, String name) {
        this.validUntil = validUntil;
        this.name = name;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public String getName() {
        return name;
    }
}
