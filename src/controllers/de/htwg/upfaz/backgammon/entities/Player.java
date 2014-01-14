package controllers.de.htwg.upfaz.backgammon.entities;

public final class Player
        implements IPlayer {

    private int color;

    public Player(final int clr) {
        color = clr;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        if (color == 0) {
            return "white";
        } else {
            return "black";
        }
    }
    public void setCurrentPlayer(final String s) {
//        throw new IllegalArgumentException("Do not call Player.setCurrentPlayer");
    }
    public void changeCurrentPlayer() {
//        throw new IllegalArgumentException("Do not call Player.changeCurrentPlayer");
    }
    public IPlayer getCurrentPlayer() {
        return this;
    }
}
