package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.upfaz.backgammon.entities.Field;
import de.htwg.upfaz.backgammon.entities.IPlayer;

public final class GameWithTui {

	public static final String YOU_CAN_NOT_PLAY_WITH_THIS_STONE = "You can not play with this stone!";

	public GameWithTui() {
	}

	public void playTurn(final Game currentGame, final IPlayer plr,
			final Tui tui) {

		currentGame.setEndPhase(false);
		currentGame.checkEndPhase();
		currentGame.setStatus("");
		currentGame.setStatus(String.format(
				GameWithGui.PLAYER_S_IT_S_YOUR_TURN, plr.toString()));
		currentGame.setJumps(currentGame.rollTheDice());

		currentGame.setJumpsT(currentGame.getJumps());

		for (int index = currentGame.automaticTakeOut(); index < currentGame
				.getTurnsNumber(); index++) {

			currentGame.checkEndPhase();
			if (currentGame.checkIfMoveImpossible()) {
				currentGame.setStatus("No moves available");
				return;
			}

			tui.printField(currentGame.getGameMap());

			if (notGetStartAndTargetNumbers(currentGame,
					currentGame.getGameMap(), plr, tui)) {
				index--;
				continue;
			}

			currentGame.setGameMap(currentGame.doSomethingWithStones());
			currentGame.renewJumps(currentGame.getStartNumber(),
					currentGame.getTargetNumber());
			currentGame.setStartNumber(0);
			currentGame.setTargetNumber(0);

			// check for winner
			if (currentGame.checkForWinner()) {
				currentGame.setStatus(String.format(
						GameWithGui.PLAYER_S_IS_THE_WINNER, plr.toString()));
				currentGame.setWinner(plr.getColor() + 1);
				return;
			}
			currentGame.printJumpsStatus(currentGame.getJumps());
		}
	}

	// has to be moved in Game ?
	static boolean notGetStartAndTargetNumbers(final Game currentGame,
			final Field[] gm, final IPlayer plr, final Tui tui) {

		currentGame.setGameMap(gm);
		if (plr.getColor() == 0
				&& currentGame.getGameMap()[25].getNumberStones() > 0) {

			// TO ADD: check if is possible to play turn
			currentGame.setStartNumber(25);
			// get targetNumber
			currentGame.setTargetNumber(tui
					.getTargetWhileEatenWhite(currentGame));
		} else if (plr.getColor() == 1
				&& currentGame.getGameMap()[24].getNumberStones() > 0) {

			currentGame.setStartNumber(24);

			// get targetNumber
			currentGame.setTargetNumber(tui.getTargetWhileEatenBlack(
					currentGame.getGameMap(), plr, currentGame.getJumps()));
		} else if (currentGame.isEndPhase()) {
			tui.getStartNumber();
			tui.getEndTarget();
		} else {

			// get startNumber

			tui.getStartNumber();

			if (currentGame.isValidStartLoop()) {
				currentGame.setStatus(YOU_CAN_NOT_PLAY_WITH_THIS_STONE);
				return true;
			}

			// get targetNumber
			tui.getEndTarget();
		}

		// check direction
		if (currentGame.getTargetNumber() < 24
				&& currentGame.isNotCheckDirection(plr)) {
			return true;
		}
		currentGame.setStatus(String.format(
				GameWithGui.SETTING_START_NUMBER_TO_D_AND_TARGET_NUMBER_TO_D,
				currentGame.getStartNumber(), currentGame.getTargetNumber()));
		return false;
	}
}
