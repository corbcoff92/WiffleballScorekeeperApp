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

/**
 * This class provides much of the shared implementation required for both saving and loading a game.
 * It also acts as a shared entry point for the GameAndroid and Activity classes. It stores a reference
 * to the previously saved games.
 */
public class GameLoaderSaver {
    private final String SAVED_GAMES_KEY = "SAVED_GAMES";

    final private GameAndroid gameAndroid = GameAndroid.getInstance();
    final private Context context;
    final private boolean loader;

    private Game[] savedGames;
    private LinearLayout savedGamesLayout;
    private boolean hasSavedGames;
    private int selectedSlotNumber = -1;

    /**
     * Creates an instance of either a game loader or saver based on the provided arguments.
     * @param context           Reference to the app context, used for accessing {@link SharedPreferences}
     *                          and making toasts.
     * @param savedGamesLayout  Layout that will contain the views for the saved game slots.
     * @param loader            Boolean flag indicting that this instance will be used for loading games.
     */
    public GameLoaderSaver(Context context, LinearLayout savedGamesLayout, boolean loader) {
        this.context = context;
        this.savedGamesLayout = savedGamesLayout;
        this.loader = loader;
    }

    /**
     * Click handler that can be used when a saved game slot is clicked. This handler sets the selected
     * saved game slot based on the given slot number. It also changes the appearence of the given View
     * instance, providing an indication that it is currently selected.
     * @param view      Reference to the {@link View} that triggered the event, indicating that it
     *                  has been clicked on.
     * @param slotNum   Number indicating which slot the given {@link View} belonged to.
     */
    private void selectSlotNum(View view, int slotNum) {
        this.selectedSlotNumber = slotNum;

        // Draw box around selected slot, remove boxes around any other slot that might have been
        // previously selected.
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

    /**
     * Loads the game instance contained in the currently selected saved games slot. Returns a boolean
     * indicating whether or not a game was loaded. A toast is made if no slot is currently selected.
     * @return  {@code true} if a game was loaded, {@code false} otherwise.
     */
    public boolean loadGame() {
        boolean gameLoaded = false;
        // Check that there are any saved games
        if (hasSavedGames) {
            // Check that a saved game slot has been selected
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

    /**
     * Deletes the game contained in the currently selected saved game slot from memory. A toast is
     * made if a saved game slot is not currently selected.
     */
    public void deleteSavedGame() {
        // Check that there are any saved games
        if (hasSavedGames) {
            // Check that a saved games slot has been selected
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

    /**
     * Saves the current game to the currently selected saved game slot in memory. This allows the
     * game to persist even if the application is closed. Returns a boolean flag indicating whether
     * or not the game was saved to memory. A toast is made if a saved game slot is not currently selected.
     * @return {@code true} if the current game was saved to memory, {@code false} otherwise.
     */
    public boolean saveGame() {
        boolean gameSaved = false;
        // Check that a saved game slot is currently selected
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

    /**
     * Populates the layout used for displaying state of any previously saved games, or empty slots
     * if the saved game slot does not currently contain a previusly saved game. The layout is
     * populated with either a {@link GameView} or an {@link EmptySlot} depending on whether or not
     * a the slot currently contains a previously saved game. Click handlers are attached allowing
     * the saved game slots to be selected.
     */
    public void updateGameViews() {
        // Load saved games from memory
        loadSavedGames();

        // Clear layout and populate with the correct view
        // type based on the previously saved games.
        savedGamesLayout.removeAllViews();
        // Indicates whether or not there are any previously saved games.
        hasSavedGames = false;
        int i = 0;
        for (Game game : savedGames) {
            final int slotNumber = i;
            if (game != null) {
                // Create and display GameView for current previously saved game
                GameView gameView = new GameView(context, null);
                gameView.displayGame(game);
                // Attach click event handler and add to layout
                gameView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
                gameView.setHeadingText(context.getString(R.string.empty_slot_text, slotNumber+1));
                savedGamesLayout.addView(gameView);
                hasSavedGames = true;
            } else {
                // Create and update EmptySlot for current saved game slot that doesn't contain a
                // previously saved game
                EmptySlot emptyView = new EmptySlot(context);
                emptyView.setText(context.getString(R.string.empty_slot_text, slotNumber+1));
                // If this instance is not used for loading games, attach a click event handler to
                // allow empty slots to be selected so that the current game can be stored in them.
                if (!loader) {
                    emptyView.setClickable(true);
                    emptyView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
                }
                savedGamesLayout.addView(emptyView);
            }
            i++;
        }
    }

    /**
     * Loads any previously saved games from {@link SharedPreferences}. The previously saved games are
     * stored in an {@link java.util.ArrayList} which is serialized using the {@link Gson} module
     * provided by Google.
     */
    private void loadSavedGames() {
        // Access SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVED_GAMES_KEY, Context.MODE_PRIVATE);
        // Get serialized list of previously saved games from shared preferences
        String gamesJson = sharedPreferences.getString(SAVED_GAMES_KEY, null);
        // Deserialize list of previously saved games using Google's Gson module
        Gson gson = new Gson();
        Type typeGame = new TypeToken<Game[]>() {
        }.getType();
        savedGames = gson.fromJson(gamesJson, typeGame);
    }

    /**
     * Saves the currently saved games to {@link SharedPreferences}. The saved games are
     * stored in an {@link java.util.ArrayList} which and serialized using the {@link Gson} module
     * provided by Google.
     */
    void storeSavedGames() {
        try {
            // Serialize currently saved games list using Google's Gson module
            Gson gson = new Gson();
            String savedGamesJson = gson.toJson(savedGames);
            // Access shared preferences, and store the serialized list
            SharedPreferences sharedPreferences = context.getSharedPreferences(SAVED_GAMES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SAVED_GAMES_KEY, savedGamesJson);
            editor.apply();
        } catch (Exception e) {
            GameAndroid.makeToast(this.context, e.toString());
        }
    }
}