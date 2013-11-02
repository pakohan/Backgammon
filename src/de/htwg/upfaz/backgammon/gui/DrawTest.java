package de.htwg.upfaz.backgammon.gui;

import de.htwg.upfaz.backgammon.controller.IGame;

import javax.swing.*;
import java.awt.*;

public class DrawTest
        extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int BORDER = 30;
    private static final int DRAW_STONE_STEP = 25;
    private Image dice1;
    private Image dice2;
    private Image dice3;
    private Image dice4;
    private Image dice5;
    private Image dice6;
    private Image stoneb;
    private Image stonew;
    private Image background;
    private final IGame currentGame;

    public DrawTest(final IGame game) {

        currentGame = game;

        setBackground(Toolkit.getDefaultToolkit().createImage("res/background.png"));
        setDice1(Toolkit.getDefaultToolkit().createImage("res/dice1.png"));
        setDice2(Toolkit.getDefaultToolkit().createImage("res/dice2.png"));
        setDice3(Toolkit.getDefaultToolkit().createImage("res/dice3.png"));
        setDice4(Toolkit.getDefaultToolkit().createImage("res/dice4.png"));
        setDice5(Toolkit.getDefaultToolkit().createImage("res/dice5.png"));
        setDice6(Toolkit.getDefaultToolkit().createImage("res/dice6.png"));
        setStoneB(Toolkit.getDefaultToolkit().createImage("res/grau.png"));
        setStoneW(Toolkit.getDefaultToolkit().createImage("res/weiss.png"));
    }

    void paintStones(final Graphics g, final Image image, final int x, final int y, final int a) {
        for (int i = 0; i < currentGame.getGameMap()[a].getNumberStones(); i++) {
            if (12 > a || Constances.FIELD_EATEN_BLACK == a || Constances.FIELD_END_BLACK == a) {
                g.drawImage(image, x, y + i * DRAW_STONE_STEP, null);
            } else {
                g.drawImage(image, x, y - i * DRAW_STONE_STEP, null);
            }
        }
    }

    private void getCoordinates(final Graphics g, final Image imageToDraw, final int a) {
        int xCoordinate = 645;

        if (6 <= a && 18 > a) {
            xCoordinate -= 2 * BORDER;
        } else {
            switch (a) {
            case 24:
                paintStones(g, imageToDraw, Constances.EATEN_LEFT_BORDER, 35, a);
                break;
            case 25:
                paintStones(g, imageToDraw, Constances.EATEN_LEFT_BORDER, 575, a);
                break;
            case 26:
                paintStones(g, imageToDraw, Constances.LEFT_OUT_BORDER, 35, a);
                break;
            case 27:
                paintStones(g, imageToDraw, Constances.LEFT_OUT_BORDER, 575, a);
                break;
            default:
                break;
            }
        }

        final int yCoordinate;
        if (12 > a) {
            yCoordinate = 35;
            xCoordinate -= a % 12 * Constances.FIELD_SIZE_PXL;
            paintStones(g, imageToDraw, xCoordinate, yCoordinate, a);
        } else if (11 < a && 24 > a) {
            yCoordinate = 565;
            xCoordinate -= (23 - a) % 12 * Constances.FIELD_SIZE_PXL;
            paintStones(g, imageToDraw, xCoordinate, yCoordinate, a);
        }
    }

    private void drawStones(final Graphics g) {
        for (int i = 0; Constances.TOTAL_FIELDS_NR > i; i++) {
            final Image imageToDraw;
            if (0 == currentGame.getGameMap()[i].getStoneColor()) {
                imageToDraw = getStoneW();
            } else {
                imageToDraw = getStoneB();
            }
            getCoordinates(g, imageToDraw, i);
        }
    }

    @Override
    public void paintComponent(final Graphics g) {

        g.drawImage(getBack(), 0, 0, null);
        drawStones(g);

        for (int i = 0; 2 > i; i++) {

            drawDices(g, i);
        }
    }

    private void drawDices(final Graphics g, final int i) {
        final int diceX = Constances.DICE_X + i * Constances.DICE_SIZE;

        try {

            switch (currentGame.getJumpsT()[i]) {
            case 1:
                g.drawImage(getDice1(), diceX, Constances.DICE_Y, null);
                break;
            case 2:
                g.drawImage(getDice2(), diceX, Constances.DICE_Y, null);
                break;
            case 3:
                g.drawImage(getDice3(), diceX, Constances.DICE_Y, null);
                break;
            case 4:
                g.drawImage(getDice4(), diceX, Constances.DICE_Y, null);
                break;
            case 5:
                g.drawImage(getDice5(), diceX, Constances.DICE_Y, null);
                break;
            case 6:
                g.drawImage(getDice6(), diceX, Constances.DICE_Y, null);
                break;
            default:
                // System.out.println("dafuq?");
                break;
            }
        } catch (Exception ignored) {
            System.out.println("Dices.paint() - something went wrong");
        }
    }

    private void setDice1(final Image newImage) {
        dice1 = newImage;
    }

    private void setDice2(final Image newImage) {
        dice2 = newImage;
    }

    private void setDice3(final Image newImage) {
        dice3 = newImage;
    }

    private void setDice4(final Image newImage) {
        dice4 = newImage;
    }

    private void setDice5(final Image newImage) {
        dice5 = newImage;
    }

    private void setDice6(final Image newImage) {
        dice6 = newImage;
    }

    private void setStoneB(final Image newImage) {
        stoneb = newImage;
    }

    private void setStoneW(final Image newImage) {
        stonew = newImage;
    }

    private void setBackground(final Image newImage) {
        background = newImage;
    }

    private Image getDice1() {
        return dice1;
    }

    private Image getDice2() {
        return dice2;
    }

    private Image getDice3() {
        return dice3;
    }

    private Image getDice4() {
        return dice4;
    }

    private Image getDice5() {
        return dice5;
    }

    private Image getDice6() {
        return dice6;
    }

    private Image getStoneB() {
        return stoneb;
    }

    private Image getStoneW() {
        return stonew;
    }

    private Image getBack() {
        return background;
    }
}
