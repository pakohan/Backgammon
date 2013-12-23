package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.GameNew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

public final class BackgammonFrame
        extends JFrame
        implements MouseListener, MouseMotionListener, Observer {

    private static final long serialVersionUID = 1L;

    //private final Game currentGame;
    private final GameNew currentGame;
    private final StatusPanel statusPanel;
    private int x;
    private int y;

    public BackgammonFrame(final GameNew gm) {
        currentGame = gm;

        setTitle("Upfaz backgammon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constances.DEFAULT_X_RESOLUTION, Constances.DEFAULT_Y_RESOLUTION);
        setResizable(false);
        final Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        final DrawDesk gameField = new DrawDesk(currentGame);
        pane.add(gameField);
        statusPanel = new StatusPanel();
        pane.add(statusPanel, BorderLayout.PAGE_END);

        addMouseListener(this);
        addMouseMotionListener(this);

        setVisible(true);
        statusPanel.setText(currentGame.toString());
        repaint();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();
        // convert coordinates in field number
        //currentGame.setResult(getClickedField(x, y));

        currentGame.click(getClickedField(x, y));
    }
    @Override
    public void update(final Observable o, final Object arg) {
        statusPanel.setText(x + ":" + y + " -#- " + currentGame.toString());
        repaint();
        if (currentGame.getWinner() != -1) {
            winnerDialog();
            currentGame.setWinner(-1);
            startNewGameDialog();
        }
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

    public static int getClickedField(final int x, final int y) {

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
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        // mouseHandler(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        update(null, null);
    }

    public void winnerDialog() {
        JOptionPane.showMessageDialog(this, currentGame.getCurrentPlayer() + " is the winner!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
    }

    private void startNewGameDialog() {
        JOptionPane.showMessageDialog(this, "Start new game?", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
    }
}
