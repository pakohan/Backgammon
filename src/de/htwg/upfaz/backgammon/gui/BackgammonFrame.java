package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BackgammonFrame
        extends JFrame
        implements MouseListener, MouseMotionListener, IObserver {

    private static final long serialVersionUID = 1L;
    private static final String GET_TARGET_WHILE_EATEN_WHITE = "getTargetWhileEatenWhite";

    private final IGame currentGame;
    private final StatusPanel statusPanel;
    private String status = "";
    private int result = -1;

    public BackgammonFrame(final IGame gm) {

        currentGame = gm;

        setTitle("Upfaz  backgammon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constances.DEFAULT_X, Constances.DEFAULT_Y);
        setResizable(false);
        final Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        // bp = new BackgroundPanel();
        // pane.add(bp);
        // dices = new Dices(currentGame.getJumps());
        // pane.add(dices);
        // ds = new DrawStones(currentGame.getGameMap());
        // pane.add(ds);

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
        setResult(getClickedField(x, y));
        mouseHandler(e);
    }

    private int getLeft(final int x) {
        int left = 0;
        int candidate = 0;

        for (int i = Constances.LEFT_BORDER; Constances.LEFT_CENTER_BORDER >= i; i += Constances.FIELD_SIZE_PXL) {
            left = calcLeft(i, x, candidate);
            if (0 <= left) {
                break;
            }
            candidate++;
        }
        if (-1 == left) {
            for (int i = Constances.RIGHT_CENTER_BORDER; Constances.RIGHT_BORDER >= i; i += Constances.FIELD_SIZE_PXL) {
                left = calcLeft(i, x, candidate);
                if (0 <= left) {
                    break;
                }
                candidate++;
            }
        }

        return left;
    }

    private int getClickedField(final int x, final int y) {

        final int left = getLeft(x);
        int output = -1;

        if (Constances.UPPER_CENTER_BORDER >= y && Constances.UPPER_BORDER <= y && 0 <= left) {
            output = 11 - left;
        } else if (Constances.DOWN_CENTER_BORDER <= y && Constances.DOWN_BORDER >= y && 0 <= left) {
            output = 12 + left;
        }

        output = getClickedEndfield(output, x, y);

        return output;
    }

    private int getClickedEndfield(final int oldOutput, final int x, final int y) {

        int newOutput = oldOutput;

        if (Constances.LEFT_OUT_BORDER < x && Constances.RIGHT_OUT_BORDER > y) {
            if (340 > y) {
                newOutput = Constances.FIELD_END_BLACK;
            } else {
                newOutput = Constances.FIELD_END_WHITE;
            }
        }
        return newOutput;
    }

    private int calcLeft(final int i, final int x, final int candidate) {
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
    public void mousePressed(final MouseEvent e) {
        // do nothing

    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        // do nothing

    }

    @Override
    public void update() {

        statusPanel.setText(status);
        repaint();
    }

    int getResult() {
        return result;
    }

    public void setResult(final int newResult) {
        result = newResult;
    }

    public int getTargetWhileEatenWhite(final IGame currentGame) {
        currentGame.setCurrentMethodName(GET_TARGET_WHILE_EATEN_WHITE);
        int targetNumber = getResult();
        try {

            while (-1 == targetNumber || 6 < targetNumber) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                targetNumber = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
            System.out.println(GET_TARGET_WHILE_EATEN_WHITE);
        }

        return targetNumber;
    }

    public int getTargetWhileEatenBlack(final IGame currentGame) {
        currentGame.setCurrentMethodName("getTargetWhileEatenBlack");
        int targetNumber = getResult();
        try {

            while (Constances.FIELD_EATEN_BLACK <= targetNumber || 18 > targetNumber) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                targetNumber = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
            System.out.println("getTargetWhileEatenBlack");
        }

        return targetNumber;
    }

    public void getStartNumber(final IGame currentGame) {
        currentGame.setCurrentMethodName("getStartNumber");
        int startNumber = getResult();
        try {

            while (0 > startNumber || Constances.FIELD_EATEN_BLACK <= startNumber) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                startNumber = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
            System.out.println("getStartNumber");
        }

        if (currentGame.checkStartNumber(startNumber)) {
            currentGame.setStartNumber(startNumber);
        } else {
            System.out.println("You can't move this piece or there is no pieces");
            setResult(-1);
            getStartNumber(currentGame);
        }
    }

    private int getTargetResult(final int oldTarget) {

        int newTarget = oldTarget;
        try {

            while (0 > newTarget || Constances.FIELD_EATEN_BLACK == newTarget || Constances.FIELD_EATEN_WHITE == newTarget || newTarget == currentGame.getStartNumber() || (Constances.FIELD_END_BLACK == newTarget || Constances.FIELD_END_WHITE == newTarget) && !currentGame.isEndPhase()) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                newTarget = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("getTargetResult: " + e);
        }
        return newTarget;
    }

    private void checkColorProblem(final int targetNumber) {
        if (currentGame.getGameMap()[targetNumber].isNotJumpable(currentGame.getCurrentPlayer().getColor())) {
            System.out.println("Can't jump this Field (Color problem)");
            setResult(-1);
            getEndNumber(currentGame);
        }
    }

    private void checkTargetValidness(final int targetNumber) {
        if (currentGame.checkNormalEndTarget(targetNumber)) {
            currentGame.setTargetNumber(targetNumber);
        } else {
            setResult(-1);
            getEndNumber(currentGame);
        }
    }

    private void checkEndphaseBlack() {
        // endphase black
        if (currentGame.checkEndphaseBlackTarget()) {
            currentGame.setTargetNumber(Constances.FIELD_END_BLACK);
        } else {
            setResult(-1);
            getEndNumber(currentGame);
        }
    }

    private void checkEndphaseWhite() {
        // endphase white
        if (currentGame.checkEndphaseWhiteTarget(Constances.FIELD_END_WHITE)) {
            currentGame.setTargetNumber(Constances.FIELD_END_WHITE);
        } else {
            setResult(-1);
            getEndNumber(currentGame);
        }
    }

    public void getEndNumber(final IGame currentGame) {
        currentGame.setCurrentMethodName("getEndNumber");
        int targetNumber = getResult();
        try {

            targetNumber = getTargetResult(targetNumber);

            if (0 <= targetNumber && Constances.FIELD_EATEN_BLACK > targetNumber) {

                checkColorProblem(targetNumber);
                checkTargetValidness(targetNumber);
            } else if (Constances.FIELD_END_BLACK == targetNumber) {
                checkEndphaseBlack();
            } else if (Constances.FIELD_END_WHITE == targetNumber) {
                checkEndphaseWhite();
            }
        } catch (Exception ignored) {
            System.out.println("getEndNumber");
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        mouseHandler(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        mouseHandler(e);
    }

    private void mouseHandler(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        status = "x = " + x + ", y = " + y + "\t start = " + currentGame.getStartNumber() + ", target = " + currentGame.getTargetNumber() + ", result = " + getResult() + "; Current player: " + currentGame.getCurrentPlayer() + "; " + currentGame.printJumpsString() + "; Method: " + currentGame.getCurrentMethodName();
        update();
    }

    public void noMovesDialog() {
        JOptionPane.showMessageDialog(this, "No possible moves available", "Bad luck", JOptionPane.WARNING_MESSAGE);
    }

    public void winnerDialog() {
        JOptionPane.showMessageDialog(this, currentGame.getCurrentPlayer() + " is the winner!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
    }
}
