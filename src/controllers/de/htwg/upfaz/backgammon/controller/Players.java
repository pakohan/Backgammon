package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.entities.Player;

public final class Players implements IPlayer {

	public static final int PLAYER_COLOR_WHITE = 0;
	public static final int PLAYER_COLOR_BLACK = 1;
	private final IPlayer[] players;
	private int currentPlayer;

	public Players() {
		players = new IPlayer[2];
		players[0] = new Player(PLAYER_COLOR_WHITE);
		players[1] = new Player(PLAYER_COLOR_BLACK);
		currentPlayer = PLAYER_COLOR_BLACK;
	}

	public IPlayer getCurrentPlayer() {
		return players[currentPlayer];
	}

	public void changeCurrentPlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 0;
		} else {
			currentPlayer = 1;
		}
	}

	public int getColor() {
		return players[currentPlayer].getColor();
	}

	@Override
	public String toString() {
		return players[currentPlayer].toString();
	}

	public void setCurrentPlayer(final String s) {
		if (players[0].toString().equals(s)) {
			currentPlayer = 0;
		} else {
			currentPlayer = 1;
		}
	}
}
