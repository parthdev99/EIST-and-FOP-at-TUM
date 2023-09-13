package de.tum.in.ase.fop;

public class Mod {
	public static void main(String[] args) {
		System.out.println("Starting modulo:");
		System.out.println("Please enter the first argument: ");
		int arg1 = InputReader.readInt();
		System.out.println("Please enter the second argument: ");
		int arg2 = InputReader.readInt();
		int mod = arg1 % arg2;
		System.out.println("The result is:" + mod);
		System.out.println("Finished modulo-operation");
	}
}
