package controllers.de.htwg.upfaz.backgammon.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.de.htwg.upfaz.backgammon.entities.Field;
import controllers.de.htwg.upfaz.backgammon.entities.IField;
import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;

import java.util.Arrays;
import java.util.UUID;

public final class GameMap {

    public static final int TOTAL_FIELDS_NR = 28;
    public static final int FIELD_EATEN_WHITE = 25;
    public static final int FIELD_EATEN_BLACK = 24;
    public static final int FIELD_END_WHITE = 27;
    public static final int FIELD_END_BLACK = 26;
    private final Dice dice;
    private UUID uuid;
    private String revision;

    private Field[] gameMap;
    private IPlayer players;

    public GameMap(final IGame game, final IPlayer players, final Dice dice) {
        this.uuid = UUID.randomUUID();
        this.players = players;
        this.dice = dice;
        gameMap = new Field[TOTAL_FIELDS_NR];

        for (int i = 0; i < gameMap.length; i++) {
            gameMap[i] = new Field(i);
        }

        initStones();
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

    @JsonIgnore
    public IField[] getFields() {
        return gameMap;
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
        return uuid;
    }

    @JsonIgnore
    public boolean isNew() {
        return revision == null || revision.equals("");
    }

    @JsonIgnore
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("_rev")
    public String getRevision() {
        return revision;
    }

    @JsonProperty("_rev")
    public void setRevision(final String revision) {
        this.revision = revision;
    }

    /**
     * Gets the revision.
     * <p>
     * Note: This duplicated method is necessary for Forms.bindFromRequest() of
     * Play!.
     * </p>
     *
     * @return The revision.
     */
    @JsonIgnore
    public String get_rev() {
        return revision;
    }

    /**
     * Sets the revision.
     * <p>
     * Note: This duplicated method is necessary for Forms.bindFromRequest() of
     * Play!.
     * </p>
     *
     * @param rev The revision.
     */
    @JsonIgnore
    public void set_rev(final String rev) {
        this.revision = rev;
    }

    @JsonProperty("_id")
    public String getId() {
        return uuid.toString();
    }

    @JsonProperty("_id")
    public void setId(final String id) {
        this.uuid = UUID.fromString(id);
    }

    @JsonIgnore
    public int getRev() {
        final String[] strings = revision.split("-");
        return Integer.parseInt(strings[0]);
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

    @JsonIgnore
    @Override
    public String toString() {
        return "GameMap{" +
                "dice=" + dice +
                ", uuid=" + uuid +
                ", revision='" + revision + '\'' +
                ", gameMap=" + Arrays.toString(gameMap) +
                ", players=" + players +
                '}';
    }
}
