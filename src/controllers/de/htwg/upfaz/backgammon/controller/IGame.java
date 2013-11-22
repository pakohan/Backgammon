package controllers.de.htwg.upfaz.backgammon.controller;

public interface IGame {

    public static final int MAX_JUMPS = 4;
    public static final int DICE_RANDOM = 6;

    public void setStatus(String s);

    public void setDice(final Dice dice);
}
