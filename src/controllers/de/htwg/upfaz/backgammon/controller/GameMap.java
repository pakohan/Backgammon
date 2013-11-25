package controllers.de.htwg.upfaz.backgammon.controller;

import controllers.de.htwg.upfaz.backgammon.entities.Field;
import controllers.de.htwg.upfaz.backgammon.entities.IField;
import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;

public final class GameMap {
    public static final int TOTAL_FIELDS_NR = 28;
    public static final int FIELD_EATEN_WHITE = 25;
    public static final int FIELD_EATEN_BLACK = 24;
    public static final int FIELD_END_WHITE = 27;
    public static final int FIELD_END_BLACK = 26;

    private Field[] gameMap;
    private IPlayer players;

    public GameMap(final IGame game /*will be used later*/, final IPlayer players) {
        this.players = players;
        gameMap = new Field[TOTAL_FIELDS_NR];

        for (int i = 0; i < gameMap.length; i++) {
            gameMap[i] = new Field(i);
        }

        initStones();
    }

    public void moveStone(final int startNumber, final int targetNumber) {
        gameMap[targetNumber].setNumberStones(gameMap[targetNumber].getNumberStones() + 1);
        if (targetNumber != 26 || targetNumber != 27) {
        	gameMap[targetNumber].setStoneColor(players.getColor());
        }
        gameMap[startNumber].setNumberStones(gameMap[startNumber].getNumberStones() - 1);
        if (gameMap[startNumber].getNumberStones() == 0) {
            gameMap[startNumber].setStoneColor(-1);
        }
       
    }


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
    public IField getField(final int targetNumber) {
        return gameMap[targetNumber];
    }
    public IField[] getFields() {
        return gameMap;
    }
    public boolean isWhiteEaten() {
        return players.getColor() == Players.PLAYER_COLOR_WHITE && getField(FIELD_EATEN_WHITE).getNumberStones() > 0;
    }
    public boolean isBlackEaten() {
        return players.getColor() == Players.PLAYER_COLOR_BLACK && getField(FIELD_EATEN_BLACK).getNumberStones() > 0;
    }

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
}
