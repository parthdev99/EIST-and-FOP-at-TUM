package de.tum.in.ase.fop;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.tum.in.ase.fop.ConnectionType.NORMAL;
import static de.tum.in.ase.fop.ConnectionType.WORMHOLE;

public final class Space {
	private final Map<Beacon, List<BeaconConnection>> connections;

	Map<Beacon, List<BeaconConnection>> getBeaconConnections() {
		return connections;
	}

	public Space(Map<Beacon, List<BeaconConnection>> connections) {
		this.connections = Collections.unmodifiableMap(connections);
	}

	public static void main(String[] args) {
		// An example for you to test your implementation
		HashMap<Beacon, List<BeaconConnection>> connections = new HashMap<>();
		Space space = new Space(connections);

		Beacon alpha = new Beacon(space, "Alpha");
		Beacon beta = new Beacon(space, "Beta");
		Beacon gamma = new Beacon(space, "Gamma");
		Beacon delta = new Beacon(space, "Delta");
		Beacon epsilon = new Beacon(space, "Epsilon");
		connections.put(alpha, List.of(new BeaconConnection(beta, NORMAL), new BeaconConnection(delta, NORMAL)));
		connections.put(beta, List.of(new BeaconConnection(beta, NORMAL), new BeaconConnection(gamma, WORMHOLE),
				new BeaconConnection(delta, NORMAL)));
		connections.put(gamma, List.of(new BeaconConnection(alpha, NORMAL), new BeaconConnection(gamma, NORMAL),
				new BeaconConnection(epsilon, NORMAL)));
		connections.put(delta, List.of(new BeaconConnection(beta, NORMAL), new BeaconConnection(gamma, WORMHOLE)));
		connections.put(epsilon, List.of(new BeaconConnection(beta, NORMAL), new BeaconConnection(gamma, WORMHOLE)));

		Spaceuin pingu = new Spaceuin(alpha, epsilon, new SimpleFlightRecorder());
		pingu.start();
		/*try{
			pingu.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
