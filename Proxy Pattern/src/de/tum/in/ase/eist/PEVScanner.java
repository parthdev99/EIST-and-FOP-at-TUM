package de.tum.in.ase.eist;

import de.tum.in.ase.eist.pev.PEV;
import de.tum.in.ase.eist.search.SearchInterface;

import java.util.Set;
import java.util.stream.Collectors;

public class PEVScanner {
    private SearchInterface searchInterface;

    public PEVScanner(Set<PEV> availablePEVs) {
        // TODO: Task 2: Setup the search interface correctly
        //this.searchInterface=availablePEVs;

    }

    public Set<PEV> scanPEVs(Rider rider) {
        // TODO: Task 2: Use the SearchInterface instance to search PEVs that are available for the rider
        return Set.of();
    }
}
