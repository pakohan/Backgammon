package de.htwg.upfaz.backgammon.controller;

import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.upfaz.backgammon.entities.Player;
import de.htwg.util.observer.Observable;

import java.util.Arrays;
import java.util.Random;

public final class Game
        extends Observable
        implements IGame {

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

    private Field[] gameMap;
    private final IPlayer player1;
    private final IPlayer player2;

    private int winner;
    private boolean endPhase;
    private int[] jumps;
    private int[] jumpsT;
    private int startNumber = -1;
    private int targetNumber = -1;

    private String status = "Let's the game begin!";
    private String currentMehtodName = "Begin";
    private IPlayer currentPlayer;

    //
    // Constructors
    //
    public Game() {
        gameMap = new Field[TOTAL_FIELDS_NR];

        for (int i = 0; i < gameMap.length; i++) {
            gameMap[i] = new Field(i);
        }
        player1 = new Player(0);
        player2 = new Player(1);
        initStones(gameMap);
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

    @Override
    public void setGameMap(final Field[] newGameMap) {

        if (newGameMap == null) {
            gameMap = new Field[0];
        } else {
            gameMap = Arrays.copyOf(newGameMap, newGameMap.length);
        }
    }

    @Override
    public Field[] getGameMap() {
        return gameMap;
    }

    @Override
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
        setStatus(String.format("So, youre moves are: %d, %d", jumpsToReturn[0], jumpsToReturn[1]));
        setStatus("");
        return jumpsToReturn;
    }

    private Field[] eatStone(final Field[] gameMap, final IPlayer plr, final int startNumber, final int targetNumber) {
        currentMehtodName = "eatStone";
        gameMap[targetNumber].setNumberStones(1);
        gameMap[targetNumber].setStoneColor(plr.getColor());
        if (plr.getColor() == 0) {
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

    Field[] moveStone(final Field[] gameMap, final IPlayer plr, final int startNumber, final int targetNumber) {
        currentMehtodName = "moveStone";
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[targetNumber].setStoneColor(plr.getColor());
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
        setStatus("Moving the stone");
        return gameMap;
    }

    @Override
    public boolean checkForWinner(final IField[] gameMap) {
        // if (gameMap[FIELD_END_BLACK].getNumberStones() == 15
        // || gameMap[FIELD_END_WHITE].getNumberStones() == 15) {
        // return true;
        // } else {
        // return false;
        // }

        return gameMap[FIELD_END_BLACK].getNumberStones() == STONES_TO_WIN || gameMap[FIELD_END_WHITE].getNumberStones() == STONES_TO_WIN;
    }

    Field[] takeOutStone(final Field[] gameMap, final int startNumber, final int targetNumber) {
        currentMehtodName = "takeOutStone";
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
        setStatus("Taking out the stone");
        return gameMap;
    }

    @Override
    public Field[] doSomethingWithStones(final Field[] gm, final IPlayer plr, final int startNumber, final int targetNumber, final boolean endPhase) {
        currentMehtodName = "doSomethingWithStones";
        setGameMap(gm);

        // eat or move stone
        if (gameMap[targetNumber].getNumberStones() == 1 && gameMap[targetNumber].getStoneColor() != plr.getColor()) {

            gameMap = eatStone(gameMap, plr, startNumber, targetNumber);
        } else if (endPhase && this.targetNumber > 25) {

            gameMap = takeOutStone(gameMap, startNumber, targetNumber);
        } else {

            gameMap = moveStone(gameMap, plr, startNumber, targetNumber);
        }
        return gameMap;
    }

    @Override
    public int getStartNumber() {
        return startNumber;
    }

    @Override
    public void setStartNumber(final int number) {
        startNumber = number;
    }

    @Override
    public int getTargetNumber() {
        return targetNumber;
    }

    @Override
    public void setTargetNumber(final int number) {
        targetNumber = number;
    }

    @Override
    public boolean notCheckDirection(final IPlayer plr) {
        if (plr.getColor() == 0 && targetNumber <= startNumber || plr.getColor() == 1 && targetNumber >= startNumber) {
            setStatus("You're going in the wrong direction!");
            return true;
        }
        return false;
    }

    @Override
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
                setStatus("Setting jumps[" + i + "] to 0");
                break; //TODO check if break also stops for loop
            }
        }
    }

    int calcStoneInEndPhase(final IPlayer plr, final IField[] gm) {
        currentMehtodName = "calcStoneInEndPhase";
        int stonesInEndPhase = 0;
        if (plr.getColor() == 0) {
            for (int i = 18; i <= 23; i++) {
                if (gm[i].getStoneColor() == plr.getColor()) {
                    stonesInEndPhase += gm[i].getNumberStones();
                }
            }
            stonesInEndPhase += gm[FIELD_END_WHITE].getNumberStones();
        } else {
            for (int i = 5; i >= 0; i--) {
                if (gm[i].getStoneColor() == plr.getColor()) {
                    stonesInEndPhase += gm[i].getNumberStones();
                }
            }
            stonesInEndPhase += gm[FIELD_END_BLACK].getNumberStones();
        }

        return stonesInEndPhase;
    }

    @Override
    public void setJumps(final int[] newJumps) {

        if (newJumps == null) {
            jumps = new int[0];
        } else {
            jumps = Arrays.copyOf(newJumps, newJumps.length);
        }
    }

    @Override
    public int[] getJumps() {
        return jumps;
    }

    @Override
    public int getWinner() {
        return winner;
    }

    @Override
    public IPlayer getPlayer1() {
        return player1;
    }

    @Override
    public IPlayer getPlayer2() {
        return player2;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setCurrentPlayer(final IPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(final String s) {
        status = s;
        notifyObservers();
    }

    @Override
    public boolean checkStartNumber(final int number) {
        currentMehtodName = "checkStartNumber";
        try {
            return gameMap[number].getStoneColor() == currentPlayer.getColor();
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public void printJumpsStatus(final int[] jumps) {

        setStatus(String.format("So, youre moves are: %d, %d, %d, %d", jumps[0], jumps[1], jumps[2], jumps[3]));
    }

    @Override
    public String printJumpsString() {

        return String.format("Jumps: %d, %d, %d, %d", jumps[0], jumps[1], jumps[2], jumps[3]);
    }

    @Override
    public int[] getJumpsT() {
        return jumpsT;
    }

    @Override
    public void setJumpsT(final int[] j) {
        jumpsT = new int[2];
        jumpsT[0] = j[0];
        jumpsT[1] = j[1];
    }

    @Override
    public boolean eatenWhiteCheck(final int target) {
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

    @Override
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

    @Override
    public boolean checkEndphaseWhiteTarget(final int newTarget) {
        currentMehtodName = "checkEndphaseWhiteTarget";
        // if ((Math.abs(targetNumber - currentGame.getStartNumber()
        // - 3)) == currentGame.jumps[0]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[1]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[2]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[3]) {
        // }

        boolean returnVal = false;

        for (int i = 0; i < MAX_JUMPS; i++) {
            returnVal = Math.abs(newTarget - startNumber - 3) <= jumps[i];
            if (returnVal) {
                break;
            }
        }

        return returnVal;
    }

    @Override
    public boolean checkEndphaseBlackTarget() {
        currentMehtodName = "checkEndphaseBlackTarget";
        // if (currentGame.jumps[0] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[1] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[2] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[3] == (currentGame.getStartNumber() + 1)) {
        //
        // }
        boolean returnVal = false;

        for (int i = 0; i < 4; i++) {
            returnVal = jumps[i] >= startNumber + 1;
            if (returnVal) {
                break;
            }
        }

        return returnVal;
    }

    @Override
    public boolean notCheckStartValidnessLoop() {
        currentMehtodName = "checkStartValidnessLoop";
        boolean toReturn = true;
        for (int i = 0; i < 4; i++) {
            if (jumps[i] != 0 && checkStartValidness(i)) {
                toReturn = false;
            }
        }

        return toReturn;
    }

    private boolean checkStartValidness(final int i) {
        currentMehtodName = "checkStartValidness";

        try {
            if (currentPlayer.getColor() == 0 && (startNumber + jumps[i] > 23 || gameMap[startNumber + jumps[i]].isNotJumpable(currentPlayer.getColor()))) {
                return false;
            } else if (currentPlayer.getColor() == 1 && (startNumber - jumps[i] < 0 || gameMap[startNumber - jumps[i]].isNotJumpable(currentPlayer.getColor()))) {
                return false;
            }
        } catch (Exception e) {
            System.err.printf("checkStartValidness: %s%n", e);
        }
        return true;
    }

    @Override
    public String getCurrentMethodName() {
        return currentMehtodName;
    }

    @Override
    public void setCurrentMethodName(final String newName) {
        currentMehtodName = newName;
    }

    @Override
    public boolean isEndPhase() {
        return endPhase;
    }

    @Override
    public void setEndPhase(final boolean endPhase) {
        this.endPhase = endPhase;
    }

    @Override
    public void setWinner(final int winner) {
        this.winner = winner;
    }

    @Override
    public int getTurnsNumber() {
        int returnVal = 4;

        if (jumps[3] == 0) {
            returnVal = 2;
        }

        return returnVal;
    }

    @Override
    public void checkEndPhase() {
        if (calcStoneInEndPhase(currentPlayer, gameMap) == STONES_TO_WIN) {
            endPhase = true;
            setStatus("End Phase!");
        }
    }

    @Override
    public boolean notCheckIfMovesPossible() {

        boolean toReturn = true;

        for (int i = 0; i < 24; i++) {
            if (gameMap[i].getStoneColor() == currentPlayer.getColor()) {
                startNumber = i;
                if (!notCheckStartValidnessLoop()) {

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

    @Override
    public int automaticTakeOut() {
        int toReturn = 0;
        if (endPhase) {

            int counter = 6;
            if (currentPlayer.getColor() == 1) { // black

                for (int i = 5; i >= 0; i--) {
                    if (gameMap[i].getStoneColor() == currentPlayer.getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            } else { // white

                for (int i = 18; i <= 23; i++) {
                    if (gameMap[i].getStoneColor() == currentPlayer.getColor()) {
                        break;
                    } else {
                        counter--;
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                if (jumps[i] >= counter) {
                    if (currentPlayer.getColor() == 1) {
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

    @Override
    public String toString() {
        return String.format("Game{gameMap=%s, player1=%s, player2=%s, winner=%d, endPhase=%s, jumps=%s, jumpsT=%s, startNumber=%d, targetNumber=%d, status='%s', currentMehtodName='%s', currentPlayer=%s}", Arrays.toString(gameMap), player1, player2, winner, endPhase, Arrays.toString(jumps), Arrays.toString(jumpsT), startNumber, targetNumber, status, currentMehtodName, currentPlayer);
    }
}
