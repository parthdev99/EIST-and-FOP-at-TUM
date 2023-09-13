package de.tum.in.ase.eist;

public class EKickscooter extends PEV {

    public EKickscooter(int chargeLevel, String licensePlate) {
        super(chargeLevel, licensePlate);
        pricePerMinute = 2;
    }

    @Override
    public String toString() {
        return "E-Kickscooter " + getLicensePlate() + " with charge level of " + getChargeLevel();
    }
}
