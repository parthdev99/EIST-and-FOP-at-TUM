package de.tum.in.ase.fop;

// @ToDo 3: Insert the right value in this class
public class Airport {

	public String name = "Stuttgart Airport";
	public Airplane airbus;
	public Airplane boeing;

	public Airport() {
		// @ToDo 1: Make sure the values of each plane are assigned properly
		this.boeing = new Airplane("B747", "Boeing", 747);
		this.airbus = new Airplane("A380", "Airbus", 380);
	}

	public void printAirportInformation() {
		System.out.println("Hello, you are at the " + this.name + ". Currently, the following airplanes are at this airport");
		airbus.printAirplaneInformation();
		boeing.printAirplaneInformation();
	}

}
