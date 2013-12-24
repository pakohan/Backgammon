package controllers.de.htwg.upfaz.backgammon.entities;

public interface IField {

    int getFieldNr();

    void setFieldNr(int newFieldNr);

    void setNumberStones(int newVar);

    int getNumberStones();

    int getStoneColor();

    void setStoneColor(int newColor);

    boolean isNotJumpable(int color);

    String toString();
}
