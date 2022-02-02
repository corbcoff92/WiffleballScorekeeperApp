package com.example.wiffleballscorekeeperapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.example.wiffleballscorekeeperapp.customviews.EmptySlot;
import com.example.wiffleballscorekeeperapp.customviews.GameView;
import com.example.game.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

public class GameLoaderSaver {
    private final String SAVED_GAMES_KEY = "SAVED_GAMES";
    final private GameAndroid gameAndroid = GameAndroid.getInstance();
    final private Context context;
    final private boolean loader;

    private Game[] savedGames;
    private LinearLayout savedGamesLayout;
    private boolean hasSavedGames;
    private int selectedSlotNumber = -1;

    public GameLoaderSaver(Context context, LinearLayout savedGamesLayout, boolean loader) {
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
                gameView.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.selected_border, null));
            } else {
                gameView.setBackground(null);
            }
        }
    }

    public boolean loadGame() {
        boolean gameLoaded = false;
        if (hasSavedGames) {
            if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {
                Game game = savedGames[selectedSlotNumber];
                gameAndroid.loadGame(game);
                savedGames[selectedSlotNumber] = null;
                storeSavedGames();
                GameAndroid.makeToast(context, context.getString(R.string.game_loaded_text));
                gameLoaded = true;
            } else {
                GameAndroid.makeToast(context, context.getString(R.string.no_slot_selected));
            }
        } else {
            GameAndroid.makeToast(context, context.getString(R.string.no_saved_games, context.getString(R.string.load_text)));
        }
        return gameLoaded;
    }

    public void deleteSavedGame() {
        if (hasSavedGames) {
            if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {
                savedGames[selectedSlotNumber] = null;
                storeSavedGames();
                selectedSlotNumber = -1;
                updateGameViews();
            } else {
                GameAndroid.makeToast(context, context.getString(R.string.no_slot_selected));
            }
        } else {
            GameAndroid.makeToast(context, context.getString(R.string.no_saved_games,context.getString(R.string.delete_text)));
        }
    }

    public boolean saveGame() {
        boolean gameSaved = false;
        if (selectedSlotNumber >= 0 && selectedSlotNumber < 3) {

            savedGames[selectedSlotNumber] = gameAndroid.getCurrentGame();
            storeSavedGames();
            GameAndroid.makeToast(context, context.getString(R.string.game_saved_text));
            gameSaved = true;
        } else {
            GameAndroid.makeToast(context, context.getString(R.string.no_slot_selected));
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
                gameView.setHeadingText(context.getString(R.string.empty_slot_text, slotNumber+1));
                savedGamesLayout.addView(gameView);
                hasSavedGames = true;
            } else {
                EmptySlot emptyView = new EmptySlot(context);
                emptyView.setText(context.getString(R.string.empty_slot_text, slotNumber+1));
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
