package de.htwg.upfaz.backgammon.entities;

import de.htwg.util.observer.ResourceBundle;

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
        if (0 == color) {
            return ResourceBundle.getString("white");
        } else {
            return ResourceBundle.getString("black");
        }
    }
}
