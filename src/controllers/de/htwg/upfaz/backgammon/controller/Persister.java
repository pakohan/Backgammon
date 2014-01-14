package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.controller.GameMap;

import java.util.UUID;

public interface Persister {

    // will be called after a new game has been created
    UUID createGame(GameMap map);

    void saveGame(GameMap map);

    void closeDB();

    GameMap loadGame(UUID id, int rev);
}
