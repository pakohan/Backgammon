package de.htwg.upfaz.backgammon.controller;

import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.util.observer.IObservable;

public interface IGame
        extends IObservable {

    public abstract void setGameMap(Field[] newGameMap);

    public abstract Field[] getGameMap();

    public abstract int[] rollTheDice();

    public abstract IField[] eatStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber);

    public abstract IField[] moveStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber);

    public abstract boolean checkForWinner(IField[] gameMap);

    public abstract IField[] takeOutStone(Field[] gameMap, IPlayer plr, int startNumber, int targetNumber);

    public abstract Field[] doSomethingWithStones(Field[] gm, IPlayer plr, int startNumber, int targetNumber, boolean endPhase);

    public abstract int getStartNumber();

    public abstract void setStartNumber(int number);

    public abstract int getTargetNumber();

    public abstract void setTargetNumber(int number);

    public abstract boolean checkDirection(IPlayer plr, int start, int target);

    public abstract void renewJumps(int start, int target);

    public abstract int calcStoneInEndPhase(IPlayer plr, IField[] gm);

    public abstract void setJumps(int[] newJumps);

    public abstract int[] getJumps();

    public abstract int getWinner();

    public abstract IPlayer getPlayer1();

    public abstract IPlayer getPlayer2();

    public abstract IPlayer getCurrentPlayer();

    public abstract void setCurrentPlayer(IPlayer currentPlayer);

    public abstract String getStatus();

    public abstract void setStatus(final String s);

    public abstract boolean checkStartNumber(int number);

    public abstract void printJumpsStatus(int[] jumps);

    public abstract String printJumpsString();

    public abstract int[] getJumpsT();

    public abstract void setJumpsT(int[] j);

    public abstract boolean eatenWhiteCheck(int target);

    public abstract boolean checkNormalEndTarget(int newTarget);

    public abstract boolean checkEndphaseWhiteTarget(int newTarget);

    public abstract boolean checkEndphaseBlackTarget(int newTarget);

    public abstract boolean checkStartValidnessLoop();

    public abstract String getCurrentMethodName();

    public abstract void setCurrentMethodName(String newName);

    public abstract boolean isEndPhase();

    public abstract void setEndPhase(boolean endPhase);

    public abstract void setWinner(int winner);

    public abstract int getTurnsNumber();

    public abstract void checkEndPhase();

    public abstract boolean checkIfMovesPossible();

    public abstract int automaticTakeOut();
}
