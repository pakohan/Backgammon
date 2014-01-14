package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.Field;
import controllers.de.htwg.upfaz.backgammon.entities.IField;
import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name ="MAP")
public final class GameMap
        implements Serializable {

    public static final int TOTAL_FIELDS_NR = 28;
    public static final int FIELD_EATEN_WHITE = 25;
    public static final int FIELD_EATEN_BLACK = 24;
    public static final int FIELD_END_WHITE = 27;
    public static final int FIELD_END_BLACK = 26;

    @Id
    private String uuid;
    @OneToOne(cascade = CascadeType.ALL)
    private Dice dice;
    @Column(name = "REVISION")
    private String revision;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private Field[] gameMap;
    @Transient
    private IPlayer players;
    @Column(name = "FIRST_CLICK")
    private int firstClick = -1;
    @Column(name = "SECOND_CLICK")
    private int secondClick = -1;
    @Column(name = "WINNER")
    private int winner = -1;
    @Column(name = "END_PHASE")
    private boolean endPhase = false;

    public GameMap(final IPlayer players, final Dice dice) {
        this.uuid = UUID.randomUUID().toString();
        this.players = players;
        this.dice = dice;
        gameMap = new Field[TOTAL_FIELDS_NR];

        for (int i = 0; i < gameMap.length; i++) {
            gameMap[i] = new Field(i);
        }

        initStones();
    }

    public GameMap() {
        this.players = new Players();
        this.dice = new Dice();
    }

    @JsonIgnore
    public void moveStone(final int startNumber, final int targetNumber) {
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        gameMap[targetNumber].setStoneColor(players.getColor());
        final int oldStones = gameMap[startNumber].getNumberStones();
        final int newStones = oldStones - 1;
        gameMap[startNumber].setNumberStones(newStones);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
    }

    @JsonIgnore
    public void eatStone(final int startNumber, final int targetNumber) {
        gameMap[targetNumber].setNumberStones(1);
        gameMap[targetNumber].setStoneColor(players.getColor());
        if (players.getColor() == Players.PLAYER_COLOR_WHITE) {
            gameMap[FIELD_EATEN_BLACK].setNumberStones(gameMap[FIELD_EATEN_BLACK].getNumberStones() + 1);
            gameMap[FIELD_EATEN_BLACK].setStoneColor(1);
        } else {
            gameMap[FIELD_EATEN_WHITE].setNumberStones(gameMap[FIELD_EATEN_WHITE].getNumberStones() + 1);
            gameMap[FIELD_EATEN_WHITE].setStoneColor(0);
        }
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
    }

    @JsonIgnore
    public IField getField(final int targetNumber) {
        return gameMap[targetNumber];
    }

    @JsonProperty("field")
    public Field[] getFields() {
        return gameMap;
    }

    @JsonProperty("field")
    public void setFields(final Field[] fields) {
        this.gameMap = fields;
    }

    @JsonProperty("currentPlayer")
    public String getCurrentPlayer() {
        return players.toString();
    }

    @JsonIgnore
    public boolean isWhiteEaten() {
        return players.getColor() == Players.PLAYER_COLOR_WHITE && getField(FIELD_EATEN_WHITE).getNumberStones() > 0;
    }

    @JsonIgnore
    public boolean isBlackEaten() {
        return players.getColor() == Players.PLAYER_COLOR_BLACK && getField(FIELD_EATEN_BLACK).getNumberStones() > 0;
    }

    @JsonIgnore
    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    @JsonIgnore
    public boolean isNew() {
        return revision == null || revision.equals("");
    }

    @JsonIgnore
    public void setUuid(final UUID uuid) {
        this.uuid = uuid.toString();
    }

    @JsonIgnore
    public String get_rev() {
        return revision;
    }

    @JsonIgnore
    public void set_rev(final String rev) {
        this.revision = rev;
    }

    @JsonIgnore
    private void initStones() {

        gameMap[0].setStoneColor(0);
        gameMap[0].setNumberStones(2);

        gameMap[11].setStoneColor(0);
        gameMap[11].setNumberStones(5);

        gameMap[16].setStoneColor(0);
        gameMap[16].setNumberStones(3);

        gameMap[18].setStoneColor(0);
        gameMap[18].setNumberStones(5);

        gameMap[23].setStoneColor(1);
        gameMap[23].setNumberStones(2);

        gameMap[12].setStoneColor(1);
        gameMap[12].setNumberStones(5);

        gameMap[7].setStoneColor(1);
        gameMap[7].setNumberStones(3);

        gameMap[5].setStoneColor(1);
        gameMap[5].setNumberStones(5);

        gameMap[FIELD_EATEN_BLACK].setStoneColor(1); // 24
        gameMap[FIELD_EATEN_WHITE].setStoneColor(0); // 25

        gameMap[FIELD_END_BLACK].setStoneColor(1); // 26
        gameMap[FIELD_END_WHITE].setStoneColor(0); // 27

		/*
         * endspiel tests gameMap[3].setStoneColor(1); gameMap[4].setStoneColor(1);
		 * gameMap[5].setStoneColor(1); gameMap[3].setNumberStones(5);
		 * gameMap[4].setNumberStones(5); gameMap[5].setNumberStones(5);
		 *
		 * gameMap[18].setStoneColor(0); gameMap[19].setStoneColor(0);
		 * gameMap[20].setStoneColor(0); gameMap[18].setNumberStones(5);
		 * gameMap[19].setNumberStones(5); gameMap[20].setNumberStones(5);
		 */
    }

    @JsonIgnore
    public boolean checkForWinner() {
        return gameMap[FIELD_END_BLACK].getNumberStones() == Constances.STONES_TO_WIN || gameMap[FIELD_END_WHITE].getNumberStones() == Constances.STONES_TO_WIN;
    }

    @JsonProperty("_rev")
    public String getRevision() {
        return revision;
    }

    @JsonProperty("_rev")
    public void setRevision(final String revision) {
        this.revision = revision;
    }

    @JsonProperty("_id")
    public String getId() {
        return uuid.toString();
    }

    @JsonProperty("_id")
    public void setId(final String id) {
        this.uuid = id;
    }

    @JsonProperty("dice")
    public Dice getDice() {
        return dice;
    }

    @JsonProperty("dice")
    public void setDice(final Dice dice) {
        this.dice = dice;
    }

    public int getFirstClick() {
        return firstClick;
    }

    public void setFirstClick(final int firstClick) {
        this.firstClick = firstClick;
    }
    public int getSecondClick() {
        return secondClick;
    }
    public void setSecondClick(final int secondClick) {
        this.secondClick = secondClick;
    }
    public int getWinner() {
        return winner;
    }
    public void setWinner(final int winner) {
        this.winner = winner;
    }
    public boolean isEndPhase() {
        return endPhase;
    }
    public void setEndPhase(final boolean endPhase) {
        this.endPhase = endPhase;
    }
    @JsonIgnore
    public IPlayer getPlayers() {
        return players;
    }

    @Override
    @JsonProperty("status")
    public String toString() {
        if (checkForWinner()) {
            return String.format(Constances.PLAYER_S_IS_THE_WINNER, getCurrentPlayer());
        } else if (isEndPhase()) {
            return "End Phase!";
        } else {
            return String.format("start = %d, target = %d; Current player: %s", getFirstClick(), getSecondClick(), getCurrentPlayer());
        }
    }

    
}
