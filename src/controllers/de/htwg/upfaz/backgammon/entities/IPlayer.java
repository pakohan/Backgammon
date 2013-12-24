package controllers.de.htwg.upfaz.backgammon.entities;

public interface IPlayer {
    int getColor();

    String toString();

    void setCurrentPlayer(String s);
}
