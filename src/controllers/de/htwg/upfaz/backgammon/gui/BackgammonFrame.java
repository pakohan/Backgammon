package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.Game;
import controllers.de.htwg.util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public final class BackgammonFrame extends JFrame implements MouseListener,
		MouseMotionListener, IObserver {

	private static final long serialVersionUID = 1L;

	private final Game currentGame;
	private final StatusPanel statusPanel;

	public BackgammonFrame(final Game gm) {
		currentGame = gm;

		setTitle("Upfaz backgammon");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(Constances.DEFAULT_X_RESOLUTION,
				Constances.DEFAULT_Y_RESOLUTION);
		setResizable(false);
		final Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		/*
		 * bp = new BackgroundPanel(); pane.add(bp); dices = new
		 * Dices(currentGame.getJumps()); pane.add(dices); ds = new
		 * DrawStones(currentGame.getGameMap()); pane.add(ds);
		 */

		final DrawDesk test = new DrawDesk(currentGame);
		pane.add(test);
		statusPanel = new StatusPanel();
		pane.add(statusPanel, BorderLayout.PAGE_END);

		addMouseListener(this);
		addMouseMotionListener(this);

		setVisible(true);
		repaint();

	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		// convert coordinates in field number
		currentGame.setResult(getClickedField(x, y));

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

	public static int getClickedField(final int x, final int y) {

		final int left = getLeft(x);
		int output = -1;

		if (y <= Constances.UPPER_CENTER_BORDER && y >= Constances.UPPER_BORDER
				&& left >= 0) {
			output = 11 - left;
		} else if (y >= Constances.DOWN_CENTER_BORDER
				&& y <= Constances.DOWN_BORDER && left >= 0) {
			output = 12 + left;
		}

		output = getClickedEndfield(output, x, y);

		return output;
	}

	private static int getClickedEndfield(final int oldOutput, final int x,
			final int y) {

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
		// mouseHandler(e);
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	public void update() {
		statusPanel.setText(currentGame.getStatus());
		repaint();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		// mouseHandler(e);
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		// mouseHandler(e);
	}

	private void mouseHandler(final MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		currentGame.setStatus(x + ":" + y + " -#- " + currentGame.toString());
		update();
	}

	public void winnerDialog() {
		JOptionPane.showMessageDialog(this, currentGame.getCurrentPlayer()
				+ " is the winner!", "Congratulations!",
				JOptionPane.PLAIN_MESSAGE);
	}
}
