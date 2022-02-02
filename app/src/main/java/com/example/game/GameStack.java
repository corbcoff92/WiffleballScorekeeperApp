package com.example.game;

import java.util.LinkedList;
import java.util.HashMap;


public class GameStack {
    final private LinkedList<HashMap<String, Game>> undoStack = new LinkedList<>();
    final private LinkedList<HashMap<String, Game>> redoStack = new LinkedList<>();

    final private Game game;

    public GameStack(Game game) {
        this.game = game;
    }

    public void stackGameState(String action) {
        if (redoAvailable()) {
            redoStack.clear();
        }
        undoStack.push(new HashMap<String, Game>() {{
            put(action, new Game(game));
        }});
    }

    public boolean redoAvailable() {
        return !redoStack.isEmpty();
    }

    public boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    public String redoLastAction() {
        if (redoAvailable()) {
            HashMap<String, Game> lastGameState = redoStack.pop();
            String action = lastGameState.keySet().iterator().next();
            undoStack.push(new HashMap<String, Game>() {{
                put(action, new Game(game));
            }});
            game.redo(lastGameState);
            return action;
        } else {
            return null;
        }
    }

    public String undoLastAction() {
        if (undoAvailable()) {
            HashMap<String, Game> lastGameState = undoStack.pop();
            String action = lastGameState.keySet().iterator().next();
            redoStack.push(new HashMap <String, Game>() {{
                put(action, new Game(game));
            }});
            game.undo(lastGameState);
            return action;
        } else {
            return null;
        }
    }
}