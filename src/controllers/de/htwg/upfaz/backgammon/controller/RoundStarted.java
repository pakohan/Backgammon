package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.controller.State;

public final class RoundStarted extends State {
	private int clickBefore;

	public RoundStarted(int lastClick) {
		this.clickBefore = lastClick;
		super.setState(STATE.STATE_ROUND_STARTED);
	}
	
	public final State click(int fieldClicked) {
		
		// three input parameters: current player; gamemap; click;
		// evaluation if click is valid
		return null;
	}
}