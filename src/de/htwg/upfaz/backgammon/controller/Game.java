package de.htwg.upfaz.backgammon.controller;

import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.upfaz.backgammon.entities.Player;
import de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import de.htwg.upfaz.backgammon.gui.GameWithTui;
import de.htwg.upfaz.backgammon.gui.Log;
import de.htwg.upfaz.backgammon.gui.Tui;
import de.htwg.util.observer.IObservable;
import de.htwg.util.observer.Observable;

import java.util.Arrays;
import java.util.Random;

public final class Game extends Observable implements IObservable {

	private static final int FIELD_NR_0 = 0;
	private static final int FIELD_NR_5 = 5;
	private static final int FIELD_NR_7 = 7;
	private static final int FIELD_NR_11 = 11;
	private static final int FIELD_NR_16 = 16;
	private static final int FIELD_NR_12 = 12;
	private static final int FIELD_NR_18 = 18;
	private static final int FIELD_NR_23 = 23;
	private static final int FIELD_EATEN_WHITE = 25;
	private static final int FIELD_EATEN_BLACK = 24;
	private static final int FIELD_END_WHITE = 27;
	private static final int FIELD_END_BLACK = 26;
	private static final int TOTAL_FIELDS_NR = 28;
	private static final int STONE_INIT_2 = 2;
	private static final int STONE_INIT_5 = 5;
	private static final int STONE_INIT_3 = 3;
	private static final int DICE_RANDOM = 6;
	private static final int MAX_JUMPS = 4;
	private static final int STONES_TO_WIN = 15;
	private final BackgammonFrame bf;

	private Field[] gameMap;
	private final IPlayer[] players;

	private int winner;
	private boolean endPhase;
	private int[] jumps = new int[4];
	private int[] jumpsT = new int[4];
	private int startNumber = -1;
	private int targetNumber = -1;

	private String status = "Let's the game begin!";
	private String currentMehtodName = "Begin";
	private int currentPlayer;
	private Tui tui;

	protected static final String PLAYER_S_IS_THE_WINNER = "Player %s is the winner!";
	protected static final String PLAYER_S_IT_S_YOUR_TURN = "Player %s, it's your Turn!";
	protected static final String SETTING_START_NUMBER_TO_D_AND_TARGET_NUMBER_TO_D = "Setting startNumber to %d and targetNumber to %d";

	public Game() {
		gameMap = new Field[TOTAL_FIELDS_NR];

		for (int i = 0; i < gameMap.length; i++) {
			gameMap[i] = new Field(i);
		}

		initStones(gameMap); // place stones to the gameMap
		players = new IPlayer[2];
		players[0] = new Player(0);
		players[1] = new Player(1);
		currentPlayer = 1;
		this.bf = new BackgammonFrame(this); // actually gui

		// here should be created tui.
		this.tui = new Tui(this); // tui

		this.addObserver(tui);

		// was before in playGameWithGui() function
		this.addObserver(bf);
		this.notifyObservers();
	}

	private static void initStones(final IField[] gm) {

		gm[FIELD_NR_0].setStoneColor(0);
		gm[FIELD_NR_0].setNumberStones(STONE_INIT_2);

		gm[FIELD_NR_11].setStoneColor(0);
		gm[FIELD_NR_11].setNumberStones(STONE_INIT_5);

		gm[FIELD_NR_16].setStoneColor(0);
		gm[FIELD_NR_16].setNumberStones(STONE_INIT_3);

		gm[FIELD_NR_18].setStoneColor(0);
		gm[FIELD_NR_18].setNumberStones(STONE_INIT_5);

		gm[FIELD_NR_23].setStoneColor(1);
		gm[FIELD_NR_23].setNumberStones(STONE_INIT_2);

		gm[FIELD_NR_12].setStoneColor(1);
		gm[FIELD_NR_12].setNumberStones(STONE_INIT_5);

		gm[FIELD_NR_7].setStoneColor(1);
		gm[FIELD_NR_7].setNumberStones(STONE_INIT_3);

		gm[FIELD_NR_5].setStoneColor(1);
		gm[FIELD_NR_5].setNumberStones(STONE_INIT_5);

		gm[FIELD_EATEN_BLACK].setStoneColor(1); // 24
		gm[FIELD_EATEN_WHITE].setStoneColor(0); // 25

		gm[FIELD_END_BLACK].setStoneColor(1); // 26
		gm[FIELD_END_WHITE].setStoneColor(0); // 27

		/*
		 * endspiel tests gm[3].setStoneColor(1); gm[4].setStoneColor(1);
		 * gm[5].setStoneColor(1); gm[3].setNumberStones(5);
		 * gm[4].setNumberStones(5); gm[5].setNumberStones(5);
		 * 
		 * gm[18].setStoneColor(0); gm[19].setStoneColor(0);
		 * gm[20].setStoneColor(0); gm[18].setNumberStones(5);
		 * gm[19].setNumberStones(5); gm[20].setNumberStones(5);
		 */
	}

	public void setGameMap(final Field[] newGameMap) {

		if (newGameMap == null) {
			gameMap = new Field[0];
		} else {
			gameMap = Arrays.copyOf(newGameMap, newGameMap.length);
		}
	}

	public Field[] getGameMap() {
		return gameMap;
	}

	/* Generate jumps (before the turn) */
	public int[] rollTheDice() {
		final int[] jumpsToReturn = new int[MAX_JUMPS];
		final Random dice = new Random();
		for (int index = 0; index < 2; index++) {
			jumpsToReturn[index] = dice.nextInt(DICE_RANDOM) + 1;
		}
		if (jumpsToReturn[0] == jumpsToReturn[1]) {
			jumpsToReturn[2] = jumpsToReturn[0];
			jumpsToReturn[3] = jumpsToReturn[0];
			setStatus("You're lucky bastard");
		} else {
			jumpsToReturn[2] = 0;
			jumpsToReturn[3] = 0;
		}

		System.out.println(String.format("So, youre moves are: %d, %d",
				jumpsToReturn[0], jumpsToReturn[1]));

		// setStatus(String.format("So, youre moves are: %d, %d",
		// jumpsToReturn[0], jumpsToReturn[1]));
		setStatus("");
		return jumpsToReturn;
	}

	/*
	 * eatStone function for eating stone action -> operating with Field[] array
	 */
	private Field[] eatStone(final Field[] gameMap, final IPlayer plr,
			final int startNumber, final int targetNumber) {
		currentMehtodName = "eatStone";
		gameMap[targetNumber].setNumberStones(1);
		gameMap[targetNumber].setStoneColor(plr.getColor());
		if (plr.getColor() == 0) {
			gameMap[FIELD_EATEN_BLACK]
					.setNumberStones(gameMap[FIELD_EATEN_BLACK]
							.getNumberStones() + 1);
			gameMap[FIELD_EATEN_BLACK].setStoneColor(1);
		} else {
			gameMap[FIELD_EATEN_WHITE]
					.setNumberStones(gameMap[FIELD_EATEN_WHITE]
							.getNumberStones() + 1);
			gameMap[FIELD_EATEN_WHITE].setStoneColor(0);
		}
		gameMap[startNumber].setNumberStones(gameMap[startNumber]
				.getNumberStones() - 1);
		setStatus("Eating the stone");
		return gameMap;
	}

	private Field[] moveStone(final Field[] gameMap, final IPlayer plr,
			final int startNumber, final int targetNumber) {
		currentMehtodName = "moveStone";
		gameMap[targetNumber].setNumberStones(gameMap[targetNumber]
				.getNumberStones() + 1);
		gameMap[targetNumber].setStoneColor(plr.getColor());
		gameMap[startNumber].setNumberStones(gameMap[startNumber]
				.getNumberStones() - 1);
		if (gameMap[startNumber].getNumberStones() == 0) {
			gameMap[startNumber].setStoneColor(-1);
		}
		setStatus("Moving the stone");
		return gameMap;
	}

	public boolean checkForWinner() {
		// if (gameMap[FIELD_END_BLACK].getNumberStones() == 15
		// || gameMap[FIELD_END_WHITE].getNumberStones() == 15) {
		// return true;
		// } else {
		// return false;
		// }
		IField[] gameMap = getGameMap();
		return gameMap[FIELD_END_BLACK].getNumberStones() == STONES_TO_WIN
				|| gameMap[FIELD_END_WHITE].getNumberStones() == STONES_TO_WIN;
	}

	private Field[] takeOutStone(final Field[] gameMap, final int startNumber,
			final int targetNumber) {
		currentMehtodName = "takeOutStone";
		gameMap[targetNumber].setNumberStones(gameMap[targetNumber]
				.getNumberStones() + 1);
		gameMap[startNumber].setNumberStones(gameMap[startNumber]
				.getNumberStones() - 1);
		if (gameMap[startNumber].getNumberStones() == 0) {
			gameMap[startNumber].setStoneColor(-1);
		}
		setStatus("Taking out the stone");
		return gameMap;
	}

	/* Move, take out or eat another stone. */
	public Field[] doSomethingWithStones() {
		currentMehtodName = "doSomethingWithStones";
		IPlayer plr = getCurrentPlayer();
		int startNumber = getStartNumber();
		int targetNumber = getTargetNumber();

		// eat or move stone
		if (gameMap[targetNumber].getNumberStones() == 1
				&& gameMap[targetNumber].getStoneColor() != plr.getColor()) {

			gameMap = eatStone(gameMap, plr, startNumber, targetNumber);
		} else if (isEndPhase() && this.targetNumber > 25) {

			gameMap = takeOutStone(gameMap, startNumber, targetNumber);
		} else {

			gameMap = moveStone(gameMap, plr, startNumber, targetNumber);
		}
		return gameMap;
	}

	public int getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(final int number) {
		startNumber = number;
	}

	public int getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(final int number) {
		targetNumber = number;
	}

	public boolean isNotCheckDirection(final IPlayer plr) {
		if (plr.getColor() == 0 && targetNumber <= startNumber
				|| plr.getColor() == 1 && targetNumber >= startNumber) {
			setStatus("You're going in the wrong direction!");
			return true;
		}
		return false;
	}

	public void renewJumps(final int start, final int target) {
		currentMehtodName = "renewJumps";
		if (target >= FIELD_END_BLACK) {
			renewJumpsEndPhase(start);
		} else {
			for (int i = 0; i < MAX_JUMPS; i++) {
				if (Math.abs(target - start) == jumps[i]) {
					jumps[i] = 0;
					setStatus(String.format("Setting jumps[%d] to 0", i));
					break;
				}
			}
		}
	}

	private void renewJumpsEndPhase(final int start) {
		for (int i = 0; i < MAX_JUMPS; i++) {
			if (24 - start == jumps[i] || start + 1 == jumps[i]) {
				jumps[i] = 0;
				setStatus(String.format("Setting jumps[%d] to 0", i));
				break; // TODO check if break also stops for loop
			}
		}
	}

	/* Calculating stones in Endphase-Fields to know if it's endphase or not */
	int calcStoneInEndPhase(final IPlayer plr, final IField[] gm) {
		currentMehtodName = "calcStoneInEndPhase";
		int stonesInEndPhase = 0;
		if (plr.getColor() == 0) {
			for (int i = 18; i <= 23; i++) { // Fields 18-23 for white player
												// are endphase fields
				if (gm[i].getStoneColor() == plr.getColor()) {
					stonesInEndPhase += gm[i].getNumberStones();
				}
			}
			stonesInEndPhase += gm[FIELD_END_WHITE].getNumberStones();
		} else {
			for (int i = 5; i >= 0; i--) { // Fields 0-5 for black player are
											// endphase fields
				if (gm[i].getStoneColor() == plr.getColor()) {
					stonesInEndPhase += gm[i].getNumberStones();
				}
			}
			stonesInEndPhase += gm[FIELD_END_BLACK].getNumberStones();
		}

		return stonesInEndPhase;
	}

	public void setJumps(final int[] newJumps) {

		if (newJumps == null) {
			jumps = new int[0];
		} else {
			jumps = Arrays.copyOf(newJumps, newJumps.length);
		}
	}

	public int[] getJumps() {
		return jumps;
	}

	public int getWinner() {
		return winner;
	}

	public IPlayer getPlayer1() {
		return players[0];
	}

	public IPlayer getPlayer2() {
		return players[1];
	}

	public IPlayer getCurrentPlayer() {
		return players[currentPlayer];
	}

	/*
	 * public void setCurrentPlayer(final IPlayer currentPlayer) {
	 * this.currentPlayer = currentPlayer; }
	 */

	public String getStatus() {
		return status;
	}

	public void setStatus(final String s) {
		status = s;
		notifyObservers();
	}

	public boolean checkStartNumber(final int number) {
		currentMehtodName = "checkStartNumber";
		try {
			return gameMap[number].getStoneColor() == players[currentPlayer]
					.getColor();
		} catch (Exception e) {
			Log.verbose(e);
			return false;
		}
	}

	public void printJumpsStatus(final int[] jumps) {
		setStatus(String.format("So, youre moves are: %d, %d, %d, %d",
				jumps[0], jumps[1], jumps[2], jumps[3]));
	}

	public String printJumpsString() {
		return String.format("Jumps: %d, %d, %d, %d", jumps[0], jumps[1],
				jumps[2], jumps[3]);
	}

	public int[] getJumpsT() {
		return jumpsT;
	}

	public void setJumpsT(final int[] j) {
		jumpsT = new int[2];
		jumpsT[0] = j[0];
		jumpsT[1] = j[1];
	}

	public boolean isEatenWhiteCheck(final int target) {
		boolean returnVal = false;

		for (int i = 0; i < MAX_JUMPS; i++) {
			returnVal = target == jumps[i] - 1;
			if (returnVal) {
				jumps[i] = 0;
				break;
			}
		}

		return returnVal;
	}

	public boolean checkNormalEndTarget(final int newTarget) {
		boolean returnVal = false;

		for (int i = 0; i < MAX_JUMPS; i++) {
			returnVal = Math.abs(newTarget - startNumber) == jumps[i];
			if (returnVal) {
				break;
			}
		}

		return returnVal;
	}

	public boolean checkEndphaseWhiteTarget(final int newTarget) {
		currentMehtodName = "checkEndphaseWhiteTarget";

		boolean returnVal = false;

		for (int i = 0; i < MAX_JUMPS; i++) {
			returnVal = Math.abs(newTarget - startNumber - 3) <= jumps[i];
			if (returnVal) {
				break;
			}
		}

		return returnVal;
	}

	public boolean checkEndphaseBlackTarget() {
		currentMehtodName = "checkEndphaseBlackTarget";

		boolean returnVal = false;

		for (int i = 0; i < 4; i++) {
			returnVal = jumps[i] >= startNumber + 1;
			if (returnVal) {
				break;
			}
		}

		return returnVal;
	}

	public boolean checkAllStartnumbersValidness() {
		currentMehtodName = "checkStartValidnessLoop";
		boolean toReturn = true;
		for (int i = 0; i < 4; i++) {
			if (jumps[i] != 0 && checkSingleStartValidness(i)) {
				toReturn = false;
			}
		}

		return toReturn;
	}

	private boolean checkSingleStartValidness(final int i) {
		currentMehtodName = "checkStartValidness";

		try {
			if (players[currentPlayer].getColor() == 0
					&& (startNumber + jumps[i] > 23 || gameMap[startNumber
							+ jumps[i]].isNotJumpable(players[currentPlayer]
							.getColor()))) {
				return false;
			} else if (players[currentPlayer].getColor() == 1
					&& (startNumber - jumps[i] < 0 || gameMap[startNumber
							- jumps[i]].isNotJumpable(players[currentPlayer]
							.getColor()))) {
				return false;
			}
		} catch (Exception e) {
			Log.verbose("checkStartValidness");
			Log.verbose(e);
		}
		return true;
	}

	public String getCurrentMethodName() {
		return currentMehtodName;
	}

	public void setCurrentMethodName(final String newName) {
		currentMehtodName = newName;
	}

	public boolean isEndPhase() {
		return endPhase;
	}

	public void setEndPhase(final boolean endPhase) {
		this.endPhase = endPhase;
	}

	public void setWinner(final int winner) {
		this.winner = winner;
	}

	public int getTurnsNumber() {
		int returnVal = 4;

		if (jumps[3] == 0) {
			returnVal = 2;
		}

		return returnVal;
	}

	public void checkEndPhase() {
		endPhase = false;
		if (calcStoneInEndPhase(players[currentPlayer], gameMap) == STONES_TO_WIN) {
			endPhase = true;
			setStatus("End Phase!");
		}
	}

	public boolean checkIfMoveImpossible() {

		boolean toReturn = true;

		for (int i = 0; i < 24; i++) {
			if (gameMap[i].getStoneColor() == players[currentPlayer].getColor()) {
				startNumber = i;
				if (!checkAllStartnumbersValidness()) {

					toReturn = false;
				}
			}
		}

		if (endPhase) {
			toReturn = false;
		}

		startNumber = -1;
		return toReturn;
	}

	public int automaticTakeOut() {
		int toReturn = 0;
		if (endPhase) {

			int counter = 6;
			if (players[currentPlayer].getColor() == 1) { // black

				for (int i = 5; i >= 0; i--) {
					if (gameMap[i].getStoneColor() == players[currentPlayer]
							.getColor()) {
						break;
					} else {
						counter--;
					}
				}
			} else { // white

				for (int i = 18; i <= 23; i++) {
					if (gameMap[i].getStoneColor() == players[currentPlayer]
							.getColor()) {
						break;
					} else {
						counter--;
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (jumps[i] >= counter) {
					if (players[currentPlayer].getColor() == 1) {
						startNumber = counter - 1;
						targetNumber = 26;
					} else {
						startNumber = 24 - counter;
						targetNumber = 27;
					}
					try {
						if (gameMap[startNumber].getNumberStones() > 0) {
							takeOutStone(gameMap, startNumber, targetNumber);
							renewJumps(startNumber, targetNumber);
							toReturn++;
						}
					} catch (Exception ignored) {
						return 0;
					}
				}
			}
		}

		return toReturn;
	}

	private void changeCurrentPlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 0;
		} else {
			currentPlayer = 1;
		}
		setStatus(String
				.format(PLAYER_S_IT_S_YOUR_TURN, players[currentPlayer]));
	}

	public BackgammonFrame getBackgammonFrame() {
		return bf;
	}

	public String toString() {
		return String
				.format("Game{gameMap=%s, player1=%s, player2=%s, winner=%d, endPhase=%s, jumps=%s, jumpsT=%s, startNumber=%d, targetNumber=%d, status='%s', currentMehtodName='%s', currentPlayer=%s}",
						Arrays.toString(gameMap), players[0], players[1],
						winner, endPhase, Arrays.toString(jumps),
						Arrays.toString(jumpsT), startNumber, targetNumber,
						status, currentMehtodName, currentPlayer);
	}

	// this function works with gui - that's why the tui
	// doesn't work now
	// called in startRound() and works with BackgammonFrame bf
	// (which is actually the gui).
	// There is similar function for the tui,
	// but I don't know how to make them work parallel
	private boolean notGetStartAndTargetNumbers() {
		setCurrentMethodName("getStartAndTargetNumbers");

		if (getCurrentPlayer().getColor() == 0
				&& getGameMap()[25].getNumberStones() > 0) { //white eaten

			// TO ADD: check if is possible to play turn
			setStartNumber(25);
			// get targetNumber
			bf.setResult(-1);
			setTargetNumber(bf.getTargetWhileEatenWhite());
		} else if (getCurrentPlayer().getColor() == 1
				&& getGameMap()[24].getNumberStones() > 0) { //black eaten

			setStartNumber(24);

			// get targetNumber
			bf.setResult(-1);
			setTargetNumber(bf.getTargetWhileEatenBlack());
		} else if (isEndPhase()) { //endphase
			// get startNumber
			bf.setResult(-1);
			bf.getStartNumber();

			// get targetNumber
			bf.setResult(-1);
			bf.getEndNumber();

			// check direction
			if (getTargetNumber() < 24
					&& isNotCheckDirection(getCurrentPlayer())) {
				return true;
			}
		} else { //normal turn

			// get startNumber
			bf.setResult(-1);
			bf.getStartNumber();

			if (checkAllStartnumbersValidness()) {
				setStatus(GameWithTui.YOU_CAN_NOT_PLAY_WITH_THIS_STONE);
				return true;
			}

			// get targetNumber
			bf.setResult(-1);
			bf.getEndNumber();

			// check direction
			if (getTargetNumber() < 24
					&& isNotCheckDirection(getCurrentPlayer())) {
				return true;
			}
		}

		setStatus(String.format(
				SETTING_START_NUMBER_TO_D_AND_TARGET_NUMBER_TO_D,
				getStartNumber(), getTargetNumber()));
		return false;
	}

	/*
	 * Turn logic
	 */
	public void startRound() {
		changeCurrentPlayer();
		checkEndPhase();

		// i don't know how it works, but don't remove it - here was the problem
		setJumps(rollTheDice());
		setJumpsT(getJumps());

		// automatic takeout for taking out stones automatically if its possible
		// useful in endphase or when stone is eaten and has to be returned back
		// to desk
		int index = automaticTakeOut();

		// Actual "turn" - one player has 0 to 4 turns per round
		for (int i = index; i < getTurnsNumber(); i++) {
			// check for winner
			if (checkForWinner()) {
				setStatus(String.format(PLAYER_S_IS_THE_WINNER,
						players[currentPlayer]));
				setWinner(players[currentPlayer].getColor() + 1);
				return;
			}

			checkEndPhase();

			// check if it possible to make move
			if (checkIfMoveImpossible()) { // noMovesDialog();
				System.out.println("no moves available");
				return;
			}

			// getting start and target number (for now from GUI! - has to be
			// parallelised)
			// if no numbers got - try one more time
			if (notGetStartAndTargetNumbers()) {
				setStartNumber(-1);
				i--;
				continue;
			}

			// change gameMap (Move, take out or eat another stone)
			setGameMap(doSomethingWithStones());
			renewJumps(getStartNumber(), getTargetNumber());
			setStartNumber(-1);
			setTargetNumber(-1);

			// check for winner
			if (checkForWinner()) {
				setStatus(String.format(PLAYER_S_IS_THE_WINNER,
						players[currentPlayer].toString()));
				setWinner(players[currentPlayer].getColor() + 1);
				return;
			}
			printJumpsStatus(getJumps());
		}

		notifyObservers();
	}
}
