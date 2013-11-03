package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;
import de.htwg.util.observer.IObserver;
import de.htwg.util.observer.ResourceBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public final class BackgammonFrame
        extends JFrame
        implements MouseListener, MouseMotionListener, IObserver {

    private static final long serialVersionUID = 1L;
    private static final String GET_TARGET_WHILE_EATEN_WHITE = "gettargetwhileeatenwhite";

    private final IGame currentGame;
    private final StatusPanel statusPanel;
    private String status = "";
    private int result = -1;

    public BackgammonFrame(final IGame gm) {

        currentGame = gm;

        setTitle(ResourceBundle.getString("upfaz.backgammon"));
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
        currentGame.setCurrentMethodName(ResourceBundle.getString("gettargetwhileeatenblack"));
        int targetNumber = getResult();
        try {

            while (Constances.FIELD_EATEN_BLACK <= targetNumber || 18 > targetNumber) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                targetNumber = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
            System.out.println(ResourceBundle.getString("gettargetwhileeatenblack"));
        }

        return targetNumber;
    }

    public void getStartNumber(final IGame currentGame) {
        currentGame.setCurrentMethodName(ResourceBundle.getString("getstartnumber"));
        int startNumber = getResult();
        try {

            while (0 > startNumber || Constances.FIELD_EATEN_BLACK <= startNumber) {
                Thread.sleep(Constances.TIME_TO_SLEEP_IN_MS);
                startNumber = getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
            System.out.println(ResourceBundle.getString("getstartnumber"));
        }

        if (currentGame.checkStartNumber(startNumber)) {
            currentGame.setStartNumber(startNumber);
        } else {
            System.out.println(ResourceBundle.getString("you.can.t.move.this.piece.or.there.is.no.pieces"));
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
            System.out.printf(ResourceBundle.getString("gettargetresult.s.n"), e);
        }
        return newTarget;
    }

    private void checkColorProblem(final int targetNumber) {
        if (currentGame.getGameMap()[targetNumber].isNotJumpable(currentGame.getCurrentPlayer().getColor())) {
            System.out.println(ResourceBundle.getString("can.t.jump.this.field.color.problem"));
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
        currentGame.setCurrentMethodName(ResourceBundle.getString("getendnumber"));
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
            System.out.println(ResourceBundle.getString("getendnumber"));
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
        status = String.format(ResourceBundle.getString("x.d.y.d.t.start.d.target.d.result.d.current.player.s.s.method.s"), x, y, currentGame.getStartNumber(), currentGame.getTargetNumber(), getResult(), currentGame.getCurrentPlayer(), currentGame.printJumpsString(), currentGame.getCurrentMethodName());
        update();
    }

    public void noMovesDialog() {
        JOptionPane.showMessageDialog(this, ResourceBundle.getString("no.possible.moves.available"), ResourceBundle.getString("bad.luck"), JOptionPane.WARNING_MESSAGE);
    }

    public void winnerDialog() {
        JOptionPane.showMessageDialog(this, String.format(ResourceBundle.getString("s.is.the.winner"), currentGame.getCurrentPlayer()), ResourceBundle.getString("congratulations"), JOptionPane.PLAIN_MESSAGE);
    }
    @Override
    public String toString() {
        return String.format(ResourceBundle.getString("backgammonframe.currentgame.s.statuspanel.s.status.s.result.d"), currentGame, statusPanel, status, result);
    }
}
