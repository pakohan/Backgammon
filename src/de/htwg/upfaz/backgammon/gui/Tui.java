package de.htwg.upfaz.backgammon.gui;

import com.sun.javafx.tools.packager.Log;
import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.upfaz.backgammon.entities.IField;
import de.htwg.upfaz.backgammon.entities.IPlayer;
import de.htwg.util.observer.IObserver;

import java.util.Scanner;
import java.util.regex.Pattern;

import static de.htwg.upfaz.backgammon.gui.BackgammonFrame.CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class Tui
        implements IObserver {

    private static final String YOUR_INPUT = "Your input is ";
    private static final int INT_24 = 24;
    private static final String CHOOSE_THE_TARGET_FIELD = "Choose the target field: ";
    private static final Pattern COMPILE2 = Pattern.compile("[0-6]");
    private static final Pattern COMPILE = Pattern.compile("[0-9]|1[0-9]|2[0-3]");
    private static final Pattern COMPILE1 = Pattern.compile("1[8-9]|2[0-3]");
    private static final Pattern COMPIL3 = Pattern.compile("[0-9]|1[0-9]|2[0-3]");
    private final IGame currentGame;

    public Tui(final IGame gm) {
        currentGame = gm;
    }

    @SuppressWarnings("MagicNumber")
    public static void printField(final IField[] gameMap) {
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

    public int getTargetWhileEatenWhite(final IGame currentGame) {

        String target;
        int targetNumber;
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose the target field");
            try {
                target = scanner.nextLine();
                if (COMPILE2.matcher(target).matches()) {
                    // targetNumber = new int(target);

                    targetNumber = Integer.valueOf(target); // sonar recommends

                    if (currentGame.getGameMap()[targetNumber].isNotJumpable(currentGame.getCurrentPlayer().getColor())) {
                        System.out.println(CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM);
                        continue;
                    }

                    if (currentGame.isEatenWhiteCheck(targetNumber)) {
                        break;
                    }
                } else {
                    setStatusInputMismatch();
                }
            } catch (NumberFormatException e) {
                Log.verbose(e);
            } catch (Exception ignored) {
                setStatusInputMismatch();
            }
        }
        currentGame.setStatus(YOUR_INPUT + target);

        return targetNumber;
    }

    public int getTargetWhileEatenBlack(final IField[] gameMap, final IPlayer plr, final int[] jumps) {

        String target;
        int targetNumber;
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            try {

                System.out.println("");
                System.out.printf(CHOOSE_THE_TARGET_FIELD);
                target = scanner.nextLine();
                if (COMPILE1.matcher(target).matches()) {
                    targetNumber = Integer.valueOf(target); // sonar recommends

                    // TO ADD: check if movable
                    if (gameMap[targetNumber].isNotJumpable(plr.getColor())) {
                        System.out.println(CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM);
                        continue;
                    }

                    if (targetNumber + jumps[0] == INT_24) {
                        jumps[0] = 0;
                        break;
                    } else if (targetNumber + jumps[1] == INT_24) {
                        jumps[1] = 0;
                        break;
                    } else if (targetNumber + jumps[2] == INT_24) {
                        jumps[2] = 0;
                        break;
                    } else if (targetNumber + jumps[3] == INT_24) {
                        jumps[3] = 0;
                        break;
                    }
                } else {
                    setStatusInputMismatch();
                }
            } catch (NumberFormatException e) {
                Log.verbose(e);
            } catch (Exception e) {
                Log.verbose("Exception. Input doesn't match!");
                Log.verbose(e);
            }
        }
        currentGame.setStatus(YOUR_INPUT + target);

        return targetNumber;
    }

    public void getStartNumber(final IGame currentGame) {

        String start;
        int startNumber;
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            try {

                System.out.printf("%nChoose the stone: ");
                start = scanner.nextLine();
                // startNumber = new int(start);

                startNumber = Integer.valueOf(start); // sonar recommends

                if (COMPIL3.matcher(start).matches() && currentGame.checkStartNumber(startNumber)) {
                    break;
                } else {
                    setStatusInputMismatch();
                }
            } catch (NumberFormatException e) {
                Log.verbose(e);
            } catch (Exception e) {
                Log.verbose(e);
            }
        }

        currentGame.setStatus(YOUR_INPUT + start);
        currentGame.setStartNumber(startNumber);
    }

    public void getEndTarget(final IGame currentGame) {

        String target;
        int targetNumber;
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            try {

                System.out.println("");
                System.out.printf(CHOOSE_THE_TARGET_FIELD);
                target = scanner.nextLine();

                if (COMPILE.matcher(target).matches()) {
                    // targetNumber = new int(target);

                    targetNumber = Integer.valueOf(target); // sonar recommends

                    // TO ADD: check if movable
                    if (currentGame.getGameMap()[targetNumber].isNotJumpable(currentGame.getCurrentPlayer().getColor())) {
                        System.out.println(CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM);
                        continue;
                    }

                    if (currentGame.checkNormalEndTarget(targetNumber)) {
                        break;
                    }
                } else if (currentGame.getCurrentPlayer().getColor() == 0 && "w".equals(target) && currentGame.isEndPhase()) {
                    targetNumber = 27;

                    if (currentGame.checkEndphaseWhiteTarget(targetNumber)) {
                        break;
                    }
                } else if (currentGame.getCurrentPlayer().getColor() == 1 && "s".equals(target) && currentGame.isEndPhase()) {
                    targetNumber = 26;

                    if (currentGame.checkEndphaseBlackTarget()) {
                        break;
                    }
                } else {
                    setStatusInputMismatch();
                }
            } catch (NumberFormatException e) {
                Log.verbose(e);
            } catch (Exception e) {
                Log.verbose(e);
            }
        }

        currentGame.setStatus(YOUR_INPUT + target);
        currentGame.setTargetNumber(targetNumber);
    }

    private void setStatusInputMismatch() {
        currentGame.setStatus("Input doesn't match!");
    }

    private static void stoneSyso(final IField[] gm, final int i) {
        System.out.printf("%s-", gm[i].toString());
    }

    @Override
    public void update() {
        System.out.println(currentGame.getStatus());
    }
    @Override
    public String toString() {
        return String.format("Tui{currentGame=%s}", currentGame);
    }
}
