package de.htwg.upfaz.backgammon;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import de.htwg.upfaz.backgammon.gui.GameWithTui;
import de.htwg.upfaz.backgammon.gui.Log;
import de.htwg.upfaz.backgammon.gui.Tui;

import java.util.Scanner;

public final class Main {

	private static int curPl = 1;

	private Main() {
	}

	public static void main(final String[] args) {
		final Game currentGame = new Game();
		System.out.println("Welcome to upfaz backgammon.");

		// here was the choice that ui to play
		// final int choiceNumber = chooseUI();
		//
		// if (choiceNumber == 1) {
		// playGameWithTui(currentGame);
		// } else {
		// playGameWithGui(currentGame);
		// }

		// after that I decided to avoid the choice
		// no need in this function
		// playGameWithGui(currentGame);


		while (currentGame.getWinner() == 0) {
			currentGame.startRound();
		}

		//show winner
		currentGame.getBackgammonFrame().winnerDialog();
	}

	/*
	 * Player chooses what UI he wants to play -> has to be refactored All UIs
	 * have to run parallel without choosing
	 */
	@SuppressWarnings("unused")
	private static int chooseUI() {
		final Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Choose the UI:");
			System.out.println("\t1.Tui");
			System.out.println("\t2.Gui");
			final String choice = scanner.nextLine();
			scanner.close();
			final int choiceNumber = Integer.valueOf(choice);
			if (choiceNumber == 1 || choiceNumber == 2) {

				return choiceNumber;
			} else {
				System.out.printf("Input doesn't match: %s%n", choice);

				return 0;
			}
		} catch (NumberFormatException e) {
			Log.verbose(e);
			return 0;
		} catch (Exception e) {
			Log.verbose(e);
			return 0;
		}

	}

	@SuppressWarnings("unused")
	private static void playGameWithTui(final Game currentGame) {

		final GameWithTui tuiGame = new GameWithTui();
		final Tui tui = new Tui(currentGame);
		currentGame.addObserver(tui);
		currentGame.notifyObservers();
		while (currentGame.getWinner() == 0) {

			if (curPl == 1) {
				tuiGame.playTurn(currentGame, currentGame.getPlayer1(), tui);
				curPl = 2;
			} else {
				tuiGame.playTurn(currentGame, currentGame.getPlayer2(), tui);
				curPl = 1;
			}
		}

		System.out.println(currentGame.getCurrentPlayer() + " is the winner!");
	}

	@SuppressWarnings("unused")
	private static void playGameWithGui(final Game currentGame) {

		// final GameWithGui guiGame = new GameWithGui();

		// actually backgammom frame is initialised in Game constructor
		// final BackgammonFrame bf = new BackgammonFrame(currentGame);
		final BackgammonFrame bf = currentGame.getBackgammonFrame();

		while (currentGame.getWinner() == 0) {
			currentGame.startRound();
		}

		bf.winnerDialog();
	}
}
