package de.tum.in.ase.eist;

import java.time.LocalDateTime;

public record DriversLicense(LocalDateTime validUntil, String name) {}
