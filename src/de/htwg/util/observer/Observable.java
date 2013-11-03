package de.htwg.util.observer;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Observable
        implements IObservable {

    private final Collection<IObserver> subscribers = new ArrayList<IObserver>(2);

    @Override
    public void addObserver(final IObserver s) {
        subscribers.add(s);
    }

    // --Commented out by Inspection START (02.11.13 18:14):
    //    @Override
    //    public void removeObserver(IObserver s) {
    //        subscribers.remove(s);
    //    }
    // --Commented out by Inspection STOP (02.11.13 18:14)

    // --Commented out by Inspection START (02.11.13 18:14):
    //    @Override
    //    public void removeAllObservers() {
    //        subscribers.removeAll(subscribers);
    //    }
    // --Commented out by Inspection STOP (02.11.13 18:14)

    @Override
    public void notifyObservers() {
        for (final IObserver observer : subscribers) {
            observer.update();
        }
    }
}
