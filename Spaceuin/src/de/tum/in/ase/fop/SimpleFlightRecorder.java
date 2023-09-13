package de.tum.in.ase.fop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class SimpleFlightRecorder implements FlightRecorder {

	private final List<Beacon> way;

	private static final int BOUND = 20;

	public SimpleFlightRecorder() {
		this(new ArrayList<>());
	}

	private SimpleFlightRecorder(List<Beacon> way) {
		this.way = Objects.requireNonNull(way);
	}

	@Override
	public void recordArrival(Beacon beacon) {
//		System.out.println(Thread.currentThread() + " arrived at " + beacon);
		randomDelay();
		way.add(beacon);
	}

	@Override
	public void recordDeparture(Beacon beacon) {
//		System.out.println(Thread.currentThread() + " left " + beacon);
		randomDelay();
	}

	@Override
	public FlightRecorder createCopy() {
		return new SimpleFlightRecorder(new ArrayList<>(way));
	}

	@Override
	public void tellStory() {
		for (Beacon beacon : way) {
			System.out.println("I was at beacon " + beacon);
		}
	}

	private static void randomDelay() {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextLong(2, BOUND));
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
