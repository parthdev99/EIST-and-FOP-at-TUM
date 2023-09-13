package de.tum.in.ase.fop;

public class Sub {
	public static void main(String[] args) {
		System.out.println("Starting subtraction:");
		System.out.println("Please enter the first argument: ");
		int arg1 = InputReader.readInt();
		System.out.println("Please enter the second argument: ");
		int arg2 = InputReader.readInt();
		int sub = arg1 - arg2;
		System.out.println("The result is:" + sub);
		System.out.println("Finished subtraction");
	}
}
