package de.tum.in.ase.fop;

import java.util.Random;

public class StandardLetterBag implements LetterBag {
	private final Random random;
	private int letterCount;

	public StandardLetterBag() {
		this.random = new Random();
		this.letterCount = 100;
	}

	public char getLetter() {
		if (letterCount <= 0) {
			throw new IllegalStateException("There are no more letters in the letter bag");
		}
		letterCount--;
		int index = random.nextInt(26);
		return (char) ('A' + index);
	}

	public int getLetterCount() {
		return letterCount;
	}
}
