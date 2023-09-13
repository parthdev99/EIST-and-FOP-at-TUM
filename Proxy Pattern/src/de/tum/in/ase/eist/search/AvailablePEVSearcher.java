package de.tum.in.ase.eist.search;

import de.tum.in.ase.eist.Rider;
import de.tum.in.ase.eist.pev.PEV;

import java.util.Set;
import java.util.stream.Collectors;

// TODO: Task 1: Use common search specification
public class AvailablePEVSearcher implements SearchInterface{
    private final Set<PEV> pevs;

    public AvailablePEVSearcher(Set<PEV> availablePEVs) {
        this.pevs = availablePEVs;
    }

    public Set<PEV> searchPEVs(Rider rider) {
        return pevs.stream().filter(pev -> pev.getChargeLevel() > 0 && pev.isAvailable()).collect(Collectors.toSet());
    }
}
