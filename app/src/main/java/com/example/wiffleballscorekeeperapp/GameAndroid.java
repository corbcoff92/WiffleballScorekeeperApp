package com.example.wiffleballscorekeeperapp;

import android.content.Context;
import android.widget.Toast;

import com.example.game.Game;
import com.example.game.GameStack;

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

    public static GameAndroid getInstance() {
        if (instance == null) {
            instance = new GameAndroid();
        }
        return instance;
    }

    public void newGame(int numInnings, String awayName, String homeName) {
        currentGame = new Game(numInnings, awayName, homeName);
        gameStack = new GameStack(currentGame);
    }

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

    public String undoGameAction() {
        return gameStack.undoLastAction();

    }

    public String getWaitingState() {
        String waitingState = "";
        if (currentGame.isWaiting) {
            if (currentGame.checkInningOver()) {
                waitingState = "Next inning";
            } else {
                waitingState = "Next batter";
            }
        }
        return waitingState;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public String redoGameAction() {
        return gameStack.redoLastAction();
    }

    public boolean undoAvailable() {
        return gameStack.undoAvailable();
    }

    public boolean redoAvailable() {
        return gameStack.redoAvailable();
    }

    public boolean isWaiting() {
        return currentGame.isWaiting;
    }

    public boolean isGameOver() {
        return currentGame.isGameOver;
    }

    public int getNumInnings() {
        return currentGame.getNumInnings();
    }

    public void continueGame() {
        currentGame.continueGame();
    }

    public void gameFinished() {
        this.currentGame = null;
    }

    void loadGame(Game game) {
        currentGame = new Game(game);
        gameStack = new GameStack(currentGame);
    }

    public static void makeToast(Context context, String text) {
        if (toaster == null) {
            toaster = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toaster.setText(text);
        toaster.show();
    }
}