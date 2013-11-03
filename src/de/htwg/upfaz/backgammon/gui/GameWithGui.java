package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.util.observer.ResourceBundle;

public final class GameWithGui {

    public void playTurn(final IGame currentGame, final IPlayer plr, final BackgammonFrame bf) {
        currentGame.setCurrentMethodName(ResourceBundle.getString("playturn"));
        currentGame.setCurrentPlayer(plr);
        currentGame.setEndPhase(false);
        currentGame.checkEndPhase();
        currentGame.setStatus(String.format(ResourceBundle.getString("player.s.it.s.your.turn"), plr.toString()));
        currentGame.setJumps(currentGame.rollTheDice());

        currentGame.setJumpsT(currentGame.getJumps());

        for (int index = currentGame.automaticTakeOut(); index < currentGame.getTurnsNumber(); index++) {

            // check for winner
            if (currentGame.checkForWinner(currentGame.getGameMap())) {
                currentGame.setStatus(String.format(ResourceBundle.getString("player.s.is.the.winner"), plr.toString()));
                currentGame.setWinner(plr.getColor() + 1);
                return;
            }

            currentGame.checkEndPhase();
            if (currentGame.notCheckIfMovesPossible()) {
                bf.noMovesDialog();
                return;
            }

            if (notGetStartAndTargetNumbers(currentGame, currentGame.getGameMap(), plr, bf)) {
                currentGame.setStartNumber(-1);
                index--;
                continue;
            }

            currentGame.setGameMap(currentGame.doSomethingWithStones(currentGame.getGameMap(), plr, currentGame.getStartNumber(), currentGame.getTargetNumber(), currentGame.isEndPhase()));
            currentGame.renewJumps(currentGame.getStartNumber(), currentGame.getTargetNumber());
            currentGame.setStartNumber(-1);
            currentGame.setTargetNumber(-1);

            // check for winner
            if (currentGame.checkForWinner(currentGame.getGameMap())) {
                currentGame.setStatus(String.format(ResourceBundle.getString("player.s.is.the.winner1"), plr.toString()));
                currentGame.setWinner(plr.getColor() + 1);
                return;
            }
            currentGame.printJumpsStatus(currentGame.getJumps());
        }
    }

    private boolean notGetStartAndTargetNumbers(final IGame currentGame, final Field[] gm, final IPlayer plr, final BackgammonFrame bf) {
        currentGame.setCurrentMethodName(ResourceBundle.getString("getstartandtargetnumbers"));
        currentGame.setGameMap(gm);

        if (0 == plr.getColor() && 0 < currentGame.getGameMap()[25].getNumberStones()) {

            // TO ADD: check if is possible to play turn
            currentGame.setStartNumber(25);
            // get targetNumber
            bf.setResult(-1);
            currentGame.setTargetNumber(bf.getTargetWhileEatenWhite(currentGame));
        } else if (1 == plr.getColor() && 0 < currentGame.getGameMap()[24].getNumberStones()) {

            currentGame.setStartNumber(24);

            // get targetNumber
            bf.setResult(-1);
            currentGame.setTargetNumber(bf.getTargetWhileEatenBlack(currentGame));
        } else if (currentGame.isEndPhase()) {
            // get startNumber
            bf.setResult(-1);
            bf.getStartNumber(currentGame);

            // get targetNumber
            bf.setResult(-1);
            bf.getEndNumber(currentGame);

            // check direction
            if (24 > currentGame.getTargetNumber() && currentGame.notCheckDirection(plr)) {
                return true;
            }
        } else {

            // get startNumber
            bf.setResult(-1);
            bf.getStartNumber(currentGame);

            if (currentGame.notCheckStartValidnessLoop()) {
                currentGame.setStatus(ResourceBundle.getString("you.can.not.play.with.this.stone"));
                return true;
            }

            // get targetNumber
            bf.setResult(-1);
            bf.getEndNumber(currentGame);

            // check direction
            if (24 > currentGame.getTargetNumber() && currentGame.notCheckDirection(plr)) {
                return true;
            }
        }

        currentGame.setStatus(String.format(ResourceBundle.getString("setting.startnumber.to.d.and.targetnumber.to.d"), currentGame.getStartNumber(), currentGame.getTargetNumber()));
        return false;
    }
}
