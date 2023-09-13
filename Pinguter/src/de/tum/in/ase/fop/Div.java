package de.tum.in.ase.fop;

public class Div {
	public static void main(String[] args) {
		System.out.println("Starting division:");
		System.out.println("Please enter the first argument: ");
		int arg1 = InputReader.readInt();
		System.out.println("Please enter the second argument: ");
		int arg2 = InputReader.readInt();
		int div = arg1 / arg2;
		System.out.println("The result is:" + div);
		System.out.println("Finished division");
	}
}
