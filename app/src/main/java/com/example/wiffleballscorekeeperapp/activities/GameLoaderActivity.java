package com.example.wiffleballscorekeeperapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiffleballscorekeeperapp.GameLoaderSaver;
import com.example.wiffleballscorekeeperapp.R;

public class GameLoaderActivity extends AppCompatActivity {
    private GameLoaderSaver gameLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loader);

        findViewById(R.id.button_load).setOnClickListener(view -> loadGame());
        findViewById(R.id.button_cancel_load).setOnClickListener(view -> finish());
        findViewById(R.id.button_delete).setOnClickListener(view -> deleteSavedGame());

        LinearLayout savedGamesLayout = findViewById(R.id.saved_games_layout);
        gameLoader = new GameLoaderSaver(this, savedGamesLayout, true);
        gameLoader.updateGameViews();
    }

    private void loadGame() {
        if (gameLoader.loadGame()) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void deleteSavedGame() {
        gameLoader.deleteSavedGame();
    }
}