package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.entities.Player;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;
import controllers.de.htwg.upfaz.backgammon.gui.Log;
import controllers.de.htwg.util.observer.IObservable;
import controllers.de.htwg.util.observer.Observable;

public final class Game
        extends Observable
        implements IObservable, Runnable, IGame {

    private static final int STONES_TO_WIN = 15;

    private final IPlayer[] players;
    private GameMap gameMap;

    private int winner;
    private boolean endPhase;
    private Dice dice = new Dice(0);
    private State state;

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

        gameMap = new GameMap();
        players = new IPlayer[2];
        players[0] = new Player(0);
        players[1] = new Player(1);
        currentPlayer = 1;
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

    public int getDiceAt(final int i) {
        return dice.getDiceAt(i);
    }

    @Override
    public String toString() {
        return String.format("start = %d, target = %d, result = %d; Current player: %s; Method: %s", startNumber, targetNumber, result, getCurrentPlayer(), currentMehtodName);
    }

    private boolean checkForWinner() {
        return gameMap.getField(GameMap.FIELD_END_BLACK).getNumberStones() == STONES_TO_WIN || gameMap.getField(GameMap.FIELD_END_WHITE).getNumberStones() == STONES_TO_WIN;
    }


    /* Move, take out or eat another stone. */
    private void doSomethingWithStones() {
        currentMehtodName = "doSomethingWithStones";

        if (gameMap.getField(targetNumber).getNumberStones() == 1 && gameMap.getField(targetNumber).getStoneColor() != players[currentPlayer].getColor()) {

            gameMap.eatStone(startNumber, targetNumber, players[currentPlayer]);
        } else if (endPhase && targetNumber > GameMap.FIELD_EATEN_WHITE) {

            gameMap.takeOutStone(startNumber, targetNumber);
        } else {

            gameMap.moveStone(startNumber, targetNumber, players[currentPlayer]);
        }
    }

    private boolean isNotCheckDirection() {
        if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_WHITE && targetNumber <= startNumber || players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_BLACK && targetNumber >= startNumber) {
            setStatus("You're going in the wrong direction!");
            return true;
        }
        return false;
    }

    private void renewJumps(final int start, final int target) {
        currentMehtodName = "move";
        if (target >= GameMap.FIELD_END_BLACK) {
            dice.renewJumpsEndPhase(start);
        } else {
            dice.move(Math.abs(start - target));
        }
    }

    @Override
    public void setDice(final Dice dice) {
        this.dice = dice;
    }

    private boolean checkAllStartnumbersValidness() {
        currentMehtodName = "checkAllStartnumbersValidness";
        boolean toReturn = true;
        for (int i = 0; i < 4; i++) {
            if (dice.getDiceAt(i) != 0 && !((players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_WHITE && (startNumber + dice.getDiceAt(i) > 23 || gameMap.getField(startNumber + dice.getDiceAt(i)).isNotJumpable(players[currentPlayer].getColor()))) || (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_BLACK && (startNumber - dice.getDiceAt(i) < 0 || gameMap.getField(startNumber - dice.getDiceAt(i)).isNotJumpable(players[currentPlayer].getColor()))))) {
                toReturn = false;
            }
        }

        return toReturn;
    }

    private void checkEndPhase() {
        int stonesInEndPhase = 0;
        if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_WHITE) {
            for (int i = 18; i <= 23; i++) { // Fields 18-23 for white player
                // are endphase fields
                if (gameMap.getField(i).getStoneColor() == players[currentPlayer].getColor()) {
                    stonesInEndPhase += gameMap.getField(i).getNumberStones();
                }
            }
            stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_WHITE).getNumberStones();
        } else {
            for (int i = 5; i >= 0; i--) { // Fields 0-5 for black player are
                // endphase fields
                if (gameMap.getField(i).getStoneColor() == players[currentPlayer].getColor()) {
                    stonesInEndPhase += gameMap.getField(i).getNumberStones();
                }
            }
            stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_BLACK).getNumberStones();
        }


        endPhase = stonesInEndPhase == STONES_TO_WIN;
        if (endPhase) {
            setStatus("End Phase!");
        }
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
    private boolean notGetStartAndTargetNumbers()
            throws InterruptedException {
        currentMehtodName = "getStartAndTargetNumbers";

        if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_WHITE && gameMap.getField(GameMap.FIELD_EATEN_WHITE).getNumberStones() > 0) { //white eaten

            // TO ADD: check if is possible to play turn
            startNumber = GameMap.FIELD_EATEN_WHITE;
            // get targetNumber
            result = -1;
            while (result == -1 || result > 6) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
            }
            targetNumber = result;
        } else if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_BLACK && gameMap.getField(GameMap.FIELD_EATEN_BLACK).getNumberStones() > 0) { //black eaten

            startNumber = GameMap.FIELD_EATEN_BLACK;

            // get targetNumber
            result = -1;
            while (result >= GameMap.FIELD_EATEN_BLACK || result < 18) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
            }
            targetNumber = result;
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

    private void startRound()
            throws InterruptedException {
        changeCurrentPlayer();
        checkEndPhase();

        dice = new Dice();

        // automatic takeout for taking out stones automatically if its possible
        // useful in endphase or when stone is eaten and has to be returned back
        // to desk
        if (endPhase) {

            int counter = 6;
            if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_BLACK) { // black

                for (int i = 5; i >= 0; i--) {
                    if (gameMap.getField(i).getStoneColor() == players[currentPlayer].getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            } else { // white

                for (int i = 18; i <= 23; i++) {
                    if (gameMap.getField(i).getStoneColor() == players[currentPlayer].getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                if (dice.getDiceAt(i) >= counter) {
                    if (players[currentPlayer].getColor() == GameMap.PLAYER_COLOR_BLACK) {
                        startNumber = counter - 1;
                        targetNumber = 26;
                    } else {
                        startNumber = 24 - counter;
                        targetNumber = 27;
                    }
                    try {
                        if (gameMap.getField(startNumber).getNumberStones() > 0) {
                            gameMap.takeOutStone(startNumber, targetNumber);
                            renewJumps(startNumber, targetNumber);
                        }
                    } catch (Exception ignored) {
                        return;
                    }
                }
            }
        }

        // Actual "turn" - one player has 0 to 4 turns per round
        while (dice.hasTurnsLeft()) {
            // check for winner
            if (checkForWinner()) {
                setStatus(String.format(PLAYER_S_IS_THE_WINNER, players[currentPlayer]));
                winner = players[currentPlayer].getColor() + 1;
                return;
            }

            checkEndPhase();


            for (int i = 0; i < 24; i++) {
                if (gameMap.getField(i).getStoneColor() == players[currentPlayer].getColor()) {
                    startNumber = i;
                    if (!checkAllStartnumbersValidness()) {
                        startNumber = -1;
                        return;
                    }
                }
            }

            if (endPhase) {
                startNumber = -1;
                return;
            }

            // getting start and target number (for now from GUI! - has to be
            // parallelised)
            // if no numbers got - try one more time
            if (notGetStartAndTargetNumbers()) {
                startNumber = -1;
                continue;
            }

            // change gameMap (Move, take out or eat another stone)
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

    private void getStartNumber() {

        int localStartNumber = result;
        try {

            while (localStartNumber < 0 || localStartNumber >= GameMap.FIELD_EATEN_BLACK) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                localStartNumber = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {

            Log.verbose(e);
        }

        if (gameMap.getField(localStartNumber).getStoneColor() == players[currentPlayer].getColor()) {
            startNumber = localStartNumber;
        } else {
            System.err.println("You can't move this piece or there are no pieces");
            result = -1;
            getStartNumber();
        }
    }

    private void getEndNumber() {
        try {

            while (result < 0 || result == GameMap.FIELD_EATEN_BLACK || result == GameMap.FIELD_EATEN_WHITE || result == startNumber || (result == GameMap.FIELD_END_BLACK || result == GameMap.FIELD_END_WHITE) && !endPhase) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
            }

            if (result >= 0 && result < GameMap.FIELD_EATEN_BLACK) {

                if (gameMap.getField(result).isNotJumpable(players[currentPlayer].getColor())) {
                    System.err.println(STRING2);
                    result = -1;
                    getEndNumber();//or get startNumber()?
                }

                if (dice.checkNormalEndTarget(Math.abs(result - startNumber))) {
                    this.targetNumber = result;
                } else {
                    result = -1;
                    getEndNumber();//or get startNumber()?
                }
            } else if (result == GameMap.FIELD_END_BLACK) {
                boolean returnVal = false;

                for (int i = 0; i < 4; i++) {
                    returnVal = returnVal || dice.getDiceAt(i) >= startNumber + 1;
                }

                if (returnVal) {
                    targetNumber = GameMap.FIELD_END_BLACK;
                } else {
                    result = -1;
                    getEndNumber();//or get startNumber()?
                }
            } else if (result == GameMap.FIELD_END_WHITE) {
                if (dice.checkNormalEndTarget(Math.abs(GameMap.FIELD_END_WHITE - startNumber - 3))) {
                    targetNumber = GameMap.FIELD_END_WHITE;
                } else {
                    result = -1;
                    getEndNumber();//or get startNumber()?
                }
            }
        } catch (Exception e) {
            Log.verbose("getEndNumber");
            Log.verbose(e);
        }
    }
}
