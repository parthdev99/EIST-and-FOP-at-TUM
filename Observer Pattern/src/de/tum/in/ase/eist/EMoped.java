package de.tum.in.ase.eist;

public class EMoped extends PEV {

    public EMoped(int chargeLevel, String licensePlate) {
        super(chargeLevel, licensePlate);
        pricePerMinute = 5;
    }

    @Override
    public String toString() {
        return "E-Moped " + getLicensePlate() + " with charge level of " + getChargeLevel();
    }
}
