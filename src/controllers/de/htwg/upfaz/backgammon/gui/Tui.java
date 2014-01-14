package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.entities.IField;

import java.util.Observable;
import java.util.Observer;

public final class Tui
        implements Observer {

    private static final int HALF_FIELDS = 6;
    private final Core currentGame;

    public Tui(final Core currentGame2) {
        currentGame = currentGame2;
    }

    public void printField(final IField[] gameMap) {
        sysout("||011-010-009-008-007-006|OUT|005-004-003-002-001-000||-s-|%n");
        sysout("||---------------------------------------------------||---|%n");
        sysout(String.format("||"));
        for (int i = GameMap.FIELD_11; i > HALF_FIELDS; i--) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s|%s|", gameMap[HALF_FIELDS].toString(), gameMap[GameMap.FIELD_EATEN_BLACK].toString()));
        for (int i = GameMap.NUMBER_5; i > 0; i--) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s||%s|%n", gameMap[0].toString(), gameMap[GameMap.FIELD_END_BLACK].toString()));

        sysout("||---------------------------------------------------||---|%n");

        sysout(String.format("||"));
        for (int i = GameMap.FIELD_12; i < GameMap.FIELD_17; i++) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s|%s|", gameMap[GameMap.FIELD_17].toString(), gameMap[GameMap.FIELD_EATEN_WHITE].toString()));
        for (int i = GameMap.FIELD_18; i < GameMap.FIELD_23; i++) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s||%s|%n", gameMap[GameMap.FIELD_23].toString(), gameMap[GameMap.FIELD_END_WHITE].toString()));

        sysout("||---------------------------------------------------||---|%n");
        sysout("||012-013-014-015-016-017|OUT|018-019-020-021-022-023||-w-|%n");
    }
    public void update(final Observable o, final Object arg) {
        printField(currentGame.getGameMap().getFields());
        sysout(currentGame.toString());
    }

    private static void stoneSyso(final IField[] gm, final int i) {
        sysout(String.format("%s-", gm[i].toString()));
    }

    private static void sysout(String s) {
        System.out.print(s);
    }

    @Override
    public String toString() {
        return String.format("Tui{currentGame=%s}", currentGame);
    }
}
