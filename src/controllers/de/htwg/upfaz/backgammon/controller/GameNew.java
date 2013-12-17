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
	private boolean endPhase = false;

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
					getCurrentPlayer()));
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
		if (gameMap.getField(firstClick).getStoneColor() != players.getColor()) {
			System.out
					.println("There are no your pieces");
			return false;
		}

		// check direction
		if (secondClick < 24
				&& (players.getColor() == Players.PLAYER_COLOR_WHITE
						&& secondClick <= firstClick || players.getColor() == Players.PLAYER_COLOR_BLACK
						&& secondClick >= firstClick)) {
			System.out.println("You're going in the wrong direction!");
			return false;
		}
		
		if (!dice.checkDistance(Math.abs(secondClick - firstClick))) {
			System.out.println("Can you count?");
			return false;
		}
		
		if (gameMap.getField(secondClick).isNotJumpable(players.getColor())) {
			System.out.println("There are some stones from another player");
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
		checkEndPhase();
		// perform the move

		if (gameMap.getField(secondClick).getNumberStones() == 1
				&& gameMap.getField(secondClick).getStoneColor() != players.getColor()) {

			gameMap.eatStone(firstClick, secondClick);

		}
		/*
		 * CHECK END PHASE. Probably no need in that 
		 * else if (endPhase && (secondClick > 25)) {
		 * 
		 * gameMap.takeOutStone(firstClick, secondClick);
		 * 
		 * }
		 */
		else {

			gameMap.moveStone(firstClick, secondClick);

		}

		renewJumps(firstClick, secondClick);
		firstClick = -1;
		notifyObservers();

		if (!dice.hasTurnsLeft()) {
			players.changeCurrentPlayer();
			dice = new Dice();
		}
	}

	private void checkEndPhase() {
		int stonesInEndPhase = 0;
		if (players.getColor() == Players.PLAYER_COLOR_WHITE) {
			for (int i = 18; i <= 23; i++) { // Fields 18-23 for white player
				// are endphase fields
				if (gameMap.getField(i).getStoneColor() == players.getColor()) {
					stonesInEndPhase += gameMap.getField(i).getNumberStones();
				}
			}
			stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_WHITE)
					.getNumberStones();
		} else {
			for (int i = 5; i >= 0; i--) { // Fields 0-5 for black player are
				// endphase fields
				if (gameMap.getField(i).getStoneColor() == players.getColor()) {
					stonesInEndPhase += gameMap.getField(i).getNumberStones();
				}
			}
			stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_BLACK)
					.getNumberStones();
		}

		endPhase = stonesInEndPhase == Constances.STONES_TO_WIN;
		if (endPhase) {
			setStatus("End Phase!");
		}
	}

	@Override
	public String toString() {
		return String.format("start = %d, target = %d; Current player: %s",
				firstClick, secondClick, getCurrentPlayer());
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

}
