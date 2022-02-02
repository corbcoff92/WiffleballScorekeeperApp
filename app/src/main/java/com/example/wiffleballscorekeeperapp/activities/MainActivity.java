package com.example.wiffleballscorekeeperapp.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wiffleballscorekeeperapp.GameAndroid;
import com.example.wiffleballscorekeeperapp.R;

public class MainActivity extends AppCompatActivity {
    private enum ACTIVITIES {NEW_GAME, LOAD_GAME, CONTINUE_GAME}

    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.new_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.NEW_GAME));
        findViewById(R.id.load_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.LOAD_GAME));
        findViewById(R.id.continue_game_button).setOnClickListener((View view) -> nextActivity(ACTIVITIES.CONTINUE_GAME));
        findViewById(R.id.exit_button).setOnClickListener((View view) -> finish());
    }

    @Override
    public void onResume() {
        super.onResume();
        Button continueGameButton = findViewById(R.id.continue_game_button);
        if (gameAndroid.getCurrentGame() != null) {
            continueGameButton.setVisibility(View.VISIBLE);
        } else {
            continueGameButton.setVisibility(View.GONE);
        }
    }

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