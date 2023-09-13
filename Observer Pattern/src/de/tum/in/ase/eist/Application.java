package de.tum.in.ase.eist;

import java.time.LocalDateTime;

public class Application {

    /**
     * Example
     */
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();

        Rider rider1 = new Rider("A", 19, true, new DriversLicense(now.plusYears(5), "A"));
        Rider rider2 = new Rider("B", 22, true, null);

        PEV pev = new EBike(100, "XYZ42");

        Rental rental1 = pev.rent(now, now.plusSeconds(20), rider1);
        Rental rental2 = pev.rent(now.plusSeconds(30), now.plusSeconds(60), rider2);

        // rider2 subscribes for updates on PEV XYZ42
//      pev.subscribe(rider2); // TODO implement observer pattern

        rental1.start();
        // time passes...
        rental1.stop();

        // rider2 knows they can use PEV XYZ42
//      pev.unsubscribe(rider2); // TODO implement observer pattern
        rental2.start();
    }
}
