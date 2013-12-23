package controllers.de.htwg.upfaz.backgammon.controller;

import java.util.UUID;

public interface Persister {

    // will be called after a new game has been created
    UUID createGame(GameMap map);

    void saveGame(GameMap map);

    GameMap loadGame(UUID id);
}
