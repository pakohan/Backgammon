package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.util.observer.ResourceBundle;

public final class GameWithTui {

    public void playTurn(final IGame currentGame, final IPlayer plr, final Tui tui) {

        currentGame.setCurrentPlayer(plr);
        currentGame.setEndPhase(false);
        currentGame.checkEndPhase();
        currentGame.setStatus("");
        currentGame.setStatus(String.format(ResourceBundle.getString("player.s.it.s.your.turn1"), plr.toString()));
        currentGame.setJumps(currentGame.rollTheDice());

        currentGame.setJumpsT(currentGame.getJumps());

        for (int index = currentGame.automaticTakeOut(); index < currentGame.getTurnsNumber(); index++) {

            currentGame.checkEndPhase();
            if (currentGame.notCheckIfMovesPossible()) {
                currentGame.setStatus(ResourceBundle.getString("no.moves.available"));
                return;
            }

            tui.printField(currentGame.getGameMap());

            if (notGetStartAndTargetNumbers(currentGame, currentGame.getGameMap(), plr, tui)) {
                index--;
                continue;
            }

            currentGame.setGameMap(currentGame.doSomethingWithStones(currentGame.getGameMap(), plr, currentGame.getStartNumber(), currentGame.getTargetNumber(), currentGame.isEndPhase()));
            currentGame.renewJumps(currentGame.getStartNumber(), currentGame.getTargetNumber());
            currentGame.setStartNumber(0);
            currentGame.setTargetNumber(0);

            // check for winner
            if (currentGame.checkForWinner(currentGame.getGameMap())) {
                currentGame.setStatus(String.format(ResourceBundle.getString("player.s.is.the.winner2"), plr.toString()));
                currentGame.setWinner(plr.getColor() + 1);
                return;
            }
            currentGame.printJumpsStatus(currentGame.getJumps());
        }
    }

    boolean notGetStartAndTargetNumbers(final IGame currentGame, final Field[] gm, final IPlayer plr, final Tui tui) {

        currentGame.setGameMap(gm);
        if (0 == plr.getColor() && 0 < currentGame.getGameMap()[25].getNumberStones()) {

            // TO ADD: check if is possible to play turn
            currentGame.setStartNumber(25);
            // get targetNumber
            currentGame.setTargetNumber(tui.getTargetWhileEatenWhite(currentGame));
        } else if (1 == plr.getColor() && 0 < currentGame.getGameMap()[24].getNumberStones()) {

            currentGame.setStartNumber(24);

            // get targetNumber
            currentGame.setTargetNumber(tui.getTargetWhileEatenBlack(currentGame.getGameMap(), plr, currentGame.getJumps()));
        } else if (currentGame.isEndPhase()) {
            tui.getStartNumber(currentGame);
            tui.getEndTarget(currentGame);
        } else {

            // get startNumber

            tui.getStartNumber(currentGame);

            if (currentGame.notCheckStartValidnessLoop()) {
                currentGame.setStatus(ResourceBundle.getString("you.can.not.play.with.this.stone1"));
                return true;
            }

            // get targetNumber
            tui.getEndTarget(currentGame);
        }

        // check direction
        if (24 > currentGame.getTargetNumber() && currentGame.notCheckDirection(plr)) {
            return true;
        }
        currentGame.setStatus(String.format(ResourceBundle.getString("setting.startnumber.to.d.and.targetnumber.to.d"), currentGame.getStartNumber(), currentGame.getTargetNumber()));
        return false;
    }
}
