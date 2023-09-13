package de.tum.in.ase.fop;

import java.io.*;

public class InputReader {

	private static InputStream input = System.in;
	private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

	/**
	 * Tries to read an <code>int</code> from the console, and retires if the input
	 * was not a valid integer.
	 *
	 * @see Integer#parseInt(String)
	 */
	public static int readInt() {
		Integer value = null;
		do {
			// Exchange the reader in case System.in has changed.
			// This is necessary for testing, as for every test input, System.in is changed.
			if (System.in != input) {
				input = System.in;
				bufferedReader = new BufferedReader(new InputStreamReader(input));
			}
			try {
				String line = bufferedReader.readLine();
				if (line == null) {
					throw new IllegalStateException("No input available");
				}
				value = Integer.parseInt(line.trim());
			} catch (IOException e) {
				// We "hide" the exception in the method signature by rethrowing an unchecked
				// exception
				throw new UncheckedIOException("Could not read the input", e);
			} catch (@SuppressWarnings("unused") NumberFormatException e) {
				// try again
			}
		} while (value == null);
		return value;
	}
}
