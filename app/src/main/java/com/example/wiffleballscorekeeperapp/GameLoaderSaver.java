package com.example.wiffleballscorekeeperapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import com.example.customviews.EmptySlot;
import com.example.customviews.GameView;
import com.example.game.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

class GameLoaderSaver {
    private final String SAVED_GAMES_KEY = "SAVED_GAMES";
    final private GameAndroid gameAndroid = GameAndroid.getInstance();
    final private Context context;
    final private boolean loader;

    private Game[] savedGames;
    private LinearLayout savedGamesLayout;
    private boolean hasSavedGames;
    private int selectedSlotNumber = -1;

    GameLoaderSaver(Context context, LinearLayout savedGamesLayout, boolean loader) {
        this.context = context;
        this.savedGamesLayout = savedGamesLayout;
        this.loader = loader;
    }

    private void selectSlotNum(View view, int slotNum) {
        this.selectedSlotNumber = slotNum;
        savedGamesLayout = (LinearLayout) view.getParent();
        for (int i = 0; i < savedGamesLayout.getChildCount(); i++) {
            View gameView = savedGamesLayout.getChildAt(i);
            if (gameView == view) {
                gameView.setBackground(context.getResources().getDrawable(R.drawable.selected_border));
            } else {
                gameView.setBackground(null);
            }
        }
    }

    boolean loadGame() {
        boolean gameLoaded = false;
        if (hasSavedGames) {
            if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {
                Game game = savedGames[selectedSlotNumber];
                gameAndroid.loadGame(game);
                savedGames[selectedSlotNumber] = null;
                storeSavedGames();
                GameAndroid.makeToast(context, "Game loaded...");
                gameLoaded = true;
            } else {
                GameAndroid.makeToast(context, "Please first select a game slot...");
            }
        } else {
            GameAndroid.makeToast(context, "No games to load...");
        }
        return gameLoaded;
    }

    void deleteSavedGame() {
        if (hasSavedGames) {
            if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {
                savedGames[selectedSlotNumber] = null;
                storeSavedGames();
                selectedSlotNumber = -1;
                updateGameViews();
            } else {
                GameAndroid.makeToast(context, "Please first select a game slot...");
            }
        } else {
            GameAndroid.makeToast(context, "No saved games to delete...");
        }
    }

    boolean saveGame() {
        boolean gameSaved = false;
        if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {

            savedGames[selectedSlotNumber] = gameAndroid.getCurrentGame();
            storeSavedGames();
            GameAndroid.makeToast(context, "Game saved...");
            gameSaved = true;
        } else {
            GameAndroid.makeToast(context, "Please first select a game slot...");
        }
        return gameSaved;
    }

    public void updateGameViews() {
        loadSavedGames();
        savedGamesLayout.removeAllViews();
        hasSavedGames = false;
        int i = 0;
        for (Game game : savedGames) {
            final int slotNumber = i;
            if (game != null) {
                GameView gameView = new GameView(context, null);
                gameView.displayGame(game);
                gameView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
                savedGamesLayout.addView(gameView);
                hasSavedGames = true;
            } else {
                EmptySlot emptyView = new EmptySlot(context);
                if (!loader) {
                    emptyView.setClickable(true);
                    emptyView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
                }
                savedGamesLayout.addView(emptyView);
            }
            i++;
        }
    }

    private void loadSavedGames() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVED_GAMES_KEY, Context.MODE_PRIVATE);
        String gamesJson = sharedPreferences.getString(SAVED_GAMES_KEY, null);
        Gson gson = new Gson();
        Type typeGame = new TypeToken<Game[]>() {
        }.getType();
        savedGames = gson.fromJson(gamesJson, typeGame);
    }

    void storeSavedGames() {
        try {
            Gson gson = new Gson();
            String savedGamesJson = gson.toJson(savedGames);
            SharedPreferences sharedPreferences = context.getSharedPreferences(SAVED_GAMES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SAVED_GAMES_KEY, savedGamesJson);
            editor.apply();
        } catch (Exception e) {
            GameAndroid.makeToast(this.context, e.toString());
        }
    }
}
