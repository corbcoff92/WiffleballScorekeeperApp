package com.example.wiffleballscorekeeperapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiffleballscorekeeperapp.GameLoaderSaver;
import com.example.wiffleballscorekeeperapp.R;

/**
 * This activity is used for displaying the saved game slots to the user for loading any games that
 * have previously been saved. It displays the three saved game slots for the user to choose from,
 * and provides the ability to load saved games from any non-empty slot.
 */
public class GameLoaderActivity extends AppCompatActivity {
    private GameLoaderSaver gameLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loader);

        // Attach click event handlers
        findViewById(R.id.button_load).setOnClickListener(view -> loadGame());
        findViewById(R.id.button_cancel_load).setOnClickListener(view -> finish());
        findViewById(R.id.button_delete).setOnClickListener(view -> deleteSavedGame());

        // Find & cache View reference for saved game layout
        LinearLayout savedGamesLayout = findViewById(R.id.saved_games_layout);

        // Create game loader instance & fill saved game slot views
        gameLoader = new GameLoaderSaver(this, savedGamesLayout, true);
        gameLoader.updateGameViews();
    }

    /**
     * Click event handler for load button. Loads the game from the selected game slot and starts
     * the game activity.
     */
    private void loadGame() {
        if (gameLoader.loadGame()) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Click event handler for delete button. Deletes the saved game from the selected saved game slot.
     */
    private void deleteSavedGame() {
        gameLoader.deleteSavedGame();
    }
}