package de.tum.in.ase.fop;

import java.util.*;

public class Spaceuin extends Thread {

    private Beacon start;
    private Beacon to;
    private Set<Beacon> visited;

    // For stopping all Threads at the end
    private Spaceuin parentThread;
    private List<Spaceuin> childThreads;

    // For telling the way afterward
    private FlightRecorder flightRecorder;

    // Standard constructor for first Pingu
    public Spaceuin(Beacon start, Beacon to, FlightRecorder flightRecorder) {
        this(start, to, flightRecorder, null);
    }

    // Constructor for every other Pingu
    public Spaceuin(Beacon start, Beacon to, FlightRecorder flightRecorder, Spaceuin parentThread) {
        this.start = start;
        this.to = to;
        this.flightRecorder = flightRecorder;
        visited = new HashSet<>();

        this.parentThread = parentThread;
        childThreads = new ArrayList<>();

    }

    /*@Override
    public void run() {
        // TODO: implement actual logic only here
        letsWork(start);
    }

    public void letsWork(Beacon start) {
        if (!visited.contains(start)) {
            visited.add(start);
            flightRecorder.recordArrival(start);
            synchronized (this) {
                if (start.name() == to.name()) {
                    if (isInterrupted()) {
                        return;
                    }
                    markArrival(this);
                    System.out.println(Thread.currentThread().getName());
                    flightRecorder.tellStory();
                    return;
                }
            }
            List<BeaconConnection> myConn = start.connections();
            for (int i = 0; i < myConn.size(); i++) {
                if (!visited.contains(myConn.get(i).beacon())) {
                    if (myConn.get(i).type().equals(ConnectionType.WORMHOLE)) {
                        visited.add(myConn.get(i).beacon());
                        //flightRecorder.recordArrival(myConn.get(i).beacon());
                        synchronized (this) {
                            if (start.name() == to.name()) {
                                if (isInterrupted()) {
                                    return;
                                }
                                markArrival(this);
                                flightRecorder.tellStory();
                                return;
                            }


                            createNew(myConn.get(i).beacon());
                        }
                    } else {
                        flightRecorder.recordDeparture(start);
                        letsWork(myConn.get(i).beacon());
                        flightRecorder.recordArrival(start);
                    }
                }
            }
        }
        return;

    }

    public void createNew(Beacon start) {
        Spaceuin another = new Spaceuin(start, to, flightRecorder.createCopy(), this);
        childThreads.add(another);
        another.start();
    }
    private synchronized void markArrival(Spaceuin toCheck) {
        if (parentThread != null)
            if (parentThread != toCheck)
                parentThread.markArrival(this);
        for (int i = 0; i < childThreads.size(); i++) {
            if (toCheck != childThreads.get(i))
                childThreads.get(i).markArrival(this);
        }
        interrupt();
    }*/
    public void run() {

        explore(start);
    }

    private void explore(Beacon next) {
        List<BeaconConnection> currConnections = new ArrayList<>();
        BeaconConnection bc = null;
        Beacon helper = null;
        Beacon after = null;
        synchronized (next) {
            if (!visited.contains(next)) {
                if (isInterrupted()) {
                    return;
                }

                visited.add(next);
                flightRecorder.recordArrival(next);
                if (next == to) {
                    markArrival(this);
                    flightRecorder.tellStory();
                    return;
                }
                currConnections = next.connections();

                for (int i = 0; i < currConnections.size(); i++) {
                    bc = currConnections.get(i);
                    helper = bc.beacon();
                    // creating new space
                    if (!visited.contains(helper) && bc.type() == ConnectionType.WORMHOLE) {
                        synchronized (this) {
                            if (isInterrupted()) {
                                return;
                            }
                            Spaceuin newSpace = new Spaceuin(helper, to, flightRecorder.createCopy(), this);
                            childThreads.add(newSpace);
                            newSpace.start();
                        }
                    }
                }

                after = getBeacon(next, currConnections, after);
            }
        }
        while (after != null) {
            explore(after);
            after = null;
            synchronized (next) {
                if (isInterrupted()) {
                    return;
                }
                flightRecorder.recordArrival(next);
                after = getBeacon(next, currConnections, after);
            }
        }


    }

    private Beacon getBeacon(Beacon next, List<BeaconConnection> currConnections, Beacon after) {
        BeaconConnection bc;
        Beacon helper;
        for (int i = 0; i < currConnections.size(); i++) {
            bc = currConnections.get(i);
            helper = bc.beacon();
            if (!visited.contains(helper) && bc.type() == ConnectionType.NORMAL) {
                after = helper;
                break;
            }
        }
        flightRecorder.recordDeparture(next);
        return after;
    }

    private synchronized void markArrival(Spaceuin toCkeck) {
        if (parentThread != null && parentThread != toCkeck) {
            parentThread.markArrival(this);
        }
        for (int i = 0; i < childThreads.size(); i++) {
            if (toCkeck != childThreads.get(i)) {
                childThreads.get(i).markArrival(this);
            }
        }
        interrupt();
    }
}
	/*Stack<Beacon> myStack=new Stack<>();
		myStack.push(start);

		while(!myStack.empty()) {
			Beacon current= myStack.pop();
			if(!visited.contains(current)) {
				visited.add(current);
				flightRecorder.recordArrival(current);
				if(current.name()==to.name()) {
					myStack.empty();
					flightRecorder.tellStory();
                   return;
				}
				List<BeaconConnection> myConn=current.connections();
				for(int i=0;i<myConn.size();i++)
				{
					if(!visited.contains(myConn.get(i).beacon())) {
						if(myConn.get(i).type().equals(ConnectionType.WORMHOLE)) {
							visited.add(myConn.get(i).beacon());
							flightRecorder.recordArrival(myConn.get(i).beacon());
							if(current.name()==to.name()) {
								myStack.empty();
								flightRecorder.tellStory();
                              return;
							}

							createNew(myConn.get(i).beacon());
						}
						else {

							myStack.push(myConn.get(i).beacon());
						}
					}
				}

			}
			flightRecorder.recordDeparture(current);
		}*/

