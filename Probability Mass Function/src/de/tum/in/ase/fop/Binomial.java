package de.tum.in.ase.fop;

public class Binomial  {

    public static double pow(double base, int exponent) {
        // TODO: Calculate power function!
        if(exponent==0)
            return 1;

        return base*pow(base,exponent-1);
    }

    public static int bino(int n, int k) {
        // TODO: Calculate binomial coefficient.
        return 0;
    }

    public static double probabilityMassFunction(int n, int k, double p) {
        // TODO: Calculate probability mass function.
        return 0;
    }

    public static void main(String[] args) {

        // TODO: Implement your main method here.
        // Take care that you test different user inputs.
        System.out.println(pow(2,3));
    }
}
