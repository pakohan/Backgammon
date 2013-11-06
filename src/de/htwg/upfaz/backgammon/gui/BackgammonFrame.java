package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.Game;
import de.htwg.util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public final class BackgammonFrame
        extends JFrame
        implements MouseListener, MouseMotionListener, IObserver {

    private static final long serialVersionUID = 1L;
    private static final String GET_TARGET_WHILE_EATEN_WHITE = "getTargetWhileEatenWhite";
    protected static final String CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM = "Can't jump this Field (Color problem)";
    protected static final String GET_START_NUMBER = "getStartNumber";

    private final Game currentGame;
    private final StatusPanel statusPanel;
    private String status = "";
    private int result = -1;

    public BackgammonFrame(final Game gm) {

        currentGame = gm;

        setTitle("Upfaz backgammon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constances.DEFAULT_X_RESOLUTION, Constances.DEFAULT_Y_RESOLUTION);
        setResizable(false);
        final Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        /*
            bp = new BackgroundPanel();
            pane.add(bp);
            dices = new Dices(currentGame.getJumps());
            pane.add(dices);
            ds = new DrawStones(currentGame.getGameMap());
            pane.add(ds);
        */

        final DrawTest test = new DrawTest(currentGame);
        pane.add(test);
        statusPanel = new StatusPanel();
        pane.add(statusPanel, BorderLayout.SOUTH);

        addMouseListener(this);
        addMouseMotionListener(this);

        setVisible(true);
        repaint();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        result = getClickedField(x, y);
        mouseHandler(e);
    }

    private static int getLeft(final int x) {
        int left = 0;
        int candidate = 0;

        for (int i = Constances.LEFT_BORDER; i <= Constances.LEFT_CENTER_BORDER; i += Constances.FIELD_SIZE_PXL) {
            left = calcLeft(i, x, candidate);
            if (left >= 0) {
                break;
            }
            candidate++;
        }
        if (left == -1) {
            for (int i = Constances.RIGHT_CENTER_BORDER; i <= Constances.RIGHT_BORDER; i += Constances.FIELD_SIZE_PXL) {
                left = calcLeft(i, x, candidate);
                if (left >= 0) {
                    break;
                }
                candidate++;
            }
        }

        return left;
    }

    private static int getClickedField(final int x, final int y) {

        final int left = getLeft(x);
        int output = -1;

        if (y <= Constances.UPPER_CENTER_BORDER && y >= Constances.UPPER_BORDER && left >= 0) {
            output = 11 - left;
        } else if (y >= Constances.DOWN_CENTER_BORDER && y <= Constances.DOWN_BORDER && left >= 0) {
            output = 12 + left;
        }

        output = getClickedEndfield(output, x, y);

        return output;
    }

    private static int getClickedEndfield(final int oldOutput, final int x, final int y) {

        int newOutput = oldOutput;

        if (x > Constances.LEFT_OUT_BORDER && y < Constances.RIGHT_OUT_BORDER) {
            if (y < 340) {
                newOutput = Constances.FIELD_END_BLACK;
            } else {
                newOutput = Constances.FIELD_END_WHITE;
            }
        }
        return newOutput;
    }

    private static int calcLeft(final int i, final int x, final int candidate) {
        if (x >= i && x <= i + Constances.FIELD_STEP) {
            return candidate;
        } else {
            return -1;
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        mouseHandler(e);
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        mouseHandler(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) { }

    @Override
    public void mouseReleased(final MouseEvent e) { }

    public void update() {
        statusPanel.setText(status);
        repaint();
    }

    public void setResult(final int newResult) {
        result = newResult;
    }

    public int getTargetWhileEatenWhite() {
        currentGame.setCurrentMethodName(GET_TARGET_WHILE_EATEN_WHITE);
        int targetNumber = result;
        try {

            while (targetNumber == -1 || targetNumber > 6) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                targetNumber = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose(e);
        }

        return targetNumber;
    }

    public int getTargetWhileEatenBlack() {
        currentGame.setCurrentMethodName("getTargetWhileEatenBlack");
        int targetNumber = result;
        try {

            while (targetNumber >= Constances.FIELD_EATEN_BLACK || targetNumber < 18) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                targetNumber = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose("getTargetWhileEatenBlack");
            Log.verbose(e);
        }

        return targetNumber;
    }

    public void getStartNumber() {
        currentGame.setCurrentMethodName(GET_START_NUMBER);
        int startNumber = result;
        try {

            while (startNumber < 0 || startNumber >= Constances.FIELD_EATEN_BLACK) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                startNumber = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose(GET_START_NUMBER);
            Log.verbose(e);
        }

        if (currentGame.checkStartNumber(startNumber)) {
            currentGame.setStartNumber(startNumber);
        } else {
            System.err.println("You can't move this piece or there are no pieces");
            result = -1;
            getStartNumber();
        }
    }

    private int getTargetResult(final int oldTarget) {

        int newTarget = oldTarget;
        try {
            while (newTarget < 0 || newTarget == Constances.FIELD_EATEN_BLACK || newTarget == Constances.FIELD_EATEN_WHITE || newTarget == currentGame.getStartNumber() || (newTarget == Constances.FIELD_END_BLACK || newTarget == Constances.FIELD_END_WHITE) && !currentGame.isEndPhase()) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                newTarget = result;
            }
        } catch (InterruptedException e) {
            Log.verbose(e);
        } catch (Exception e) {
            Log.verbose("getTargetResult");
            Log.verbose(e);
        }
        return newTarget;
    }

    private void checkColorProblem(final int targetNumber) {
        if (currentGame.getGameMap()[targetNumber].isNotJumpable(currentGame.getCurrentPlayer().getColor())) {
            System.err.println(CAN_T_JUMP_THIS_FIELD_COLOR_PROBLEM);
            result = -1;
            getEndNumber();
        }
    }

    private void checkTargetValidness(final int targetNumber) {
        if (currentGame.checkNormalEndTarget(targetNumber)) {
            currentGame.setTargetNumber(targetNumber);
        } else {
            result = -1;
            getEndNumber();
        }
    }

    private void checkEndphaseBlack() {
        // endphase black
        if (currentGame.checkEndphaseBlackTarget()) {
            currentGame.setTargetNumber(Constances.FIELD_END_BLACK);
        } else {
            result = -1;
            getEndNumber();
        }
    }

    private void checkEndphaseWhite() {
        // endphase white
        if (currentGame.checkEndphaseWhiteTarget(Constances.FIELD_END_WHITE)) {
            currentGame.setTargetNumber(Constances.FIELD_END_WHITE);
        } else {
            result = -1;
            getEndNumber();
        }
    }

    public void getEndNumber() {
        currentGame.setCurrentMethodName("getEndNumber");
        int targetNumber = result;
        try {

            targetNumber = getTargetResult(targetNumber);

            if (targetNumber >= 0 && targetNumber < Constances.FIELD_EATEN_BLACK) {

                checkColorProblem(targetNumber);
                checkTargetValidness(targetNumber);
            } else if (targetNumber == Constances.FIELD_END_BLACK) {
                checkEndphaseBlack();
            } else if (targetNumber == Constances.FIELD_END_WHITE) {
                checkEndphaseWhite();
            }
        } catch (Exception e) {
            Log.verbose("getEndNumber");
            Log.verbose(e);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        // mouseHandler(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        mouseHandler(e);
    }

    private void mouseHandler(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        status = String.format("x = %d, y = %d\t start = %d, target = %d, result = %d; Current player: %s; %s; Method: %s", x, y, currentGame.getStartNumber(), currentGame.getTargetNumber(), result, currentGame.getCurrentPlayer(), currentGame.printJumpsString(), currentGame.getCurrentMethodName());
        update();
    }

    public void noMovesDialog() {
        JOptionPane.showMessageDialog(this, "No possible moves available", "Bad luck", JOptionPane.WARNING_MESSAGE);
    }

    public void winnerDialog() {
        JOptionPane.showMessageDialog(this, currentGame.getCurrentPlayer() + " is the winner!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
    }
}
