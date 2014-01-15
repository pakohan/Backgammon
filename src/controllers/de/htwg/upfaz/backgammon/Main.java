package controllers.de.htwg.upfaz.backgammon;

import com.google.inject.Guice;
import com.google.inject.Injector;

import controllers.de.htwg.upfaz.backgammon.controller.BackGammonCouchDBModule;
import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import controllers.de.htwg.upfaz.backgammon.gui.Tui;

public final class Main {

    
    public static void main(final String[] args) {
        Injector INJECTOR = Guice.createInjector(new BackGammonCouchDBModule());
        final Core currentGame = INJECTOR.getInstance(Core.class);
        currentGame.createGame();

        final BackgammonFrame bf = new BackgammonFrame(currentGame);
        final Tui tui = new Tui(currentGame);

        currentGame.addObserver(tui);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();
    }
}
