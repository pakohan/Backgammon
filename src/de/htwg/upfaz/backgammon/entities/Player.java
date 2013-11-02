package de.htwg.upfaz.backgammon.entities;

public class Player
        implements IPlayer {

    private int color;

    public Player(int clr) {
        color = clr;
    }

    @Override
    public void setColor(int newVar) {
        color = newVar;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        if (color == 0) {
            return "White";
        } else {
            return "Black";
        }
    }
}
