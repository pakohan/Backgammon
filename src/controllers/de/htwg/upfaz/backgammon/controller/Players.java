package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.entities.Player;

public final class Players implements IPlayer {

    public static final int PLAYER_COLOR_WHITE = 0;
    public static final int PLAYER_COLOR_BLACK = 1;
    private final IPlayer[] players;
    private int currentPlayer;

    public Players(final IGame game /*will be used later*/) {
        players = new IPlayer[2];
        players[0] = new Player(0);
        players[1] = new Player(1);
        currentPlayer = 1;
    }

    public IPlayer getCurrentPlayer() {
        return players[currentPlayer];
    }

    public void changeCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
        }
    }

    @Override
    public int getColor() {
        return players[currentPlayer].getColor();
    }
}
