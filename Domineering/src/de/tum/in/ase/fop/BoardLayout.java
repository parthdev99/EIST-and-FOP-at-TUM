package de.tum.in.ase.fop;

import java.util.ArrayList;

/*
    A BoardLayout object contains all the calculated board areas for either the vertical or the horizontal player. It
     is only used for storing these objects and values.
 */

public class BoardLayout {
    private final ArrayList<ProtectiveArea> protectiveAreas;
    private final ArrayList<SafeArea> safeAreas;
    private static final int FIX20 = 20;
    private static final int FIX50 = 50;
    private static final int FIX10 = 10;
    private static final int FIX15 = 15;

    private final ArrayList<VulnArea> vulnAreasOne;
    private final ArrayList<VulnArea> vulnAreasTwo;
    private final ArrayList<VulnArea> vulnAreasProtectedOne;
    private final ArrayList<VulnArea> vulnAreasProtectedTwo;
    private final ArrayList<OptionArea> optionAreas;

    // private final Player player;

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    private int lowerBound;
    private int upperBound;

    private int unavailableSquares;
    private int startAvailableSquares;
    private int unplayableSquares;

    public BoardLayout() {
        //this.player = player;
        this.protectiveAreas = new ArrayList<>(FIX20);
        this.safeAreas = new ArrayList<SafeArea>(FIX15);
        this.vulnAreasTwo = new ArrayList<VulnArea>(FIX50);
        this.vulnAreasOne = new ArrayList<VulnArea>(FIX15);
        this.vulnAreasProtectedTwo = new ArrayList<VulnArea>(FIX10);
        this.vulnAreasProtectedOne = new ArrayList<VulnArea>(FIX10);
        this.optionAreas = new ArrayList<>(FIX15);

        this.lowerBound = Integer.MIN_VALUE;
        this.upperBound = Integer.MIN_VALUE;
        this.unplayableSquares = Integer.MIN_VALUE;

        this.unavailableSquares = 0;
        this.startAvailableSquares = 0;
    }

    public int numProtectiveAreas() {
        return protectiveAreas.size();
    }

    public int numSafeAreas() {
        return safeAreas.size();
    }

    public int numVulnAreasOne() {
        return vulnAreasOne.size();
    }

    public int numVulnAreasTwo() {
        return vulnAreasTwo.size();
    }

    public int numVulnAreasProtectedOne() {
        return vulnAreasProtectedOne.size();
    }

    public int numVulnAreasProtectedTwo() {
        return vulnAreasProtectedTwo.size();
    }

    protected ArrayList<ProtectiveArea> getProtectiveAreas() {
        return protectiveAreas;
    }

    protected ArrayList<SafeArea> getSafeAreas() {
        return safeAreas;
    }

    protected ArrayList<VulnArea> getVulnAreasOne() {
        return vulnAreasOne;
    }

    protected ArrayList<VulnArea> getVulnAreasTwo() {
        return vulnAreasTwo;
    }

    protected ArrayList<VulnArea> getVulnAreasProtectedOne() {
        return vulnAreasProtectedOne;
    }

    protected ArrayList<VulnArea> getVulnAreasProtectedTwo() {
        return vulnAreasProtectedTwo;
    }

    protected ArrayList<OptionArea> getOptionAreas() {
        return optionAreas;
    }

    public int getUnavailableSquares() {
        return unavailableSquares;
    }

    public int getStartAvailableSquares() {
        return startAvailableSquares;
    }

    public int getUnplayableSquares() {
        return unplayableSquares;
    }

    public void setUnavailableSquares(int unavailableSquares) {
        this.unavailableSquares = unavailableSquares;
    }

    public void setStartAvailableSquares(int startAvailableSquares) {
        this.startAvailableSquares = startAvailableSquares;
    }

    public void setUnplayableSquares(int unplayableSquares) {
        this.unplayableSquares = unplayableSquares;
    }
}
