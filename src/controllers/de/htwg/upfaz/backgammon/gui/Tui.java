package controllers.de.htwg.upfaz.backgammon.gui;

//import controllers.de.htwg.upfaz.backgammon.controller.Game;

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
        System.out.println("||011-010-009-008-007-006|OUT|005-004-003-002-001-000||-s-|");
        System.out.println("||---------------------------------------------------||---|");
        System.out.print("||");
        for (int i = 11; i > 6; i--) {
            // System.out.printf("%s-", gameMap[i].toString());
            stoneSyso(gameMap, i);
        }
        System.out.printf("%s|%s|", gameMap[6].toString(), gameMap[24].toString());
        for (int i = 5; i > 0; i--) {
            // System.out.printf("%s-", gameMap[i].toString());
            stoneSyso(gameMap, i);
        }
        System.out.printf("%s||%s|%n", gameMap[0].toString(), gameMap[26].toString());

        System.out.println("||---------------------------------------------------||---|");

        System.out.printf("||");
        for (int i = 12; i < 17; i++) {
            // System.out.printf("%s-", gameMap[i].toString());
            stoneSyso(gameMap, i);
        }
        System.out.printf("%s|%s|", gameMap[17].toString(), gameMap[25].toString());
        for (int i = 18; i < 23; i++) {
            // System.out.printf("%s-", gameMap[i].toString());
            stoneSyso(gameMap, i);
        }
        System.out.printf("%s||%s|%n", gameMap[23].toString(), gameMap[27].toString());

        System.out.println("||---------------------------------------------------||---|");
        System.out.println("||012-013-014-015-016-017|OUT|018-019-020-021-022-023||-w-|");
    }
    @Override
    public void update(final Observable o, final Object arg) {
        printField(currentGame.getGameMap().getFields());
        System.out.println(currentGame.toString());
    }

    private static void stoneSyso(final IField[] gm, final int i) {
        System.out.printf("%s-", gm[i].toString());
    }

    @Override
    public String toString() {
        return String.format("Tui{currentGame=%s}", currentGame);
    }
}
