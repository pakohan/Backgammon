package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public final class BackgammonFrame
        extends JFrame
        implements MouseListener, MouseMotionListener, Observer, WindowListener {

    private static final long serialVersionUID = 1L;
    private static final int PIXELS = 340;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;

    private final Core currentGame;
    private final StatusPanel statusPanel;
    private int x;
    private int y;

    public BackgammonFrame(final Core gm) {
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

    public void mouseClicked(final MouseEvent e) {
        final int xx = e.getX();
        final int yy = e.getY();

        currentGame.click(getClickedField(xx, yy));
    }
    public void update(final Observable o, final Object arg) {
        statusPanel.setText(x + ":" + y + " -#- " + currentGame.toString());
        repaint();
        if (currentGame.getWinner() != -1) {
            winnerDialog();
            currentGame.setWinner(-1);
            startNewGameDialog();
        }
    }
    public void windowOpened(final WindowEvent e) {
    }
    public void windowClosing(final WindowEvent e) {
    }
    public void windowClosed(final WindowEvent e) {
    }
    public void windowIconified(final WindowEvent e) {
    }
    public void windowDeiconified(final WindowEvent e) {
    }
    public void windowActivated(final WindowEvent e) {
    }
    public void windowDeactivated(final WindowEvent e) {
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
            output = ELEVEN - left;
        } else if (y >= Constances.DOWN_CENTER_BORDER && y <= Constances.DOWN_BORDER && left >= 0) {
            output = TWELVE + left;
        }

        output = getClickedEndfield(output, x, y);

        return output;
    }

    private static int getClickedEndfield(final int oldOutput, final int x, final int y) {

        int newOutput = oldOutput;

        if (x > Constances.LEFT_OUT_BORDER && y < Constances.RIGHT_OUT_BORDER) {
            if (y < PIXELS) {
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

    public void mouseEntered(final MouseEvent e) {
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mousePressed(final MouseEvent e) {
    }

    public void mouseReleased(final MouseEvent e) {
    }

    public void mouseDragged(final MouseEvent e) {
    }

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
