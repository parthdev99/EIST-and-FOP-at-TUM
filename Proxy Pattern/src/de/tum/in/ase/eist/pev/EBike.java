package de.tum.in.ase.eist.pev;

public class EBike extends PEV {
    public EBike(int chargeLevel, String licensePlate) {
        super(chargeLevel, licensePlate);
        pricePerMinute = 3;
    }

    @Override
    public String toString() {
        return "E-Bike " + getLicensePlate() + " with charge level of " + getChargeLevel();
    }
}
