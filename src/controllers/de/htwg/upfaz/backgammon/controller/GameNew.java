package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.util.observer.IObservable;
import controllers.de.htwg.util.observer.Observable;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;

public class GameNew extends Observable implements IObservable, IGame {

	private Players players;
	private GameMap gameMap;
	private Dice dice;
	private String status = "Let's the game begin!";

	private int firstClick = -1;
	private int secondClick = -1;
	private int winner = -1;

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
			secondClick = field;
			// try move also performs the move
			System.out.println("Making move from " + firstClick + " to "
					+ secondClick);
			returnVal = tryMove();
			firstClick = -1;
			secondClick = -1;
		}
		
		// check for winner
		if (gameMap.checkForWinner()) {
			setStatus(String.format(Constances.PLAYER_S_IS_THE_WINNER, 
					players.getCurrentPlayer()));
			setWinner(players.getColor() + 1);
			return true;
		}

		
		// better call this too often than not enough times
		notifyObservers();
		return returnVal;
	}

	private boolean tryMove() {
		// ask all instances like the map or the players class, if the move is
		// possible
		boolean returnVal = checkIfmovePossible();

		// if so, perform the move
		if (returnVal) {
			makeMove();
		}

		return returnVal;
	}

	private boolean checkIfmovePossible() {
		boolean toReturn = true;
		// do checks if this move is possible
		if (gameMap.getField(firstClick).getStoneColor() != players
				.getColor()) {
			System.out.println("You can't move this piece or there are no pieces");
			return false;
		}
		
		
		return toReturn;
	}

	private void renewJumps(final int start, final int target) {

		if (target >= GameMap.FIELD_END_BLACK) {
			dice.renewJumpsEndPhase(start);
		} else {
			dice.move(Math.abs(start - target));
		}
	}

	private void makeMove() {
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
	
	@Override
	public String toString() {
		return String
				.format("start = %d, target = %d; Current player: %s", 
						firstClick, secondClick, getCurrentPlayer());
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}
	
	
	
}
