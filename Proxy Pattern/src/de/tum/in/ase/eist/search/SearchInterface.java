package de.tum.in.ase.eist.search;

import de.tum.in.ase.eist.Rider;
import de.tum.in.ase.eist.pev.PEV;

import java.util.Set;

// TODO: Task 1: Define common search specification
public interface SearchInterface {
    public Set<PEV> searchPEVs(Rider rider);
}
