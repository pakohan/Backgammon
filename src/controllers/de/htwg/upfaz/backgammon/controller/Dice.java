package controllers.de.htwg.upfaz.backgammon.controller;

import java.util.Random;

public final class Dice {

    private static final Random rand = new Random(System.currentTimeMillis());

    private int[] values;
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
        }
    }

    public int getNumberTurns() {
        return numberTurns;
    }

    public boolean move(int distance) {
        boolean returnVal = false;
        if (numberTurns == 0) {
            return returnVal;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] == distance) {
                values[i] = 0;
                numberTurns--;
                returnVal = true;
            }
        }

        return returnVal;
    }
}
