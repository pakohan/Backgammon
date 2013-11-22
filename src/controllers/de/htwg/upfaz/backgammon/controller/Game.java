package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.Field;
import controllers.de.htwg.upfaz.backgammon.entities.IField;
import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.entities.Player;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;
import controllers.de.htwg.upfaz.backgammon.gui.Log;
import controllers.de.htwg.util.observer.IObservable;
import controllers.de.htwg.util.observer.Observable;

import java.util.Arrays;

public final class Game
        extends Observable
        implements IObservable, Runnable, IGame {

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
    private static final int STONES_TO_WIN = 15;
    private static final int PLAYER_COLOR_WHITE = 0;
    private static final int PLAYER_COLOR_BLACK = 1;

    private static final Game GAME = new Game();

    private Field[] gameMap;
    private final IPlayer[] players;

    private int winner;
    private boolean endPhase;
    private Dice dice;
    private int startNumber = -1;
    private int targetNumber = -1;

    private String status = "Let's the game begin!";
    private String currentMehtodName = "Begin";
    private int currentPlayer;
    private int result;

    private static final String PLAYER_S_IS_THE_WINNER = "Player %s is the winner!";
    private static final String PLAYER_S_IT_S_YOUR_TURN = "Player %s, it's your Turn!";
    private static final String STRING1 = "Setting startNumber to %d and targetNumber to %d";
    public static final String STRING2 = "Can't jump this Field (Color problem)";
    public static final String YOU_CAN_NOT_PLAY_WITH_THIS_STONE = "You can not play with this stone!";

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
        notifyObservers();
    }

    public void run() {
        while (getWinner() == 0) {
            startRound();
        }
    }

    public Field[] getGameMap() {
        return gameMap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String s) {
        status = s;
        notifyObservers();
    }

    private State state;

    public void setResult(final int result) {
        //public boolean setResult(final int result) {
        /*
		boolean returnVal = false;
		try {
			this.state = state.click(result);
			notifyObservers();
			returnVal = true;
		} catch (WrongClickException e) {
			Log.v(e);
		}

		return returnVal;
		*/
        this.result = result;
    }

    public IPlayer getCurrentPlayer() {
        return players[currentPlayer];
    }

    @Override
    public String toString() {
        return String.format("start = %d, target = %d, result = %d; Current player: %s; Method: %s", startNumber, targetNumber, result, getCurrentPlayer(), currentMehtodName);
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

    private void setGameMap(final Field[] newGameMap) {

        if (newGameMap == null) {
            gameMap = new Field[0];
        } else {
            gameMap = Arrays.copyOf(newGameMap, newGameMap.length);
        }
    }

    /*
     * eatStone function for eating stone action -> operating with Field[] array
     */
    private Field[] eatStone(final int startNumber, final int targetNumber) {
        currentMehtodName = "eatStone";
        gameMap[targetNumber].setNumberStones(1);
        gameMap[targetNumber].setStoneColor(players[currentPlayer].getColor());
        if (players[currentPlayer].getColor() == PLAYER_COLOR_WHITE) {
            gameMap[FIELD_EATEN_BLACK].setNumberStones(gameMap[FIELD_EATEN_BLACK].getNumberStones() + 1);
            gameMap[FIELD_EATEN_BLACK].setStoneColor(1);
        } else {
            gameMap[FIELD_EATEN_WHITE].setNumberStones(gameMap[FIELD_EATEN_WHITE].getNumberStones() + 1);
            gameMap[FIELD_EATEN_WHITE].setStoneColor(0);
        }
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        setStatus("Eating the stone");
        return gameMap;
    }

    private Field[] moveStone(final int startNumber, final int targetNumber) {
        currentMehtodName = "moveStone";
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[targetNumber].setStoneColor(players[currentPlayer].getColor());
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
        setStatus("Moving the stone");
        return gameMap;
    }

    private boolean checkForWinner() {
        return gameMap[FIELD_END_BLACK].getNumberStones() == STONES_TO_WIN || gameMap[FIELD_END_WHITE].getNumberStones() == STONES_TO_WIN;
    }

    private Field[] takeOutStone(final int startNumber, final int targetNumber) {
        currentMehtodName = "takeOutStone";
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
        setStatus("Taking out the stone");
        return gameMap;
    }

    /* Move, take out or eat another stone. */
    private Field[] doSomethingWithStones() {
        currentMehtodName = "doSomethingWithStones";

        if (gameMap[targetNumber].getNumberStones() == 1 && gameMap[targetNumber].getStoneColor() != players[currentPlayer].getColor()) {

            gameMap = eatStone(startNumber, targetNumber);
        } else if (endPhase && targetNumber > FIELD_EATEN_WHITE) {

            gameMap = takeOutStone(startNumber, targetNumber);
        } else {

            gameMap = moveStone(startNumber, targetNumber);
        }
        return gameMap;
    }

    private boolean isNotCheckDirection() {
        if (players[currentPlayer].getColor() == PLAYER_COLOR_WHITE && targetNumber <= startNumber || players[currentPlayer].getColor() == PLAYER_COLOR_BLACK && targetNumber >= startNumber) {
            setStatus("You're going in the wrong direction!");
            return true;
        }
        return false;
    }

    private void renewJumps(final int start, final int target) {
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
                return; // TODO check if break also stops for loop
            }
        }
    }

    /* Calculating stones in Endphase-Fields to know if it's endphase or not */
    private int calcStoneInEndPhase() {
        currentMehtodName = "calcStoneInEndPhase";
        int stonesInEndPhase = 0;
        if (players[currentPlayer].getColor() == PLAYER_COLOR_WHITE) {
            for (int i = 18; i <= 23; i++) { // Fields 18-23 for white player
                // are endphase fields
                if (gameMap[i].getStoneColor() == players[currentPlayer].getColor()) {
                    stonesInEndPhase += gameMap[i].getNumberStones();
                }
            }
            stonesInEndPhase += gameMap[FIELD_END_WHITE].getNumberStones();
        } else {
            for (int i = 5; i >= 0; i--) { // Fields 0-5 for black player are
                // endphase fields
                if (gameMap[i].getStoneColor() == players[currentPlayer].getColor()) {
                    stonesInEndPhase += gameMap[i].getNumberStones();
                }
            }
            stonesInEndPhase += gameMap[FIELD_END_BLACK].getNumberStones();
        }

        return stonesInEndPhase;
    }

    @Override
    public void setDice(final Dice dice) {
        this.dice = dice;
    }

    @Override
    public Game getInstance() {
        return GAME;
    }

    private int getWinner() {
        return winner;
    }

    private boolean checkStartNumber(final int number) {
        currentMehtodName = "checkStartNumber";
        try {
            return gameMap[number].getStoneColor() == players[currentPlayer].getColor();
        } catch (Exception e) {
            Log.verbose(e);
            return false;
        }
    }

    private boolean checkNormalEndTarget(final int newTarget) {
        boolean returnVal = false;

        for (int i = 0; i < MAX_JUMPS; i++) {
            returnVal = Math.abs(newTarget - startNumber) == jumps[i];
            if (returnVal) {
                break;
            }
        }

        return returnVal;
    }

    private boolean checkEndphaseWhiteTarget(final int newTarget) {
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

    private boolean checkEndphaseBlackTarget() {
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

    private boolean checkAllStartnumbersValidness() {
        currentMehtodName = "checkAllStartnumbersValidness";
        boolean toReturn = true;
        for (int i = 0; i < 4; i++) {
            if (jumps[i] != 0 && checkSingleStartValidness(i)) {
                toReturn = false;
            }
        }

        return toReturn;
    }

    private boolean checkSingleStartValidness(final int i) {
        currentMehtodName = "checkSingleStartValidness";

        try {
            if (players[currentPlayer].getColor() == PLAYER_COLOR_WHITE && (startNumber + jumps[i] > 23 || gameMap[startNumber + jumps[i]].isNotJumpable(players[currentPlayer].getColor()))) {
                return false;
            } else if (players[currentPlayer].getColor() == PLAYER_COLOR_BLACK && (startNumber - jumps[i] < 0 || gameMap[startNumber - jumps[i]].isNotJumpable(players[currentPlayer].getColor()))) {
                return false;
            }
        } catch (Exception e) {
            Log.verbose("checkStartValidness");
            Log.verbose(e);
        }
        return true;
    }

    private int getTurnsNumber() {
        int returnVal = 4;

        if (jumps[3] == 0) {
            returnVal = 2;
        }

        return returnVal;
    }

    private void checkEndPhase() {
        endPhase = calcStoneInEndPhase() == STONES_TO_WIN;
        if (endPhase) {
            setStatus("End Phase!");
        }
    }

    private boolean checkIfMoveImpossible() {

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

    private int automaticTakeOut() {
        int toReturn = 0;
        if (endPhase) {

            int counter = 6;
            if (players[currentPlayer].getColor() == PLAYER_COLOR_BLACK) { // black

                for (int i = 5; i >= 0; i--) {
                    if (gameMap[i].getStoneColor() == players[currentPlayer].getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            } else { // white

                for (int i = 18; i <= 23; i++) {
                    if (gameMap[i].getStoneColor() == players[currentPlayer].getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                if (jumps[i] >= counter) {
                    if (players[currentPlayer].getColor() == PLAYER_COLOR_BLACK) {
                        startNumber = counter - 1;
                        targetNumber = 26;
                    } else {
                        startNumber = 24 - counter;
                        targetNumber = 27;
                    }
                    try {
                        if (gameMap[startNumber].getNumberStones() > 0) {
                            takeOutStone(startNumber, targetNumber);
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
        setStatus(String.format(PLAYER_S_IT_S_YOUR_TURN, players[currentPlayer]));
    }

    // this function works with gui - that's why the tui
    // doesn't work now
    // called in startRound() and works with BackgammonFrame bf
    // (which is actually the gui).
    // There is similar function for the tui,
    // but I don't know how to make them work parallel
    private boolean notGetStartAndTargetNumbers() {
        currentMehtodName = "getStartAndTargetNumbers";

        if (players[currentPlayer].getColor() == PLAYER_COLOR_WHITE && gameMap[FIELD_EATEN_WHITE].getNumberStones() > 0) { //white eaten

            // TO ADD: check if is possible to play turn
            startNumber = FIELD_EATEN_WHITE;
            // get targetNumber
            result = -1;
            targetNumber = getTargetWhileEatenWhite();
        } else if (players[currentPlayer].getColor() == PLAYER_COLOR_BLACK && gameMap[FIELD_EATEN_BLACK].getNumberStones() > 0) { //black eaten

            startNumber = FIELD_EATEN_BLACK;

            // get targetNumber
            result = -1;
            targetNumber = getTargetWhileEatenBlack();
        } else if (endPhase) { //endphase
            // get startNumber
            result = -1;
            getStartNumber();

            // get targetNumber
            result = -1;
            getEndNumber();

            // check direction
            if (targetNumber < 24 && isNotCheckDirection()) {
                return true;
            }
        } else { //normal turn

            // get startNumber
            result = -1;
            getStartNumber();

            if (checkAllStartnumbersValidness()) {
                setStatus(YOU_CAN_NOT_PLAY_WITH_THIS_STONE);
                return true;
            }

            // get targetNumber
            result = -1;
            getEndNumber();

            // check direction
            if (targetNumber < 24 && isNotCheckDirection()) {
                return true;
            }
        }

        setStatus(String.format(STRING1, startNumber, targetNumber));
        return false;
    }

    // private STATE s = STATE_UNDEFINED;
	/*
		initialize game

		game starts: this.s = STATE_NO_MOVE;

		initialize round

		first click: this.s = STATE_FIRST_CLICK;

		second click this.s = STATE_FIRST_MOVE;
	*/

    /*
     * Turn logic
     */
    private void startRound() {
        changeCurrentPlayer();
        checkEndPhase();

        // i don't know how it works, but don't remove it - here was the problem

        // setJumps(rollTheDice());

        // automatic takeout for taking out stones automatically if its possible
        // useful in endphase or when stone is eaten and has to be returned back
        // to desk
        final int index = automaticTakeOut();

        // Actual "turn" - one player has 0 to 4 turns per round
        for (int i = index; i < getTurnsNumber(); i++) {
            // check for winner
            if (checkForWinner()) {
                setStatus(String.format(PLAYER_S_IS_THE_WINNER, players[currentPlayer]));
                winner = players[currentPlayer].getColor() + 1;
                return;
            }

            checkEndPhase();

            // check if it possible to make move
            if (checkIfMoveImpossible()) { // noMovesDialog();
                //System.out.println("no moves available");
                return;
            }

            // getting start and target number (for now from GUI! - has to be
            // parallelised)
            // if no numbers got - try one more time
            if (notGetStartAndTargetNumbers()) {
                startNumber = -1;
                i--;
                continue;
            }

            // change gameMap (Move, take out or eat another stone)
            setGameMap(doSomethingWithStones());
            renewJumps(startNumber, targetNumber);
            startNumber = -1;
            targetNumber = -1;

            // check for winner
            if (checkForWinner()) {
                setStatus(String.format(PLAYER_S_IS_THE_WINNER, players[currentPlayer].toString()));
                winner = players[currentPlayer].getColor() + 1;
                return;
            }
        }

        notifyObservers();
    }

    private int getTargetWhileEatenWhite() {
        try {
            while (result == -1 || result > 6) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose(e);
        }

        return result;
    }

    private int getTargetWhileEatenBlack() {
        try {
            while (result >= FIELD_EATEN_BLACK || result < 18) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose("getTargetWhileEatenBlack");
            Log.verbose(e);
        }

        return result;
    }

    private void getStartNumber() {

        int localStartNumber = result;
        try {

            while (localStartNumber < 0 || localStartNumber >= FIELD_EATEN_BLACK) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                localStartNumber = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {

            Log.verbose(e);
        }

        if (checkStartNumber(localStartNumber)) {
            startNumber = localStartNumber;
        } else {
            System.err.println("You can't move this piece or there are no pieces");
            result = -1;
            getStartNumber();
        }
    }

    private void getEndNumber() {

        int localTargetNumber = result;
        try {

            localTargetNumber = getTargetResult(localTargetNumber);

            if (localTargetNumber >= 0 && localTargetNumber < FIELD_EATEN_BLACK) {

                checkColorProblem(localTargetNumber);
                checkTargetValidness(localTargetNumber);
            } else if (localTargetNumber == FIELD_END_BLACK) {
                checkEndphaseBlack();
            } else if (localTargetNumber == FIELD_END_WHITE) {
                checkEndphaseWhite();
            }
        } catch (Exception e) {
            Log.verbose("getEndNumber");
            Log.verbose(e);
        }
    }

    private int getTargetResult(final int oldTarget) {

        int newTarget = oldTarget;
        try {
            while (newTarget < 0 || newTarget == FIELD_EATEN_BLACK || newTarget == FIELD_EATEN_WHITE || newTarget == startNumber || (newTarget == FIELD_END_BLACK || newTarget == FIELD_END_WHITE) && !endPhase) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                newTarget = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose("getTargetResult");
            Log.verbose(e);
        }
        return newTarget;
    }

    private void checkColorProblem(final int targetNumber) {
        if (gameMap[targetNumber].isNotJumpable(players[currentPlayer].getColor())) {
            System.err.println(STRING2);
            result = -1;
            getEndNumber();//or get startNumber()?
        }
    }

    private void checkTargetValidness(final int targetNumber) {
        if (checkNormalEndTarget(targetNumber)) {
            this.targetNumber = targetNumber;
        } else {
            result = -1;
            getEndNumber();//or get startNumber()?
        }
    }

    private void checkEndphaseBlack() {
        // endphase black
        if (checkEndphaseBlackTarget()) {
            targetNumber = FIELD_END_BLACK;
        } else {
            result = -1;
            getEndNumber();//or get startNumber()?
        }
    }

    private void checkEndphaseWhite() {
        // endphase white
        if (checkEndphaseWhiteTarget(FIELD_END_WHITE)) {
            targetNumber = FIELD_END_WHITE;
        } else {
            result = -1;
            getEndNumber();//or get startNumber()?
        }
    }
}
