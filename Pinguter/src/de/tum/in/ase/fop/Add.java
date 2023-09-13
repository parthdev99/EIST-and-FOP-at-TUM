package de.tum.in.ase.fop;

public class Add {
	public static void main(String[] args) {
		System.out.println("Starting addition:");
		System.out.println("Please enter the first argument: ");
		int arg1 = InputReader.readInt();
		System.out.println("Please enter the second argument: ");
		int arg2 = InputReader.readInt();
		int sum = arg1 + arg2;
		System.out.println("The result is:" + sum);
		System.out.println("Finished addition");
	}
}
