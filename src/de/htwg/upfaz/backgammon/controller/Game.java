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

    private void initStones(final IField[] gm) {

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

        if (null == newGameMap) {
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
        for (int index = 0; 2 > index; index++) {
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
        setStatus("So, youre moves are: " + jumpsToReturn[0] + ", " + jumpsToReturn[1]);
        setStatus("");
        return jumpsToReturn;
    }

    private Field[] eatStone(final Field[] gameMap, final IPlayer plr, final int startNumber, final int targetNumber) {
        currentMehtodName = "eatStone";
        gameMap[targetNumber].setNumberStones(1);
        gameMap[targetNumber].setStoneColor(plr.getColor());
        if (0 == plr.getColor()) {
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
        if (0 == gameMap[startNumber].getNumberStones()) {
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

        return STONES_TO_WIN == gameMap[FIELD_END_BLACK].getNumberStones() || STONES_TO_WIN == gameMap[FIELD_END_WHITE].getNumberStones();
    }

    Field[] takeOutStone(final Field[] gameMap, final int startNumber, final int targetNumber) {
        currentMehtodName = "takeOutStone";
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (0 == gameMap[startNumber].getNumberStones()) {
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
        if (1 == gameMap[targetNumber].getNumberStones() && gameMap[targetNumber].getStoneColor() != plr.getColor()) {

            gameMap = eatStone(gameMap, plr, startNumber, targetNumber);
        } else if (endPhase && 25 < getTargetNumber()) {

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
        if (0 == plr.getColor() && targetNumber <= startNumber || 1 == plr.getColor() && targetNumber >= startNumber) {
            setStatus("You're going in the wrong direction!");
            return true;
        }
        return false;
    }

    @Override
    public void renewJumps(final int start, final int target) {
        currentMehtodName = "renewJumps";
        if (FIELD_END_BLACK <= target) {
            renewJumpsEndPhase(start);
            return;
        }
        for (int i = 0; MAX_JUMPS > i; i++) {
            if (Math.abs(target - start) == jumps[i]) {
                jumps[i] = 0;
                setStatus("Setting jumps[" + i + "] to 0");
                return;
            }
        }
    }

    private void renewJumpsEndPhase(final int start) {
        for (int i = 0; MAX_JUMPS > i; i++) {
            if (24 - start == jumps[i] || start + 1 == jumps[i]) {
                jumps[i] = 0;
                setStatus("Setting jumps[" + i + "] to 0");
                return;
            }
        }
    }

    int calcStoneInEndPhase(final IPlayer plr, final IField[] gm) {
        currentMehtodName = "calcStoneInEndPhase";
        int stonesInEndPhase = 0;
        if (0 == plr.getColor()) {
            for (int i = 18; 23 >= i; i++) {
                if (gm[i].getStoneColor() == plr.getColor()) {
                    stonesInEndPhase += gm[i].getNumberStones();
                }
            }
            stonesInEndPhase += gm[FIELD_END_WHITE].getNumberStones();
        } else {
            for (int i = 5; 0 <= i; i--) {
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

        if (null == newJumps) {
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
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void printJumpsStatus(final int[] jumps) {

        setStatus("So, youre moves are: " + jumps[0] + ", " + jumps[1] + ", " + jumps[2] + ", " + jumps[3]);
    }

    @Override
    public String printJumpsString() {

        return "Jumps: " + jumps[0] + ", " + jumps[1] + ", " + jumps[2] + ", " + jumps[3];
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

        for (int i = 0; MAX_JUMPS > i; i++) {
            if (target == jumps[i] - 1) {
                jumps[i] = 0;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkNormalEndTarget(final int newTarget) {

        for (int i = 0; MAX_JUMPS > i; i++) {
            if (Math.abs(newTarget - getStartNumber()) == jumps[i]) {
                return true;
            }
        }

        return false;
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

        for (int i = 0; MAX_JUMPS > i; i++) {
            if (Math.abs(newTarget - getStartNumber() - 3) <= jumps[i]) {
                return true;
            }
        }

        return false;
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

        for (int i = 0; 4 > i; i++) {
            if (jumps[i] >= getStartNumber() + 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean notCheckStartValidnessLoop() {
        currentMehtodName = "checkStartValidnessLoop";
        boolean toReturn = true;
        for (int i = 0; 4 > i; i++) {
            if (0 != jumps[i] && checkStartValidness(i)) {
                toReturn = false;
            }
        }

        return toReturn;
    }

    private boolean checkStartValidness(final int i) {
        currentMehtodName = "checkStartValidness";
        try {
            if (0 == getCurrentPlayer().getColor() && (23 < getStartNumber() + jumps[i] || gameMap[getStartNumber() + jumps[i]].isNotJumpable(getCurrentPlayer().getColor()))) {
                return false;
            } else if (1 == getCurrentPlayer().getColor() && (0 > getStartNumber() - jumps[i] || gameMap[getStartNumber() - jumps[i]].isNotJumpable(getCurrentPlayer().getColor()))) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("checkStartValidness: " + e);
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
        if (0 == getJumps()[3]) {
            return 2;
        } else {
            return 4;
        }
    }

    @Override
    public void checkEndPhase() {
        if (STONES_TO_WIN == calcStoneInEndPhase(getCurrentPlayer(), getGameMap())) {
            endPhase = true;
            setStatus("End Phase!");
        }
    }

    @Override
    public boolean notCheckIfMovesPossible() {

        boolean toReturn = true;

        for (int i = 0; 24 > i; i++) {
            if (getGameMap()[i].getStoneColor() == getCurrentPlayer().getColor()) {
                startNumber = i;
                if (!notCheckStartValidnessLoop()) {

                    toReturn = false;
                }
            }
        }

        if (isEndPhase()) {
            toReturn = false;
        }

        startNumber = -1;
        return toReturn;
    }

    @Override
    public int automaticTakeOut() {
        int toReturn = 0;
        if (isEndPhase()) {

            int counter = 6;
            if (1 == getCurrentPlayer().getColor()) { // black

                for (int i = 5; 0 <= i; i--) {
                    if (getGameMap()[i].getStoneColor() != getCurrentPlayer().getColor()) {
                        counter--;
                    } else {
                        break;
                    }
                }
            } else { // white

                for (int i = 18; 23 >= i; i++) {
                    if (getGameMap()[i].getStoneColor() != getCurrentPlayer().getColor()) {
                        counter--;
                    } else {
                        break;
                    }
                }
            }

            for (int i = 0; 4 > i; i++) {
                if (getJumps()[i] >= counter) {
                    if (1 == getCurrentPlayer().getColor()) {
                        startNumber = counter - 1;
                        targetNumber = 26;
                    } else {
                        startNumber = 24 - counter;
                        targetNumber = 27;
                    }
                    try {
                        if (0 < getGameMap()[getStartNumber()].getNumberStones()) {
                            takeOutStone(getGameMap(), getStartNumber(), getTargetNumber());
                            renewJumps(getStartNumber(), getTargetNumber());
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
}
