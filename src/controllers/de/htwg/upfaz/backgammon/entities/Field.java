package controllers.de.htwg.upfaz.backgammon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FIELD")
public final class Field
        implements IField,Serializable {

    private static final int TEN_STONES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NR")
    private int fieldNr;
    @Column(name = "NUMBER_STONES")
    private int numberStones;
    @Column(name = "STONE_COLOR")
    private int stoneColor;

    public Field(final int fn) {

        fieldNr = fn;
        numberStones = 0;
        stoneColor = -1;
    }

    public Field() {    }

    @JsonProperty("nr")
    public int getFieldNr() {
        return fieldNr;
    }

    @JsonProperty("nr")
    public void setFieldNr(final int newFieldNr) {
        fieldNr = newFieldNr;
    }

    @JsonProperty("amount")
    public void setNumberStones(final int newVar) {
        numberStones = newVar;
        if (newVar == 0) {
            stoneColor = -1;
        }
    }

    @JsonProperty("amount")
    public int getNumberStones() {
        return numberStones;
    }

    @JsonProperty("color")
    public int getStoneColor() {
        return stoneColor;
    }

    @JsonProperty("color")
    public void setStoneColor(final int newColor) {
        stoneColor = newColor;
    }

    @JsonIgnore
    public boolean isNotJumpable(final int color) {
        return numberStones > 1 && color != stoneColor;
    }

    @JsonIgnore
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

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }
}
