package controllers.de.htwg.upfaz.backgammon.controller;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Random;

public final class Dice {

    private static final Random rand = new Random(System.currentTimeMillis());


    private int numberTurnsLeft = 2;
    private int numberTurns = 2;

    private int[] values;
    private int[] valuesToDraw;

    public Dice() {
        values = new int[IGame.MAX_JUMPS];
        valuesToDraw = new int[IGame.MAX_JUMPS];

        rollTheDice();
    }

    @JsonIgnore
    public void rollTheDice() {

        for (int index = 0; index < 2; index++) {
            values[index] = rand.nextInt(IGame.DICE_RANDOM) + 1;
        }

        if (values[0] == values[1]) {
            values[2] = values[0];
            values[3] = values[0];
            numberTurns = 4;
            numberTurnsLeft = 4;
        }
        valuesToDraw = values.clone();
    }

    @JsonIgnore
    public boolean hasTurnsLeft() {
        return numberTurnsLeft > 0;
    }

    @JsonIgnore
    public int getDiceToDraw(final int i) {
        return valuesToDraw[i];
    }

    @JsonIgnore
    public int getDiceAt(final int i) {
        return values[i];
    }

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
    public boolean checkDistance(final int distance) {
        boolean returnVal = false;

        for (final int val : values) {
            returnVal = distance == val;
            if (returnVal) {
                break;
            }
        }

        return returnVal;
    }

    @JsonProperty("values")
    public int[] getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(final int[] values) {
        this.values = values;
    }

    @JsonProperty("valuesToDraw")
    public int[] getValuesToDraw() {
        return valuesToDraw;
    }

    @JsonProperty("valuesToDraw")
    public void setValuesToDraw(final int[] valuesToDraw) {
        this.valuesToDraw = valuesToDraw;
    }

    public int getNumberTurns() {
        return numberTurns;
    }

    public int getNumberTurnsLeft() {
        return numberTurnsLeft;
    }

    public void setNumberTurnsLeft(int numberTurnsLeft) {
        this.numberTurnsLeft = numberTurnsLeft;
    }

    public void setNumberTurns(int numberTurns) {
        this.numberTurns = numberTurns;
    }
}
