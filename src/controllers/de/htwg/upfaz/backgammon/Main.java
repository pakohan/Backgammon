package controllers.de.htwg.upfaz.backgammon;

import controllers.de.htwg.upfaz.backgammon.controller.Game;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import controllers.de.htwg.upfaz.backgammon.gui.Tui;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        final Game currentGame = new Game();

        BackgammonFrame bf = new BackgammonFrame(currentGame);
        Tui tui = new Tui(currentGame);

        currentGame.addObserver(tui);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();

        currentGame.run();

        bf.winnerDialog();
    }
}
