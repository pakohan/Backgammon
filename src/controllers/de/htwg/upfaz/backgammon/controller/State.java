package controllers.de.htwg.upfaz.backgammon.controller;

public abstract class State {
	public enum STATE {UNDEFINED, STATE_ROUND_STARTED};

	private STATE state;
	
	public final STATE getState() {
		return state;
	}

	public final void setState(STATE state) {
		this.state = state;
	}
	
	public abstract State click(int field);
}

