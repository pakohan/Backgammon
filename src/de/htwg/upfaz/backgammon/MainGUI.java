package de.htwg.upfaz.backgammon;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import de.htwg.upfaz.backgammon.gui.GameWithTui;
import de.htwg.upfaz.backgammon.gui.Log;
import de.htwg.upfaz.backgammon.gui.Tui;

import java.util.Scanner;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class MainGUI {

    private static int curPl = 1;

    private MainGUI() { }

    public static void main(final String[] args) {
        final Game currentGame = new Game();
        System.out.println("Welcome to upfaz backgammon.");

        final int choiceNumber = chooseUI();

        if (choiceNumber == 1) {
            playGameWithTui(currentGame);
        } else {
            playGameWithGui(currentGame);
        }
    }

    private static int chooseUI() {
        final Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Choose the UI:");
            System.out.println("\t1.Tui");
            System.out.println("\t2.Gui");
            final String choice = scanner.nextLine();
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

    private static void playGameWithGui(final Game currentGame) {

        //final GameWithGui guiGame = new GameWithGui();
        final BackgammonFrame bf = new BackgammonFrame(currentGame);

        currentGame.addObserver(bf);
        currentGame.notifyObservers();
        while (currentGame.getWinner() == 0) {
            currentGame.startRound();
        }

        bf.winnerDialog();
    }
}
