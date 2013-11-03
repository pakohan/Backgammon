package de.htwg.upfaz.backgammon.entities;

public final class Field
        implements IField {

    //
    // Fields
    //

    private int fieldNr;
    private int numberStones;
    private int stoneColor;

    private static final int TEN_STONES = 10;

    //
    // Constructors
    //
    public Field(final int fn) {

        fieldNr = fn;
        numberStones = 0;
        stoneColor = -1;
    }

    @Override
    public int getFieldNr() {
        return fieldNr;
    }

    @Override
    public void setFieldNr(final int newFieldNr) {
        fieldNr = newFieldNr;
    }

    @Override
    public void setNumberStones(final int newVar) {
        numberStones = newVar;
        if (newVar == 0) {
            stoneColor = -1;
        }
    }

    @Override
    public int getNumberStones() {
        return numberStones;
    }

    @Override
    public int getStoneColor() {
        return stoneColor;
    }

    @Override
    public void setStoneColor(final int newColor) {
        stoneColor = newColor;
    }

    @Override
    public boolean isNotJumpable(final int color) {
        return numberStones > 1 && color != stoneColor;
    }

    // --Commented out by Inspection START (02.11.13 18:14):
    //    @Override
    //    public boolean isNotJumpable(int color) {
    //        return getNumberStones() > 1 && color != getStoneColor();
    //    }
    // --Commented out by Inspection STOP (02.11.13 18:14)

    // --Commented out by Inspection START (02.11.13 18:14):
    //    @Override
    //    public boolean isEmpty() {
    //        return numberStones == 0;
    //    }
    // --Commented out by Inspection STOP (02.11.13 18:14)

    @Override
    public String toString() {

        final StringBuilder b = new StringBuilder();
        if (numberStones == 0) {
            return "   ";
        }
        if (numberStones < TEN_STONES) {
            b.append("0");
        }
        b.append(numberStones);
        if (stoneColor == 0) {
            b.append("w");
        } else {
            b.append("b");
        }

        return b.toString();
    }
}
