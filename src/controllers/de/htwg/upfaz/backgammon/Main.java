package controllers.de.htwg.upfaz.backgammon;

//import controllers.de.htwg.upfaz.backgammon.controller.Game;
import controllers.de.htwg.upfaz.backgammon.controller.GameNew;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import controllers.de.htwg.upfaz.backgammon.gui.Tui;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        //final Game currentGame = new Game();

    	final GameNew currentGame = new GameNew();

        final BackgammonFrame bf = new BackgammonFrame(currentGame);
        final Tui tui = new Tui(currentGame);

        currentGame.addObserver(tui);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();

        //currentGame.run();

        //bf.winnerDialog();
    }
}
