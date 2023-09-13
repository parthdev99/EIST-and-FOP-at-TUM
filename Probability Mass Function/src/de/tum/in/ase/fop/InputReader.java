package de.tum.in.ase.fop;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputReader {

    private static InputStream input = System.in;
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

    /**
     * Reads a {@link String} from the console, and prompts the user by printing the
     * given <code>text</code> with a line break to the console.
     *
     * @param text the text to display on the console before reading an input.
     * @return the input string or <code>null</code>, if no input is available
     * (should normally not happen)
     */
    public static String readString(String text) {
        String line;
        // Exchange the reader in case System.in has changed.
        // This is necessary for testing, as for every test input, System.in is changed.
        if (System.in != input) {
            input = System.in;
            bufferedReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        }
        try {
            System.out.println(text);
            line = bufferedReader.readLine();
            if (line == null) {
                throw new IllegalStateException("No input available");
            }
        } catch (IOException e) {
            // We "hide" the exception in the method signature by rethrowing an unchecked
            // exception
            throw new UncheckedIOException("Could not read the input", e);
        }

        // Trim all whitespaces so multi-word palindromes are accepted.
        return line.replaceAll("\\s+", "");
    }

    /**
     * Tries to read an <code>int</code> from the console, and retires if the input
     * was not a valid integer. It prompts the user by printing the given
     * <code>text</code> with a line break to the console.
     *
     * @see Integer#parseInt(String)
     */
    public static int readInt(String text) {
        Integer x = null;
        do {
            String s = readString(text);
            try {
                x = Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number. Integer is expected.");
            }
        } while (x == null);
        return x;
    }

    /**
     * Tries to read an <code>double</code> from the console, and retires if the input
     * was not a valid integer. It prompts the user by printing the given
     * <code>text</code> with a line break to the console.
     *
     * @see Double#parseDouble(String)
     */
    public static double readDouble(String text) {
        Double x = null;
        do {
            String s = readString(text);
            try {
                x = Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number. Double is expected.");
            }
        } while (x == null);
        return x;
    }
}
