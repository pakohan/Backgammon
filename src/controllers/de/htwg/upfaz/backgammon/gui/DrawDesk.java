package controllers.de.htwg.upfaz.backgammon.gui;

import controllers.de.htwg.upfaz.backgammon.controller.Game;

import javax.swing.*;
import java.awt.*;

public final class DrawDesk
        extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int BORDER = 30;
    private static final int INT_25 = 25;
    private static final int DRAW_STONE_STEP = INT_25;
    private static final int INT_645 = 645;
    private static final int INT_18 = 18;
    private static final int INT_24 = 24;
    private static final int INT_26 = 26;
    private static final int INT_27 = 27;
    private static final int INT_575 = 575;
    private static final int INT_35 = 35;
    private static final int INT_12 = 12;
    private static final int INT_11 = 11;
    private static final int INT_565 = 565;
    private static final int INT_23 = 23;
    private final Image dice1;
    private final Image dice2;
    private final Image dice3;
    private final Image dice4;
    private final Image dice5;
    private final Image dice6;
    private final Image stoneb;
    private final Image stonew;
    private final Image background;
    private final Game currentGame;

    public DrawDesk(final Game game) {

        currentGame = game;

        background = getImage("background");
        dice1 = getImage("dice1");
        dice2 = getImage("dice2");
        dice3 = getImage("dice3");
        dice4 = getImage("dice4");
        dice5 = getImage("dice5");
        dice6 = getImage("dice6");
        stoneb = getImage("grau");
        stonew = getImage("weiss");
    }
    private static Image getImage(final String file) {
        final String seperator = System.getProperty("file.separator");
        return Toolkit.getDefaultToolkit().createImage(String.format("res%s%s.png", seperator, file));
    }

    void paintStones(final Graphics g, final Image image, final int x, final int y, final int a) {
        for (int i = 0; i < currentGame.getGameMap()[a].getNumberStones(); i++) {
            if (a < INT_12 || a == Constances.FIELD_EATEN_BLACK || a == Constances.FIELD_END_BLACK) {
                g.drawImage(image, x, y + i * DRAW_STONE_STEP, null);
            } else {
                g.drawImage(image, x, y - i * DRAW_STONE_STEP, null);
            }
        }
    }

    private void getCoordinates(final Graphics g, final Image imageToDraw, final int a) {
        int xCoordinate = INT_645;

        if (a >= 6 && a < INT_18) {
            xCoordinate -= 2 * BORDER;
        } else {
            switch (a) {
            case INT_24:
                paintStones(g, imageToDraw, Constances.EATEN_LEFT_BORDER, INT_35, a);
                break;
            case INT_25:
                paintStones(g, imageToDraw, Constances.EATEN_LEFT_BORDER, INT_575, a);
                break;
            case INT_26:
                paintStones(g, imageToDraw, Constances.LEFT_OUT_BORDER, INT_35, a);
                break;
            case INT_27:
                paintStones(g, imageToDraw, Constances.LEFT_OUT_BORDER, INT_575, a);
                break;
            default:
                break;
            }
        }

        final int yCoordinate;
        if (a < INT_12) {
            yCoordinate = INT_35;
            xCoordinate -= a % INT_12 * Constances.FIELD_SIZE_PXL;
            paintStones(g, imageToDraw, xCoordinate, yCoordinate, a);
        } else if (a > INT_11 && a < INT_24) {
            yCoordinate = INT_565;
            xCoordinate -= (INT_23 - a) % INT_12 * Constances.FIELD_SIZE_PXL;
            paintStones(g, imageToDraw, xCoordinate, yCoordinate, a);
        }
    }

    private void drawStones(final Graphics g) {
        for (int i = 0; i < Constances.TOTAL_FIELDS_NR; i++) {
            final Image imageToDraw;
            if (currentGame.getGameMap()[i].getStoneColor() == 0) {
                imageToDraw = stonew;
            } else {
                imageToDraw = stoneb;
            }
            getCoordinates(g, imageToDraw, i);
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        // TODO find out if needed or if breaking sth: super.paintComponent(g);

        g.drawImage(background, 0, 0, null);
        drawStones(g);

        for (int i = 0; i < 2; i++) {

            drawDices(g, i);
        }
    }

    private void drawDices(final Graphics g, final int i) {
        final int diceX = Constances.DICE_X + i * Constances.DICE_SIZE;

        try {

            switch (currentGame.getJumpsT()[i]) {
            case 1:
                g.drawImage(dice1, diceX, Constances.DICE_Y, null);
                break;
            case 2:
                g.drawImage(dice2, diceX, Constances.DICE_Y, null);
                break;
            case 3:
                g.drawImage(dice3, diceX, Constances.DICE_Y, null);
                break;
            case 4:
                g.drawImage(dice4, diceX, Constances.DICE_Y, null);
                break;
            case 5:
                g.drawImage(dice5, diceX, Constances.DICE_Y, null);
                break;
            case 6:
                g.drawImage(dice6, diceX, Constances.DICE_Y, null);
                break;
            default:
                break;
            }
        } catch (Exception e) {
            Log.verbose(e);
        }
    }

    @Override
    public String toString() {
        return String.format("DrawTest{dice1=%s, dice2=%s, dice3=%s, dice4=%s, dice5=%s, dice6=%s, stoneb=%s, stonew=%s, background=%s, currentGame=%s}", dice1, dice2, dice3, dice4, dice5, dice6, stoneb, stonew, background, currentGame);
    }
}
