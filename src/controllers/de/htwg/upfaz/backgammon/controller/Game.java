/*
package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;
import controllers.de.htwg.upfaz.backgammon.gui.Log;
import controllers.de.htwg.util.observer.IObservable;
import controllers.de.htwg.util.observer.Observable;

public final class Game extends Observable implements IObservable, Runnable,
		IGame {

	private static final int STONES_TO_WIN = 15;

	private Players players;
	private GameMap gameMap;

	private int winner;
	private boolean endPhase;
	private Dice dice;

	private int firstClick = -1;
	private int secondClick = -1;

	private String status = "Let's the game begin!";
	private String currentMehtodName = "Begin";
	private int result;


	private static final String STRING1 = "Setting startNumber to %d and targetNumber to %d";
	public static final String STRING2 = "Can't jump this Field (Color problem)";
	public static final String YOU_CAN_NOT_PLAY_WITH_THIS_STONE = "You can not play with this stone!";

	public Game() {

		players = new Players(this);
		gameMap = new GameMap(this, players);
		notifyObservers();
	}

	public void run() {
		while (winner == 0) {
			try {
				startRound();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String s) {
		status = s;
		notifyObservers();
	}

	public void setResult(final int result) {
		// public boolean setResult(final int result) {
		/*
		 * boolean returnVal = false; try { this.state = state.click(result);
		 * notifyObservers(); returnVal = true; } catch (WrongClickException e)
		 * { Log.v(e); }
		 *
		 * return returnVal;
		 *
		this.result = result;
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

	/*  IMPLEMENTED IN GAMENEW
	private boolean checkForWinner() {
		return gameMap.getField(GameMap.FIELD_END_BLACK).getNumberStones() == STONES_TO_WIN
				|| gameMap.getField(GameMap.FIELD_END_WHITE).getNumberStones() == STONES_TO_WIN;
	}
 	*/
	/*  IMPLEMENTED IN GAMENEW
	private boolean isNotCheckDirection() {
		if (players.getColor() == Players.PLAYER_COLOR_WHITE
				&& secondClick <= firstClick
				|| players.getColor() == Players.PLAYER_COLOR_BLACK
				&& secondClick >= firstClick) {
			setStatus("You're going in the wrong direction!");
			return true;
		}
		return false;
	}
	*/
	/*  IMPLEMENTED IN GAMENEW
	private void renewJumps(final int start, final int target) {
		currentMehtodName = "move";
		if (target >= GameMap.FIELD_END_BLACK) {
			dice.renewJumpsEndPhase(start);
		} else {
			dice.move(Math.abs(start - target));
		}
	}
	*

	@Override
	public void setDice(final Dice dice) {
		this.dice = dice;
	}

	//24.11 - removed the validation, has to be added later
	private boolean checkAllStartnumbersValidness() {
		boolean toReturn = true;
		for (int i = 0; i < 4; i++) {
			try {
				if (dice.getDiceAt(i) != 0
						&& !((players.getColor() == Players.PLAYER_COLOR_WHITE && (firstClick
								+ dice.getDiceAt(i) > 23 || gameMap.getField(
								firstClick + dice.getDiceAt(i)).isNotJumpable(
								players.getColor()))) || (players.getColor() == Players.PLAYER_COLOR_BLACK && (firstClick
								- dice.getDiceAt(i) < 0 || gameMap.getField(
								firstClick - dice.getDiceAt(i)).isNotJumpable(
								players.getColor()))))) {
					toReturn = false;
				}
			} catch (Exception e) {
				Log.verbose("checkStartValidness");
				Log.verbose(e);
			}

		}

		return toReturn;
	}

	/*   IMPLEMENTED IN GAMENEW
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

		endPhase = stonesInEndPhase == STONES_TO_WIN;
		if (endPhase) {
			setStatus("End Phase!");
		}
	}
	*
	// this function works with gui - that's why the tui
	// doesn't work now
	// called in startRound() and works with BackgammonFrame bf
	// (which is actually the gui).
	// There is similar function for the tui,
	// but I don't know how to make them work parallel
	private boolean notGetStartAndTargetNumbers() throws InterruptedException {
		if (gameMap.isWhiteEaten()) { // white eaten

			// TO ADD: check if is possible to play turn
			firstClick = GameMap.FIELD_EATEN_WHITE;
			// get targetNumber
			result = -1;
			while (result == -1 || result > 6) {
				Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
			}
			secondClick = result;

		} else if (gameMap.isBlackEaten()) { // black eaten

			firstClick = GameMap.FIELD_EATEN_BLACK;

			// get targetNumber
			result = -1;
			while (result >= GameMap.FIELD_EATEN_BLACK || result < 18) {
				Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
			}
			secondClick = result;
		} else if (endPhase) { // endphase
			// get startNumber
			result = -1;
			getStartNumber();

			// get targetNumber
			result = -1;
			getEndNumber();


		}
		// else normal turn
		return false;
	}

	private void startRound() throws InterruptedException {
		/* IMPLEMENTED IN GAMENEW
		players.changeCurrentPlayer();
		checkEndPhase();

		dice = new Dice();

		*
		// automatic takeout for taking out stones automatically if its possible
		// useful in endphase or when stone is eaten and has to be returned back
		// to desk
		if (endPhase) {
			System.out.println("It's endphase, trying automatic takeout");
			int counter = 6;
			if (players.getColor() == Players.PLAYER_COLOR_BLACK) { // black

				for (int i = 5; i >= 0; i--) {
					if (gameMap.getField(i).getStoneColor() == Players.PLAYER_COLOR_BLACK) {
						break;
					} else {
						counter--; // wtf!
					}
				}
			} else { // white

				for (int i = 18; i <= 23; i++) {
					if (gameMap.getField(i).getStoneColor() == Players.PLAYER_COLOR_WHITE) {
						break;
					} else {
						counter--;
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (dice.getDiceAt(i) >= counter) {
					if (players.getColor() == Players.PLAYER_COLOR_BLACK) {
						firstClick = counter - 1;
						secondClick = 26;
					} else {
						firstClick = 24 - counter;
						secondClick = 27;
					}
					try {
						if (gameMap.getField(firstClick).getNumberStones() > 0) {
							gameMap.moveStone(firstClick, secondClick);

						}
					} catch (Exception ignored) {
						return;
					}
				}
			}
		}

		// Actual "turn" - one player has 0 to 4 turns per round
		while (dice.hasTurnsLeft()) {

			for (int i = 0; i < 24; i++) {
				if (gameMap.getField(i).getStoneColor() == players.getColor()) {
					startNumber = i;
					if (!checkAllStartnumbersValidness()) {
						startNumber = -1;
						return;
					}
				}
			}


			if (endPhase) {
				firstClick = -1;
				return;
			}

			// getting start and target number (for now from GUI! - has to be
			// parallelised)
			// if no numbers got - try one more time
			if (notGetStartAndTargetNumbers()) {
				firstClick = -1;
				continue;
			}
		}

		notifyObservers();
	}

	private void getStartNumber() {}
	/* IMPLEMENTED IN GAMENEW
	private void getStartNumber() {

		int localStartNumber = result;

		if (gameMap.getField(localStartNumber).getStoneColor() == players
				.getColor()) {
			firstClick = localStartNumber;
		} else {
			Log.verbose("You can't move this piece or there are no pieces");
			result = -1;
			getStartNumber();
		}
	}
	*

	private void getEndNumber() {
		try {

			while (result < 0
					|| result == GameMap.FIELD_EATEN_BLACK
					|| result == GameMap.FIELD_EATEN_WHITE
					|| result == firstClick
					|| (result == GameMap.FIELD_END_BLACK || result == GameMap.FIELD_END_WHITE)
					&& !endPhase) {
				Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
			}

			if (result >= 0 && result < GameMap.FIELD_EATEN_BLACK) {
				// implemented in gameNew
//				if (gameMap.getField(result).isNotJumpable(players.getColor())) {
//					Log.verbose(STRING2);
//					result = -1;
//					getEndNumber();// or get startNumber()?
//				}
//				if (dice.checkDistance(Math.abs(result - firstClick))) {
//					this.secondClick = result;
//				} else {
//					result = -1;
//					getEndNumber();// or get startNumber()?
//				}
			} else if (result == GameMap.FIELD_END_BLACK) {
				boolean returnVal = false;

				for (int i = 0; i < 4; i++) {
					returnVal = returnVal
							|| dice.getDiceAt(i) >= firstClick + 1;
				}

				if (returnVal) {
					secondClick = GameMap.FIELD_END_BLACK;
				} else {
					result = -1;
					getEndNumber();// or get startNumber()?
				}
			} else if (result == GameMap.FIELD_END_WHITE) {
				if (dice.checkDistance(Math.abs(GameMap.FIELD_END_WHITE
						- firstClick - 3))) {
					secondClick = GameMap.FIELD_END_WHITE;
				} else {
					result = -1;
					getEndNumber();// or get startNumber()?
				}
			}
		} catch (Exception e) {
			Log.verbose("getEndNumber");
			Log.verbose(e);
		}
	}


}
*/
