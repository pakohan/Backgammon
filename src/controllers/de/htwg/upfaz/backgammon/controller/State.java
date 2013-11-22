package controllers.de.htwg.upfaz.backgammon.controller;

public abstract class State {

    public enum STATE {
        UNDEFINED,
        STATE_ROUND_STARTED
    }

    ;

    private STATE state;

    public State(final STATE stateRoundStarted) {
        this.state = stateRoundStarted;
    }

    public final STATE getState() {
        return state;
    }

    public final void setState(final STATE state) {
        this.state = state;
    }

    public abstract State click(int field);
}

