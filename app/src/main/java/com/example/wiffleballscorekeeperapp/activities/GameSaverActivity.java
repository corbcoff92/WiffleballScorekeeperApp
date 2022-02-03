package com.example.wiffleballscorekeeperapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.wiffleballscorekeeperapp.GameAndroid;
import com.example.wiffleballscorekeeperapp.GameLoaderSaver;
import com.example.wiffleballscorekeeperapp.R;

/**
 * This activity is used for displaying the saved game slots to the user for saving the current game.
 * It displays the three saved game slots for the user to choose from,
 * and provides the ability to save the current game in any slot.
 */
public class GameSaverActivity extends AppCompatActivity {
    private GameLoaderSaver gameSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_saver);

        // Attach click event handlers
        findViewById(R.id.button_save).setOnClickListener(view -> saveGame());
        findViewById(R.id.button_dont_save).setOnClickListener(view -> gameNotSaved());

        // Find & cache View reference for saved game layout
        LinearLayout savedGamesLayout = findViewById(R.id.save_games_layout);

        // Create game saver instance & fill saved game slot views
        gameSaver = new GameLoaderSaver(this, savedGamesLayout, false);
        gameSaver.updateGameViews();

    }

    /**
     * Click event handler for save button. Saves the current game to the selected saved game slot.
     * If the current game is saved, this activity is finished.
     */
    private void saveGame() {
        if (gameSaver.saveGame()) {
            finish();
        }
    }

    /**
     * Click event handler for the don't save button.
     * Finishes this activity.
     */
    private void gameNotSaved() {
        GameAndroid.makeToast(this, getString(R.string.game_not_saved_text));
        finish();
    }
}