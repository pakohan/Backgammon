package de.htwg.upfaz.backgammon.controller;

import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.util.observer.IObservable;

public interface IGame
        extends IObservable {

    void setGameMap(Field[] newGameMap);

    Field[] getGameMap();

    int[] rollTheDice();

    // --Commented out by Inspection (02.11.13 18:14):public abstract IField[] moveStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber);

    boolean checkForWinner(IField[] gameMap);

    // --Commented out by Inspection (02.11.13 18:14):public abstract IField[] takeOutStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber);

    Field[] doSomethingWithStones(Field[] gm, IPlayer plr, int startNumber, int targetNumber, boolean endPhase);

    int getStartNumber();

    void setStartNumber(int number);

    int getTargetNumber();

    void setTargetNumber(int number);

    boolean isNotCheckDirection(IPlayer plr);

    void renewJumps(int start, int target);

    // --Commented out by Inspection (02.11.13 18:14):public abstract int calcStoneInEndPhase(IPlayer plr, IField[] gm);

    void setJumps(int[] newJumps);

    int[] getJumps();

    int getWinner();

    IPlayer getPlayer1();

    IPlayer getPlayer2();

    IPlayer getCurrentPlayer();

    void setCurrentPlayer(IPlayer currentPlayer);

    String getStatus();

    void setStatus(final String s);

    boolean checkStartNumber(int number);

    void printJumpsStatus(int[] jumps);

    String printJumpsString();

    int[] getJumpsT();

    void setJumpsT(int[] j);

    boolean isEatenWhiteCheck(int target);

    boolean checkNormalEndTarget(int newTarget);

    boolean checkEndphaseWhiteTarget(int newTarget);

    boolean checkEndphaseBlackTarget();

    boolean isValidStartLoop();

    String getCurrentMethodName();

    void setCurrentMethodName(String newName);

    boolean isEndPhase();

    void setEndPhase(boolean endPhase);

    void setWinner(int winner);

    int getTurnsNumber();

    void checkEndPhase();

    boolean checkIfMoveImpossible();

    int automaticTakeOut();
}
