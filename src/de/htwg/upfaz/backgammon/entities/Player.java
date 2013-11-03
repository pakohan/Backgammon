package de.htwg.upfaz.backgammon.entities;

public final class Player
        implements IPlayer {

    private int color;

    public Player(final int clr) {
        color = clr;
    }

    @Override
    public void setColor(final int newVar) {
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
