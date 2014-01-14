package controllers.de.htwg.upfaz.backgammon.controller;

import com.google.inject.Inject;
import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.gui.Constances;

import java.util.Observable;
import java.util.UUID;

public class Core
        extends Observable
        implements IGame {

    private IPlayer players;
    private GameMap gameMap;
	private Dice dice;

    @Inject
    private Persister database;

    public UUID createGame() {
        players = new Players();
        dice = new Dice();
        gameMap = new GameMap(players, dice);

        players.changeCurrentPlayer();

        final UUID uuid = database.createGame(gameMap);
        setChanged();
        notifyObservers();
        return uuid;
    }

    public void loadGame(final UUID uuid) {
        this.gameMap = database.loadGame(uuid, -1);
        this.players = gameMap.getPlayers();
        this.dice = gameMap.getDice();
        setChanged();
        notifyObservers();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public IPlayer getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    public int getDiceToDraw(final int i) {
        return dice.getDiceToDraw(i);
    }

    public boolean click(final int field) {
        boolean returnVal = true;

        if (gameMap.getFirstClick() == -1) {
            Log.verbose("Clicked first field: " + field);
            gameMap.setFirstClick(field);
        } else {
            gameMap.setSecondClick(field);
            // try move also performs the move
            Log.verbose("Making move from " + gameMap.getFirstClick() + " to " + gameMap.getSecondClick());
            returnVal = tryMove();
            gameMap.setFirstClick(-1);
            gameMap.setSecondClick(-1);
        }

        // check for winner
        if (gameMap.checkForWinner()) {
            setWinner(players.getColor() + 1);
            return true;
        }

        database.saveGame(gameMap);

        // better call this too often than not enough times
        setChanged();
        notifyObservers();
        return returnVal;
    }

    private boolean tryMove() {
        // ask all instances like the map or the players class, if the move is
        // possible
        final boolean returnVal = checkIfmovePossible();

        // if so, perform the move
        if (returnVal) {
            makeMove();
        }

        return returnVal;
    }

    private boolean checkIfmovePossible() {
        final boolean toReturn = true;
        // do checks if this move is possible
        if (gameMap.getField(gameMap.getFirstClick()).getStoneColor() != players.getColor()) {
            Log.verbose("There are no your pieces");
            return false;
        }

        // check direction
        if (gameMap.getSecondClick() < Dice.TWENTY_FOUR && (players.getColor() == Players.PLAYER_COLOR_WHITE && gameMap.getSecondClick() <= gameMap.getFirstClick() || players.getColor() == Players.PLAYER_COLOR_BLACK && gameMap.getSecondClick() >= gameMap.getFirstClick())) {
            Log.verbose("You're going in the wrong direction!");
            return false;
        }

        if (!dice.checkDistance(Math.abs(gameMap.getSecondClick() - gameMap.getFirstClick()))) {
            Log.verbose("Can you count?");
            return false;
        }

        if (gameMap.getField(gameMap.getSecondClick()).isNotJumpable(players.getColor())) {
            Log.verbose("There are some stones from another player");
            return false;
        }

        return toReturn;
    }

    private void renewJumps(final int start, final int target) {

        if (target >= GameMap.FIELD_END_BLACK) {
            dice.renewJumpsEndPhase(start);
        } else {
            dice.move(Math.abs(start - target));
        }
    }

    private void makeMove() {
        checkEndPhase();
        // perform the move

        if (gameMap.getField(gameMap.getSecondClick()).getNumberStones() == 1 && gameMap.getField(gameMap.getSecondClick()).getStoneColor() != players.getColor()) {
            gameMap.eatStone(gameMap.getFirstClick(), gameMap.getSecondClick());
        } else {
            gameMap.moveStone(gameMap.getFirstClick(), gameMap.getSecondClick());
        }

        renewJumps(gameMap.getFirstClick(), gameMap.getSecondClick());
        gameMap.setFirstClick(-1);

        if (!dice.hasTurnsLeft()) {
            players.changeCurrentPlayer();
            dice.rollTheDice();
        }
    }

    private void checkEndPhase() {
        int stonesInEndPhase = 0;
        if (players.getColor() == Players.PLAYER_COLOR_WHITE) {
            // Fields 18-23 for white player
            for (int i = GameMap.FIELD_18; i <= GameMap.FIELD_23; i++) {
                // are endphase fields
                if (gameMap.getField(i).getStoneColor() == players.getColor()) {
                    stonesInEndPhase += gameMap.getField(i).getNumberStones();
                }
            }
            stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_WHITE).getNumberStones();
        } else {
            // Fields 0-5 for black player are
            for (int i = 5; i >= 0; i--) {
                // endphase fields
                if (gameMap.getField(i).getStoneColor() == players.getColor()) {
                    stonesInEndPhase += gameMap.getField(i).getNumberStones();
                }
            }
            stonesInEndPhase += gameMap.getField(GameMap.FIELD_END_BLACK).getNumberStones();
        }

        gameMap.setEndPhase(stonesInEndPhase == Constances.STONES_TO_WIN);
    }

    public int getWinner() {
        return gameMap.getWinner();
    }

    public void setWinner(final int winner) {
        gameMap.setWinner(winner);
    }
}
