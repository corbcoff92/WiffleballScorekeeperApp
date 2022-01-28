package com.example.game;

public class GameAndroid extends Game {
    public enum PITCH_CALL_TYPES {BALL, STRIKE}

    public enum HIT_TYPES {SINGLE, DOUBLE, TRIPLE, HOMERUN}

    public enum OUT_TYPES {FLYOUT, GROUNDOUT}

    private GameStack gameStack;

    public GameAndroid(int numInnings, String away_name, String home_name) {
        super(numInnings, home_name, away_name);
        gameStack = new GameStack(this);
    }

    public void pitchCalled(PITCH_CALL_TYPES callType) {
        switch (callType) {
            case BALL:
                gameStack.stackGameState("Ball");
                callBall();
                break;
            case STRIKE:
                gameStack.stackGameState("Strike");
                callStrike();
                break;
        }
    }

    public void ballHit(HIT_TYPES hit_type) {
        switch (hit_type) {
            case SINGLE:
                gameStack.stackGameState("Single");
                advanceRunnersHit(1);
                break;
            case DOUBLE:
                gameStack.stackGameState("Double");
                advanceRunnersHit(2);
                break;
            case TRIPLE:
                gameStack.stackGameState("Triple");
                advanceRunnersHit(3);
                break;
            case HOMERUN:
                gameStack.stackGameState("Homerun");
                advanceRunnersHit(4);
                break;
        }
    }

    public void outMade(OUT_TYPES out_type) {
        switch (out_type) {
            case GROUNDOUT:
                gameStack.stackGameState("Groundout");
                groundOut();
                break;
            case FLYOUT:
                gameStack.stackGameState("Flyout");
                flyOut();
                break;
        }
    }

    public String undoGameAction() {
        return gameStack.undoLastAction();

    }

    public String getWaitingState() {
        String waitingState = "";
        if (isWaiting) {
            if (checkInningOver()) {
                waitingState = "Switch sides";
            } else {
                waitingState = "Next batter";
            }
        }
        return waitingState;
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
}
