package de.htwg.upfaz.backgammon;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import de.htwg.upfaz.backgammon.gui.GameWithGui;
import de.htwg.upfaz.backgammon.gui.GameWithTui;
import de.htwg.upfaz.backgammon.gui.Tui;

import java.util.Scanner;

/**
 * Class GUI
 */
public final class MainGUI {

    private static BackgammonFrame bf;
    private static Tui tui;
    private static int curPl = 1;
    private static IGame currentGame;

    private MainGUI() { }

    public static void main(String[] args) {
        currentGame = new Game();
        System.out.println("Welcome to upfaz backgammon.");

        int choiceNumber = chooseUI();

        if (choiceNumber == 1) {
            playGameWithTui(currentGame);
        } else {
            playGameWithGui(currentGame);
        }
    }

    private static int chooseUI() {
        String choice;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Choose the UI:");
            System.out.println("\t1.Tui");
            System.out.println("\t2.Gui");
            choice = scanner.nextLine();
            int choiceNumber = Integer.valueOf(choice);
            if (choiceNumber == 1 || choiceNumber == 2) {
                return choiceNumber;
            } else {
                System.out.println("Input doesn't match: " + choice);
                return 0;
            }
        } catch (Exception e) {
            System.err.println("Exception." + e.getMessage());
            return 0;
        }
    }

    private static void playGameWithTui(IGame currentGame) {

        GameWithTui tuiGame = new GameWithTui();
        tui = new Tui(currentGame);
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

    private static void playGameWithGui(IGame currentGame) {

        GameWithGui guiGame = new GameWithGui();
        bf = new BackgammonFrame(currentGame);
        currentGame.addObserver(bf);
        currentGame.notifyObservers();
        while (currentGame.getWinner() == 0) {
            if (curPl == 1) {
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
