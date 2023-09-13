package de.tum.in.ase.fop;

public class Airplane {

	public String type;
	public String manufacturer;
	public int id;

	public Airplane(String type, String manufacturer, int id) {
		this.type = type;
		this.manufacturer = manufacturer;
		this.id = id;
	}

	public void printAirplaneInformation() {
		// @ToDo 2: Print out the right information with a linebreak
		System.out.println("This is a "+ this.manufacturer +" "+ this.type +" with the id "+ this.id);
	}
}