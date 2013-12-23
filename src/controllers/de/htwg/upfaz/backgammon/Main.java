package controllers.de.htwg.upfaz.backgammon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.de.htwg.upfaz.backgammon.controller.GameNew;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import controllers.de.htwg.upfaz.backgammon.gui.Tui;

public final class Main {
    private Main() {}

    private final static Injector INJECTOR = Guice.createInjector(new BackGammonModule());

    public static void main(final String[] args) {
        final GameNew currentGame = INJECTOR.getInstance(GameNew.class);
        currentGame.createGame();

        final BackgammonFrame bf = new BackgammonFrame(currentGame);
        final Tui tui = new Tui(currentGame);

        currentGame.addObserver(tui);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();
    }
}
