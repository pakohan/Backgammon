package de.htwg.upfaz.backgammon.entities;

public interface IField {

    public abstract int getFieldNr();

    public abstract void setFieldNr(int newFieldNr);

    public abstract void setNumberStones(int newVar);

    public abstract int getNumberStones();

    public abstract int getStoneColor();

    public abstract void setStoneColor(int newColor);

    public abstract boolean isJumpable(int color);

    public abstract boolean isNotJumpable(int color);

    public abstract boolean isEmpty();

    public abstract String toString();
}
