package de.htwg.upfaz.backgammon;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import de.htwg.upfaz.backgammon.gui.GameWithGui;
import de.htwg.upfaz.backgammon.gui.GameWithTui;
import de.htwg.upfaz.backgammon.gui.Tui;

import java.util.Scanner;

public final class MainGUI {

    private static int curPl = 1;

    private MainGUI() { }

    public static void main(final String[] args) {
        final IGame currentGame = new Game();
        System.out.println("Welcome to upfaz backgammon.");

        final int choiceNumber = chooseUI();

        if (1 == choiceNumber) {
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
            final int choiceNumber = Integer.valueOf(choice).intValue();
            if (1 == choiceNumber || 2 == choiceNumber) {
                return choiceNumber;
            } else {
                System.out.println("Input doesn't match: " + choice);
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            System.err.println("Exception." + e.getMessage());
            return 0;
        }
    }

    private static void playGameWithTui(final IGame currentGame) {

        final GameWithTui tuiGame = new GameWithTui();
        final Tui tui = new Tui(currentGame);
        currentGame.addObserver(tui);
        currentGame.notifyObservers();
        while (0 == currentGame.getWinner()) {

            if (1 == curPl) {
                tuiGame.playTurn(currentGame, currentGame.getPlayer1(), tui);
                curPl = 2;
            } else {
                tuiGame.playTurn(currentGame, currentGame.getPlayer2(), tui);
                curPl = 1;
            }
        }

        System.out.println(currentGame.getCurrentPlayer() + " is the winner!");
    }

    private static void playGameWithGui(final IGame currentGame) {

        final GameWithGui guiGame = new GameWithGui();
        final BackgammonFrame bf = new BackgammonFrame(currentGame);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();
        while (0 == currentGame.getWinner()) {
            if (1 == curPl) {
                guiGame.playTurn(currentGame, currentGame.getPlayer1(), bf);
                curPl = 2;
            } else {
                guiGame.playTurn(currentGame, currentGame.getPlayer2(), bf);
                curPl = 1;
            }
        }

        bf.winnerDialog();
    }
}
