package de.tum.in.ase.eist;

import java.util.*;

public abstract class Subject<T> {
    // TODO realize observer pattern
    private Set<Observer<T>> observers =new HashSet<>();

    public void subscribe(Observer<T> observer) {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }

    public void unsubscribe (Observer<T> observer) {
        Objects.requireNonNull(observer);
        observers.remove(observer);
    }

    public abstract T getUpdate();

    public void notifyObservers() {
        T newState=getUpdate();
        for(Observer<T> ob : observers) {
            ob.update(newState);
        }
    }
}
