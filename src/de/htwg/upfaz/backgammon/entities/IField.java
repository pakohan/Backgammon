package de.htwg.upfaz.backgammon.entities;

public interface IField {

    int getFieldNr();

    void setFieldNr(int newFieldNr);

    void setNumberStones(int newVar);

    int getNumberStones();

    int getStoneColor();

    void setStoneColor(int newColor);

    boolean isNotJumpable(int color);

    // --Commented out by Inspection (02.11.13 18:11):public abstract boolean isNotJumpable(int color);

    // --Commented out by Inspection (02.11.13 18:11):public abstract boolean isEmpty();

    String toString();
}