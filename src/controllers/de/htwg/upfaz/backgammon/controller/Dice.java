package controllers.de.htwg.upfaz.backgammon.controller;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name = "DICE")
public final class Dice implements Serializable {

    private static final Random rand = new Random(System.currentTimeMillis());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NUMBER_TURNS_LEFT")
    private int numberTurnsLeft = 2;
    @Column(name = "NUMBER_TURNS")
    private int numberTurns = 2;

    @Column(name = "VALUES_")
    @Type(type = "org.hibernate.type.WrappedMaterializedBlobType")
    private Byte[] values;

    @Column(name = "VALUES_TO_DRAW")
    @Type(type = "org.hibernate.type.WrappedMaterializedBlobType")
    private Byte[] valuesToDraw;

    public Dice() {
        values = new Byte[IGame.MAX_JUMPS];
        valuesToDraw = new Byte[IGame.MAX_JUMPS];

        rollTheDice();
    }

    @JsonIgnore
    public void rollTheDice() {
        for (int index = 0; index < 2; index++) {
            values[index] = (byte) (rand.nextInt(IGame.DICE_RANDOM) + 1);
        }

        values[2] = 0;
        values[3] = 0;

        if (values[0] == values[1]) {
            values[2] = values[0].byteValue();
            values[3] = values[0].byteValue();
            numberTurns = 4;
            numberTurnsLeft = 4;
        } else {
            numberTurns = 2;
            numberTurnsLeft = 2;
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
    public Byte[] getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(final Byte[] values) {
        this.values = values;
    }

    @JsonProperty("valuesToDraw")
    public Byte[] getValuesToDraw() {
        return valuesToDraw;
    }

    @JsonProperty("valuesToDraw")
    public void setValuesToDraw(final Byte[] valuesToDraw) {
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

    @Basic
    @JsonIgnore
    public long getId() {
        return id;
    }

    @Basic
    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }
}
