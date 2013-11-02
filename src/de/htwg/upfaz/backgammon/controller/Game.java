package de.htwg.upfaz.backgammon.controller;

import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.upfaz.backgammon.entities.Player;
import de.htwg.util.observer.Observable;

import java.util.Arrays;
import java.util.Random;

public class Game
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
    private IPlayer player1;
    private IPlayer player2;

    private int winner = 0;
    private boolean endPhase = false;
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

    private void initStones(IField[] gm) {

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
    public void setGameMap(Field[] newGameMap) {

        if (newGameMap == null) {
            this.gameMap = new Field[0];
        } else {
            this.gameMap = Arrays.copyOf(newGameMap, newGameMap.length);
        }
    }

    @Override
    public Field[] getGameMap() {
        return gameMap;
    }

    @Override
    public int[] rollTheDice() {
        int[] jumpsToReturn = new int[MAX_JUMPS];
        Random dice = new Random();
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
        setStatus("So, youre moves are: " + jumpsToReturn[0] + ", " + jumpsToReturn[1]);
        setStatus("");
        return jumpsToReturn;
    }

    @Override
    public Field[] eatStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber) {
        setCurrentMethodName("eatStone");
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

    @Override
    public Field[] moveStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber) {
        setCurrentMethodName("moveStone");
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
    public boolean checkForWinner(IField[] gameMap) {
        // if (gameMap[FIELD_END_BLACK].getNumberStones() == 15
        // || gameMap[FIELD_END_WHITE].getNumberStones() == 15) {
        // return true;
        // } else {
        // return false;
        // }

        return (gameMap[FIELD_END_BLACK].getNumberStones() == STONES_TO_WIN || gameMap[FIELD_END_WHITE].getNumberStones() == STONES_TO_WIN);
    }

    @Override
    public Field[] takeOutStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber) {
        setCurrentMethodName("takeOutStone");
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
        setStatus("Taking out the stone");
        return gameMap;
    }

    @Override
    public Field[] doSomethingWithStones(Field[] gm, IPlayer plr, int startNumber, int targetNumber, boolean endPhase) {
        setCurrentMethodName("doSomethingWithStones");
        setGameMap(gm);

        // eat or move stone
        if (gameMap[targetNumber].getNumberStones() == 1 && gameMap[targetNumber].getStoneColor() != plr.getColor()) {

            gameMap = eatStone(gameMap, plr, startNumber, targetNumber);
        } else if (endPhase && getTargetNumber() > 25) {

            gameMap = takeOutStone(gameMap, plr, startNumber, targetNumber);
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
    public void setStartNumber(int number) {
        startNumber = number;
    }

    @Override
    public int getTargetNumber() {
        return targetNumber;
    }

    @Override
    public void setTargetNumber(int number) {
        targetNumber = number;
    }

    @Override
    public boolean checkDirection(IPlayer plr, int start, int target) {
        if ((plr.getColor() == 0 && targetNumber <= startNumber) || (plr.getColor() == 1 && targetNumber >= startNumber)) {
            setStatus("You're going in the wrong direction!");
            return false;
        }
        return true;
    }

    @Override
    public void renewJumps(int start, int target) {
        setCurrentMethodName("renewJumps");
        if (target >= FIELD_END_BLACK) {
            renewJumpsEndPhase(start);
            return;
        }
        for (int i = 0; i < MAX_JUMPS; i++) {
            if (Math.abs(target - start) == jumps[i]) {
                jumps[i] = 0;
                setStatus("Setting jumps[" + i + "] to 0");
                return;
            }
        }
    }

    private void renewJumpsEndPhase(int start) {
        for (int i = 0; i < MAX_JUMPS; i++) {
            if ((24 - start) == jumps[i] || (start + 1) == jumps[i]) {
                jumps[i] = 0;
                setStatus("Setting jumps[" + i + "] to 0");
                return;
            }
        }
    }

    @Override
    public int calcStoneInEndPhase(IPlayer plr, IField[] gm) {
        setCurrentMethodName("calcStoneInEndPhase");
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
    public void setJumps(int[] newJumps) {

        if (newJumps == null) {
            this.jumps = new int[0];
        } else {
            this.jumps = Arrays.copyOf(newJumps, newJumps.length);
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
    public void setCurrentPlayer(IPlayer currentPlayer) {
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
    public boolean checkStartNumber(int number) {
        setCurrentMethodName("checkStartNumber");
        try {
            return gameMap[number].getStoneColor() == currentPlayer.getColor();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void printJumpsStatus(int[] jumps) {

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
    public void setJumpsT(int[] j) {
        jumpsT = new int[2];
        jumpsT[0] = j[0];
        jumpsT[1] = j[1];
    }

    @Override
    public boolean eatenWhiteCheck(int target) {

        for (int i = 0; i < MAX_JUMPS; i++) {
            if (target == (jumps[i] - 1)) {
                jumps[i] = 0;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkNormalEndTarget(int newTarget) {

        for (int i = 0; i < MAX_JUMPS; i++) {
            if ((Math.abs(newTarget - getStartNumber())) == jumps[i]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkEndphaseWhiteTarget(int newTarget) {
        setCurrentMethodName("checkEndphaseWhiteTarget");
        // if ((Math.abs(targetNumber - currentGame.getStartNumber()
        // - 3)) == currentGame.jumps[0]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[1]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[2]
        // || (Math.abs(targetNumber
        // - currentGame.getStartNumber() - 3)) == currentGame.jumps[3]) {
        // }

        for (int i = 0; i < MAX_JUMPS; i++) {
            if ((Math.abs(newTarget - getStartNumber() - 3)) <= jumps[i]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkEndphaseBlackTarget(int newTarget) {
        setCurrentMethodName("checkEndphaseBlackTarget");
        // if (currentGame.jumps[0] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[1] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[2] == (currentGame.getStartNumber() + 1)
        // || currentGame.jumps[3] == (currentGame.getStartNumber() + 1)) {
        //
        // }

        for (int i = 0; i < 4; i++) {
            if (jumps[i] >= (getStartNumber() + 1)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkStartValidnessLoop() {
        boolean toReturn = false;
        setCurrentMethodName("checkStartValidnessLoop");
        for (int i = 0; i < 4; i++) {
            if (jumps[i] != 0 && checkStartValidness(i)) {
                toReturn = true;
            }
        }

        return toReturn;
    }

    private boolean checkStartValidness(int i) {
        setCurrentMethodName("checkStartValidness");
        try {
            if (getCurrentPlayer().getColor() == 0 && ((getStartNumber() + jumps[i]) > 23 || !gameMap[getStartNumber() + jumps[i]].isJumpable(getCurrentPlayer().getColor()))) {
                return false;
            } else if (getCurrentPlayer().getColor() == 1 && ((getStartNumber() - jumps[i]) < 0 || !gameMap[getStartNumber() - jumps[i]].isJumpable(getCurrentPlayer().getColor()))) {
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
    public void setCurrentMethodName(String newName) {
        currentMehtodName = newName;
    }

    @Override
    public boolean isEndPhase() {
        return endPhase;
    }

    @Override
    public void setEndPhase(boolean endPhase) {
        this.endPhase = endPhase;
    }

    @Override
    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public int getTurnsNumber() {
        if (getJumps()[3] == 0) {
            return 2;
        } else {
            return 4;
        }
    }

    @Override
    public void checkEndPhase() {
        if (calcStoneInEndPhase(getCurrentPlayer(), getGameMap()) == STONES_TO_WIN) {
            setEndPhase(true);
            setStatus("End Phase!");
        }
    }

    @Override
    public boolean checkIfMovesPossible() {

        boolean toReturn = false;

        for (int i = 0; i < 24; i++) {
            if (getGameMap()[i].getStoneColor() == getCurrentPlayer().getColor()) {
                setStartNumber(i);
                if (checkStartValidnessLoop()) {

                    toReturn = true;
                }
            }
        }

        if (isEndPhase()) {
            toReturn = true;
        }

        setStartNumber(-1);
        return toReturn;
    }

    @Override
    public int automaticTakeOut() {
        int counter = 6;
        int toReturn = 0;
        if (isEndPhase()) {

            if (getCurrentPlayer().getColor() == 1) { // black

                for (int i = 5; i >= 0; i--) {
                    if (getGameMap()[i].getStoneColor() != getCurrentPlayer().getColor()) {
                        counter--;
                    } else {
                        break;
                    }
                }
            } else { // white

                for (int i = 18; i <= 23; i++) {
                    if (getGameMap()[i].getStoneColor() != getCurrentPlayer().getColor()) {
                        counter--;
                    } else {
                        break;
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                if (getJumps()[i] >= counter) {
                    if (getCurrentPlayer().getColor() == 1) {
                        setStartNumber(counter - 1);
                        setTargetNumber(26);
                    } else {
                        setStartNumber(24 - counter);
                        setTargetNumber(27);
                    }
                    try {
                        if (getGameMap()[getStartNumber()].getNumberStones() > 0) {
                            takeOutStone(getGameMap(), getCurrentPlayer(), getStartNumber(), getTargetNumber());
                            renewJumps(getStartNumber(), getTargetNumber());
                            toReturn++;
                        }
                    } catch (Exception e) {
                        return 0;
                    }
                }
            }
        }

        return toReturn;
    }
}
