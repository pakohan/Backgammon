package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.entities.IField;

import java.util.Observable;
import java.util.Observer;

public final class Tui
        implements Observer {

    private final Core currentGame;

    public Tui(final Core currentGame2) {
        currentGame = currentGame2;
    }

    public void printField(final IField[] gameMap) {
        sysout("||011-010-009-008-007-006|OUT|005-004-003-002-001-000||-s-|%n");
        sysout("||---------------------------------------------------||---|%n");
        sysout(String.format("||"));
        for (int i = 11; i > 6; i--) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s|%s|", gameMap[6].toString(), gameMap[24].toString()));
        for (int i = 5; i > 0; i--) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s||%s|%n", gameMap[0].toString(), gameMap[26].toString()));

        sysout("||---------------------------------------------------||---|%n");

        sysout(String.format("||"));
        for (int i = 12; i < 17; i++) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s|%s|", gameMap[17].toString(), gameMap[25].toString()));
        for (int i = 18; i < 23; i++) {
            stoneSyso(gameMap, i);
        }
        sysout(String.format("%s||%s|%n", gameMap[23].toString(), gameMap[27].toString()));

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
