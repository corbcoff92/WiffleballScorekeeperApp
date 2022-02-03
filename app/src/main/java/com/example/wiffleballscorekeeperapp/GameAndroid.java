package com.example.wiffleballscorekeeperapp;

import android.content.Context;
import android.widget.Toast;

import com.example.game.Game;
import com.example.game.GameStack;

/**
 * This wrapper class acts as a shared entry point for the Activity, Game, GameStack, and
 * GameLoaderSaver classes. This is a singleton class, and holds a reference to the current game and
 * its actions stack. These references are used by all the app's Activities. It also contains a useful
 * helper method {@see makeToast} for displaying a {@link Toast} to display a short message to the user.
 */
public class GameAndroid {
    public enum PITCH_CALL_TYPES {BALL, STRIKE}
    public enum HIT_TYPES {SINGLE, DOUBLE, TRIPLE, HOMERUN}
    public enum OUT_TYPES {FLYOUT, GROUNDOUT}

    private static GameAndroid instance = null;
    private Game currentGame = null;
    private GameStack gameStack = null;
    private static Toast toaster;

    private GameAndroid() {
    }

    /**
     * Used for obtaining the singleton instance of {@code GameAndroid}
     * @return Singleton instance of this {@code GameAndroid} class.
     */
    public static GameAndroid getInstance() {
        if (instance == null) {
            instance = new GameAndroid();
        }
        return instance;
    }

    /**
     * Sets the current game reference to a newly initialized game based on the given parameters.
     * A new game state stack is also created and attached.
     * @param numInnings    Number of innings that the game should last.
     * @param awayName      Name for the away team.
     * @param homeName      Name for the home team.
     */
    public void newGame(int numInnings, String awayName, String homeName) {
        currentGame = new Game(numInnings, awayName, homeName);
        gameStack = new GameStack(currentGame);
    }

    /**
     * Implementation of either a ball or strike being called, based on the provided pitch call type.
     * @param callType Indication of the pitch call type made. Must be an element from {@link PITCH_CALL_TYPES}
     */
    public void pitchCalled(PITCH_CALL_TYPES callType) {
        switch (callType) {
            case BALL:
                gameStack.stackGameState("Ball");
                currentGame.callBall();
                break;
            case STRIKE:
                gameStack.stackGameState("Strike");
                currentGame.callStrike();
                break;
        }
    }

    /**
     * Implementation of the play being hit, with hit type being based on the provided {@link HIT_TYPES} element.
     * @param hit_type Indication of the resulting hit type. Must be an element from {@link HIT_TYPES}
     */
    public void ballHit(HIT_TYPES hit_type) {
        switch (hit_type) {
            case SINGLE:
                gameStack.stackGameState("Single");
                currentGame.advanceRunnersHit(1);
                break;
            case DOUBLE:
                gameStack.stackGameState("Double");
                currentGame.advanceRunnersHit(2);
                break;
            case TRIPLE:
                gameStack.stackGameState("Triple");
                currentGame.advanceRunnersHit(3);
                break;
            case HOMERUN:
                gameStack.stackGameState("Homerun");
                currentGame.advanceRunnersHit(4);
                break;
        }
    }

    /**
     * Implementation of an out being made based on the provided {@link OUT_TYPES} element.
     * @param out_type Indication of the type of out made. Must be an element from {@link OUT_TYPES}.
     */
    public void outMade(OUT_TYPES out_type) {
        switch (out_type) {
            case GROUNDOUT:
                gameStack.stackGameState("Groundout");
                currentGame.groundOut();
                break;
            case FLYOUT:
                gameStack.stackGameState("Flyout");
                currentGame.flyOut();
                break;
        }
    }

    /**
     * Implementation of the previous game action being undone.
     * Returns a string indicating the action that was undone.
     * @return String indicating the action that was undone.
     */
    public String undoGameAction() {

        return gameStack.undoLastAction();
    }

    /**
     * Used to determine if there is an action in the game stack that can be undone.
     * @return {@code true} if there is an action available, {@code false} otherwise.
     */
    public boolean undoAvailable() {
        return gameStack.undoAvailable();
    }

    /**
     * Implementation of the previously undone game action being redone.
     * Returns a string indicating the action that was redone.
     * @return String indicating the action that was redone.
     */
    public String redoGameAction() {
        return gameStack.redoLastAction();
    }

    /**
     * Used to determine if there is an action in the game stack that can be redone.
     * @return {@code true} if there is an action available, {@code false} otherwise.
     */
    public boolean redoAvailable() {
        return gameStack.redoAvailable();
    }

    /**
     * Used to determine the current games waiting state. Returns a String indicating the reason for
     * the game being paused.
     * @return String indicating the games waiting state (the reason that the game was paused).
     */
    public String getWaitingState() {
        String waitingState = "";
        if (currentGame.isWaiting) {
            // Set indication of the waiting state
            if (currentGame.checkInningOver()) {
                waitingState = "Next inning";
            } else {
                waitingState = "Next batter";
            }
        }
        return waitingState;
    }

    /**
     * Used for obtaining the current game instance.
     * @return Current {@link Game} instance.
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Used to determine if the current game is in a waiting state. The game pauses at significant
     * times to allow the user to follow along easier.
     * @return {@code true} if the game is in a waiting state, {@code false} otherwise.
     */
    public boolean isWaiting() {
        return currentGame.isWaiting;
    }

    /**
     * Used to determine if the current game has ended.
     * @return {@code true} if the current game has ended, {@code false} otherwise.
     */
    public boolean isGameOver() {
        return currentGame.isGameOver;
    }

    /**
     * Used to determine the number of innings that the current game is supposed to last.
     * @return Number of innings that the current game is supposed to last.
     */
    public int getNumInnings() {
        return currentGame.getNumInnings();
    }

    /**
     * Continues the current game following a waiting state. This method tells the current game to
     * continue after it has reached a paused state.
     */
    public void continueGame() {
        currentGame.continueGame();
    }

    /**
     * Moves the current game to a completed state. This method removes the current game reference,
     * indicating that it does not need to be continued.
     */
    public void gameFinished() {
        this.currentGame = null;
    }

    /**
     * Implementation of a game being loaded from the given previously saved state. This method sets
     * the current game reference based on the provided {@link Game} instance.
     * @param game Game that should be loaded and made the current game.
     */
    void loadGame(Game game) {
        currentGame = new Game(game);
        gameStack = new GameStack(currentGame);
    }

    /**
     * Makes a {@link Toast} to the screen displaying a short message to the user.
     * @param context   Context for which the {@link Toast} should be made.
     * @param text      String containing the short message that should be displayed.
     */
    public static void makeToast(Context context, String text) {
        // If toast is already being displayed, replace the text rather than creating a new toast.
        // This prevents the toasts from building up and creating a long queue if buttons are rapidly
        // pressed.
        if (toaster == null) {
            toaster = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toaster.setText(text);
        toaster.show();
    }
}