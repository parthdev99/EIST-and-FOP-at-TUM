package de.tum.in.ase.fop;

public class Mul {
	public static void main(String[] args) {
		System.out.println("Starting multiplication:");
		System.out.println("Please enter the first argument: ");
		int arg1 = InputReader.readInt();
		System.out.println("Please enter the second argument: ");
		int arg2 = InputReader.readInt();
		int mul = arg1 * arg2;
		System.out.println("The result is:" + mul);
		System.out.println("Finished multiplication");
	}
}
