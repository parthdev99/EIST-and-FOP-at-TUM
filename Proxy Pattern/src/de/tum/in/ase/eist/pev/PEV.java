package de.tum.in.ase.eist.pev;

import de.tum.in.ase.eist.Rental;
import de.tum.in.ase.eist.Rider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class PEV {
    private int chargeLevel;
    private final String licensePlate;
    protected int pricePerMinute;

    private final List<Rental> rentals;

    public PEV(int chargeLevel, String licensePlate) {
        this.chargeLevel = chargeLevel;
        this.licensePlate = licensePlate;
        rentals = new ArrayList<>();
    }

    @Override
    public abstract String toString();

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(int chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public int getPricePerMinute() {
        return pricePerMinute;
    }

    public void chargeUp() {
        if (chargeLevel < 100) {
            chargeLevel++;
        }
    }

    public Rental rent(LocalDateTime from, LocalDateTime to, Rider rider) {
        if (isBooked(from, to)) {
            throw new IllegalArgumentException("Already booked!");
        }
        Rental rental = new Rental(from, to, this, rider);
        rentals.add(rental);
        return rental;
    }

    public boolean isAvailable() {
        return !isBooked(LocalDateTime.now(), LocalDateTime.now());
    }

    public void ride() {
        System.out.println("Riding " + toString());
    }

    private boolean isBooked(LocalDateTime from, LocalDateTime to) {
        return rentals.stream().anyMatch(rental -> from.isBefore(rental.getTo()) && rental.getFrom().isBefore(to));
    }
}
