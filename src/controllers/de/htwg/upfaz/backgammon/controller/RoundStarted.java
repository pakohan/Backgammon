package controllers.de.htwg.upfaz.backgammon.controller;

public final class RoundStarted
        extends State {

    private int clickBefore;
    private IGame game;

    public RoundStarted(final int lastClick, final IGame game) {
        super(STATE.STATE_ROUND_STARTED);
        this.clickBefore = lastClick;

        game.setDice(new Dice());
    }

    public final State click(final int fieldClicked) {

        // three input parameters: current player; gamemap; click;
        // evaluation if click is valid
        return null;
    }
}
