package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.util.observer.IObservable;
import controllers.de.htwg.util.observer.Observable;

public class GameNew extends Observable implements IObservable, IGame {

	private Players players;
	private GameMap gameMap;
	private Dice dice;
	private String status = "Let's the game begin!";

	private int firstClick = -1;

	public GameNew() {
		players = new Players(this);
		gameMap = new GameMap(this, players);

		players.changeCurrentPlayer();
		dice = new Dice();
		notifyObservers();
	}

	@Override
	public void setStatus(final String s) {
		status = s;
		notifyObservers();
	}

	@Override
	public void setDice(Dice dice) {
		this.dice = dice;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public String getStatus() {
		return status;
	}

	public IPlayer getCurrentPlayer() {
		return players.getCurrentPlayer();
	}

	public int getDiceAt(final int i) {
		return dice.getDiceAt(i);
	}

	public int getDiceToDraw(final int i) {
		return dice.getDiceToDraw(i);
	}

	public boolean click(int field) {
		boolean returnVal = true;

		if (firstClick == -1) {
			System.out.println("Clicked first field: " + field);
			firstClick = field;
		} else {
			// try move also performs the move
			System.out.println("Making move from " + firstClick + " to "
					+ field);
			returnVal = tryMove(firstClick, field);
			firstClick = -1;
		}

		// better call this too often than not enough times
		notifyObservers();
		return returnVal;
	}

	private boolean tryMove(int firstClick, int secondClick) {
		// ask all instances like the map or the players class, if the move is
		// possible
		boolean returnVal = checkIfmovePossible(firstClick, secondClick);

		// if so, perform the move
		if (returnVal) {
			makeMove(firstClick, secondClick);
		}

		return returnVal;
	}

	private boolean checkIfmovePossible(int firstClick, int secondClick) {
		boolean toReturn = true;
		// do checks if this move is possible

		return toReturn;
	}

	private void renewJumps(final int start, final int target) {

		if (target >= GameMap.FIELD_END_BLACK) {
			dice.renewJumpsEndPhase(start);
		} else {
			dice.move(Math.abs(start - target));
		}
	}

	private void makeMove(int firstClick, int secondClick) {
		// perform the move

		gameMap.moveStone(firstClick, secondClick);
		renewJumps(firstClick, secondClick);
		firstClick = -1;
		notifyObservers();
		
		
		if(!dice.hasTurnsLeft()){
			players.changeCurrentPlayer();
			dice = new Dice();
		}
	}
}
