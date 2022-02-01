package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customviews.EmptySlot;
import com.example.customviews.GameView;
import com.example.game.Game;
import com.example.game.GameAndroid;

import org.xml.sax.AttributeList;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLoaderActivity extends AppCompatActivity {
    private final GameAndroid gameAndroid = GameAndroid.getInstance();

    private LinearLayout savedGamesLayout;
    private Game[] savedGames;
    private int slotNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loader);

        findViewById(R.id.button_load).setOnClickListener(view -> loadGame());
        findViewById(R.id.button_cancel_load).setOnClickListener(view -> finish());
        findViewById(R.id.button_delete).setOnClickListener(view -> deleteSavedGame());

        savedGamesLayout = findViewById(R.id.saved_games_layout);

        loadGames();
        addGameViews();
    }

    private void selectSlotNum(View view, int slotNum){
        this.slotNum = slotNum;
        for (int i = 0; i < savedGamesLayout.getChildCount(); i++) {
            View gameView = savedGamesLayout.getChildAt(i);
            if (gameView == view) {
                gameView.setBackground(getResources().getDrawable(R.drawable.selected_border));
            } else {
                gameView.setBackground(null);
            }
        }
    }

    private void deleteSavedGame() {
        if (slotNum >= 0 && slotNum < 3) {
            savedGames[slotNum] = null;
            String savedGamesJson = GameSaver.toJson(savedGames);
            SharedPreferences sharedPreferences = getSharedPreferences(GameSaverActivity.SAVED_GAMES_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GameSaverActivity.SAVED_GAMES_KEY,savedGamesJson);
            editor.apply();
            slotNum = -1;
            loadGames();
            addGameViews();
        } else {
            makeToast("Please first select a game slot...");
        }
    }

    private void loadGame() {
        if (slotNum >= 0 && slotNum < 3) {
            Game game = savedGames[slotNum];
            gameAndroid.loadGame(game);
            savedGames[slotNum] = null;
            SharedPreferences sharedPreferences = getSharedPreferences(GameSaverActivity.SAVED_GAMES_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String savedGameJson = GameSaver.toJson(savedGames);
            editor.putString(GameSaverActivity.SAVED_GAMES_KEY, savedGameJson);
            editor.apply();
            makeToast("Game loaded...");
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
        } else {
            makeToast("Please first select a game slot...");
        }
    }

    private void loadGames() {
        SharedPreferences sharedPreferences = getSharedPreferences(GameSaverActivity.SAVED_GAMES_KEY, MODE_PRIVATE);
        String savedGamesJson = sharedPreferences.getString(GameSaverActivity.SAVED_GAMES_KEY, null);
        if (savedGamesJson != null)
        {
            savedGames = GameSaver.loadSavedGames(savedGamesJson);
        }
        else
        {
            savedGames = new Game[3];
        }
    }

    private void addGameViews() {
        savedGamesLayout.removeAllViews();
        for (int i = 0; i < 3; i++)
        {
            Game game = savedGames[i];
            if (game != null) {
                GameView gameView = new GameView(this, null);
                gameView.displayGame(game);

                final int slotNumber = i;
                gameView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
                savedGamesLayout.addView(gameView);
            } else {
                EmptySlot empty = new EmptySlot(this);
                savedGamesLayout.addView(empty);
            }
        }
    }

    private void makeToast(String toastText) {
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}