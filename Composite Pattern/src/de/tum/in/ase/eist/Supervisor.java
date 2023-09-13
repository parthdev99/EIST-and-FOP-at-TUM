package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

public class Supervisor extends Employee {

    // TODO 2: Implement the Supervisor class
    private List<Employee> supervisedEmployees;

    public Supervisor(String name) {
        super(name);
        this.supervisedEmployees = new ArrayList<>();
    }

    public List<Employee> getSupervisedEmployees() {
        return supervisedEmployees;
    }

    public void hireEmployee(Employee employee) {
        supervisedEmployees.add(employee);

    }

    public void fireEmployee(Employee employee) {
        supervisedEmployees.remove(employee);
    }

    public void listHierarchy(int level) {
        printName(level);
        for (int i = 0; i < supervisedEmployees.size(); i++) {
            supervisedEmployees.get(i).listHierarchy(level + 1);
        }
    }

    // TODO 3: Implement listHierarchy() for Supervisor
}
