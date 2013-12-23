package controllers.de.htwg.upfaz.backgammon.persist;

import controllers.de.htwg.upfaz.backgammon.controller.GameMap;

import java.util.UUID;

public final class DB4OPersister implements Persister{

    public DB4OPersister() {
        System.out.println("DB created");
    }

    @Override
    public UUID createGame(final GameMap map) {
        return null;
    }
    @Override
    public void saveGame(final GameMap map) {

    }
    @Override
    public GameMap loadGame(final UUID id) {
        return null;
    }
}
