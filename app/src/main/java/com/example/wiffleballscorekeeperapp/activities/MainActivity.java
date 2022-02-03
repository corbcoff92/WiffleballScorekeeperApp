package com.example.wiffleballscorekeeperapp.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wiffleballscorekeeperapp.GameAndroid;
import com.example.wiffleballscorekeeperapp.R;

/**
 * This activity acts as a main menu for the user. It allows the user to start a new game, continue
 * a current game already in progress, load a game, or exit the app.
 */
public class MainActivity extends AppCompatActivity {
    private enum ACTIVITIES {NEW_GAME, LOAD_GAME, CONTINUE_GAME}

    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach click event handlers
        findViewById(R.id.new_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.NEW_GAME));
        findViewById(R.id.load_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.LOAD_GAME));
        findViewById(R.id.continue_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.CONTINUE_GAME));
        findViewById(R.id.exit_button).setOnClickListener((View view) -> finish());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Show continue game button if there is a current game in progress
        Button continueGameButton = findViewById(R.id.continue_game_button);
        if (gameAndroid.getCurrentGame() != null) {
            continueGameButton.setVisibility(View.VISIBLE);
        } else {
            continueGameButton.setVisibility(View.GONE);
        }
    }

    /**
     * Click handler for every button of this main activity. Each button is connected with a
     * specific activity found in {@link ACTIVITIES}. The desired activity is started.
     * @param activity Indication of the activity to be started. Must be from {@link ACTIVITIES}
     */
    private void nextActivity(ACTIVITIES activity) {
        Intent intent = null;
        switch (activity) {
            case NEW_GAME:
                intent = new Intent(this, SetupNewGameActivity.class);
                break;
            case LOAD_GAME:
                intent = new Intent(this, GameLoaderActivity.class);
                break;
            case CONTINUE_GAME:
                intent = new Intent(this, GameActivity.class);
                break;
        }
        startActivity(intent);
    }
}