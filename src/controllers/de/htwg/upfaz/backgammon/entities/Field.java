package controllers.de.htwg.upfaz.backgammon.entities;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
    @Override
    public int getFieldNr() {
        return fieldNr;
    }

    @JsonProperty("nr")
    @Override
    public void setFieldNr(final int newFieldNr) {
        fieldNr = newFieldNr;
    }

    @JsonProperty("amount")
    @Override
    public void setNumberStones(final int newVar) {
        numberStones = newVar;
        if (newVar == 0) {
            stoneColor = -1;
        }
    }

    @JsonProperty("amount")
    @Override
    public int getNumberStones() {
        return numberStones;
    }

    @JsonProperty("color")
    @Override
    public int getStoneColor() {
        return stoneColor;
    }

    @JsonProperty("color")
    @Override
    public void setStoneColor(final int newColor) {
        stoneColor = newColor;
    }

    @JsonIgnore
    @Override
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
