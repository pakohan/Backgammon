package controllers.de.htwg.util.observer;

public interface IObservable {

    void addObserver(IObserver s);

    // --Commented out by Inspection (02.11.13 18:14):public abstract void removeObserver(IObserver s);

    // --Commented out by Inspection (02.11.13 18:14):public abstract void removeAllObservers();

    void notifyObservers();
}
