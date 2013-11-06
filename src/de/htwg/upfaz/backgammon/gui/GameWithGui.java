package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IPlayer;

public final class GameWithGui {

    protected static final String PLAYER_S_IS_THE_WINNER = "Player %s is the winner!";
    protected static final String PLAYER_S_IT_S_YOUR_TURN = "Player %s, it's your Turn!";
    protected static final String SETTING_START_NUMBER_TO_D_AND_TARGET_NUMBER_TO_D = "Setting startNumber to %d and targetNumber to %d";
    public static void playTurn(final IGame currentGame, final IPlayer plr, final BackgammonFrame bf) {
        currentGame.setCurrentMethodName("playTurn");
        currentGame.setCurrentPlayer(plr);
        currentGame.setEndPhase(false);
        currentGame.checkEndPhase();
        currentGame.setStatus(String.format(PLAYER_S_IT_S_YOUR_TURN, plr));
        currentGame.setJumps(currentGame.rollTheDice());

        currentGame.setJumpsT(currentGame.getJumps());

        for (int index = currentGame.automaticTakeOut(); index < currentGame.getTurnsNumber(); index++) {

            // check for winner
            if (currentGame.checkForWinner(currentGame.getGameMap())) {
                currentGame.setStatus(String.format(PLAYER_S_IS_THE_WINNER, plr));
                currentGame.setWinner(plr.getColor() + 1);
                return;
            }

            currentGame.checkEndPhase();
            if (currentGame.checkIfMoveImpossible()) {
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
                currentGame.setStatus(String.format(PLAYER_S_IS_THE_WINNER, plr.toString()));
                currentGame.setWinner(plr.getColor() + 1);
                return;
            }
            currentGame.printJumpsStatus(currentGame.getJumps());
        }
    }

    private static boolean notGetStartAndTargetNumbers(final IGame currentGame, final Field[] gm, final IPlayer plr, final BackgammonFrame bf) {
        currentGame.setCurrentMethodName("getStartAndTargetNumbers");
        currentGame.setGameMap(gm);

        if (plr.getColor() == 0 && currentGame.getGameMap()[25].getNumberStones() > 0) {

            // TO ADD: check if is possible to play turn
            currentGame.setStartNumber(25);
            // get targetNumber
            bf.setResult(-1);
            currentGame.setTargetNumber(bf.getTargetWhileEatenWhite(currentGame));
        } else if (plr.getColor() == 1 && currentGame.getGameMap()[24].getNumberStones() > 0) {

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
            if (currentGame.getTargetNumber() < 24 && currentGame.isNotCheckDirection(plr)) {
                return true;
            }
        } else {

            // get startNumber
            bf.setResult(-1);
            bf.getStartNumber(currentGame);

            if (currentGame.isValidStartLoop()) {
                currentGame.setStatus(GameWithTui.YOU_CAN_NOT_PLAY_WITH_THIS_STONE);
                return true;
            }

            // get targetNumber
            bf.setResult(-1);
            bf.getEndNumber(currentGame);

            // check direction
            if (currentGame.getTargetNumber() < 24 && currentGame.isNotCheckDirection(plr)) {
                return true;
            }
        }

        currentGame.setStatus(String.format(SETTING_START_NUMBER_TO_D_AND_TARGET_NUMBER_TO_D, currentGame.getStartNumber(), currentGame.getTargetNumber()));
        return false;
    }
}
