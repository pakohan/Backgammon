package controllers.de.htwg.upfaz.backgammon.controller;

import java.util.Random;

public final class Dice {

    private static final Random rand = new Random(System.currentTimeMillis());

    private int[] values;
    private int numberTurnsLeft = 2;
    private int numberTurns = 2;

    public Dice() {
        rollTheDice();
    }

    private void rollTheDice() {
        values = new int[IGame.MAX_JUMPS];

        for (int index = 0; index < 2; index++) {
            values[index] = rand.nextInt(IGame.DICE_RANDOM) + 1;
        }

        if (values[0] == values[1]) {
            values[2] = values[0];
            values[3] = values[0];
            numberTurns = 4;
            numberTurnsLeft = 4;
        }
    }

    public int getNumberTurns() {
        return numberTurns;
    }

    public int getNumberTurnsLeft() {
        return numberTurnsLeft;
    }

    public boolean hasTurnsLeft() {
        return numberTurnsLeft > 0;
    }

    public int getDiceAt(final int i) {
        return values[i];
    }

    public boolean move(final int distance) {
        boolean returnVal = false;
        if (numberTurnsLeft == 0) {
            return returnVal;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] == distance) {
                values[i] = 0;
                numberTurnsLeft--;
                returnVal = true;
                break;
            }
        }

        return returnVal;
    }

    public boolean renewJumpsEndPhase(final int start) {
        boolean returnVal = false;
        if (numberTurnsLeft == 0) {
            return returnVal;
        }

        for (int i = 0; i < values.length; i++) {
            if (24 - start == values[i] || start + 1 == values[i]) {
                values[i] = 0;
                numberTurnsLeft--;
                returnVal = true;
                break;
            }
        }

        return returnVal;
    }

    public boolean checkNormalEndTarget(final int newTarget) {
        boolean returnVal = false;

        for (final int val : values) {
            returnVal = newTarget == val;
            if (returnVal) {
                break;
            }
        }

        return returnVal;
    }
}
