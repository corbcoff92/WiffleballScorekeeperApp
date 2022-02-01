package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.customviews.EmptySlot;
import com.example.customviews.GameView;
import com.example.game.Game;
import com.example.game.GameAndroid;

public class GameSaverActivity extends AppCompatActivity {
    public final static String SAVED_GAMES_KEY = "SAVED_GAMES";
    private final GameAndroid gameAndroid = GameAndroid.getInstance();
    private Game[] savedGames;
    private LinearLayout savedGamesLayout;
    private int slotNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_saver);

        findViewById(R.id.button_save).setOnClickListener(view -> saveGame());
        findViewById(R.id.button_dont_save).setOnClickListener(view -> gameNotSaved());
        savedGamesLayout = findViewById(R.id.save_games_layout);

        loadSavedGames();
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

    private void loadSavedGames()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVED_GAMES_KEY, MODE_PRIVATE);
        String savedGamesJson = sharedPreferences.getString(SAVED_GAMES_KEY, null);
        if (savedGamesJson != null)
        {
            savedGames = GameSaver.loadSavedGames(savedGamesJson);
        }
        else
        {
            savedGames = new Game[3];
        }
    }

    private void saveGame() {
        if (slotNum >= 0 && slotNum < 3) {
            SharedPreferences sharedPreferences = getSharedPreferences(SAVED_GAMES_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Game game = gameAndroid.getCurrentGame();
            savedGames[slotNum] = game;
            String savedGamesJson = GameSaver.toJson(savedGames);
            editor.putString(SAVED_GAMES_KEY, savedGamesJson);
            editor.apply();
            makeToast("Game saved...");
            finish();
        } else {
            makeToast("Please first select a game slot...");
        }
    }

    private void addGameViews() {
        savedGamesLayout.removeAllViews();
        View newView;
        for (int i = 0; i < 3; i++)
        {
            Game game = savedGames[i];
            if (game != null) {
                GameView gameView = new GameView(this, null);
                gameView.displayGame(game);
                newView = gameView;

            } else {
                EmptySlot empty = new EmptySlot(this);
                newView = empty;
                newView.setClickable(true);
            }
            final int slotNumber = i;
            newView.setOnClickListener(view -> selectSlotNum(view, slotNumber));
            savedGamesLayout.addView(newView);
        }
    }

    private void gameNotSaved()
    {
        makeToast("Game not saved...");
        finish();
    }

    private void makeToast(String toastText)
    {
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}