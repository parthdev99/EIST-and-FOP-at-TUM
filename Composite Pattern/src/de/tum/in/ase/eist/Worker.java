package de.tum.in.ase.eist;

public class Worker extends Employee {
    // TODO 1: Implement the Worker class

    public Worker(String name) {
        super(name);
    }

    public void listHierarchy(int level) {
        printName(level);
    }
    // TODO 3: Implement listHierarchy() for Worker
}
