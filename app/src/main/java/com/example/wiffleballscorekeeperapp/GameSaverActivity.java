package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class GameSaverActivity extends AppCompatActivity {
    private GameLoaderSaver gameSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_saver);

        findViewById(R.id.button_save).setOnClickListener(view -> saveGame());
        findViewById(R.id.button_dont_save).setOnClickListener(view -> gameNotSaved());
        LinearLayout savedGamesLayout = findViewById(R.id.save_games_layout);
        gameSaver = new GameLoaderSaver(this, savedGamesLayout, false);

        gameSaver.updateGameViews();

    }

    private void saveGame() {
        if (gameSaver.saveGame()) {
            finish();
        }
    }

    private void gameNotSaved() {
        GameAndroid.makeToast(this, "Game not saved...");
        finish();
    }
}