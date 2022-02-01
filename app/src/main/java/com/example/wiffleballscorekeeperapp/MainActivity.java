package com.example.wiffleballscorekeeperapp;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.game.GameAndroid;

public class MainActivity extends AppCompatActivity {
    final private String LOG_TAG = this.getClass().getSimpleName();

    private enum ACTIVITIES {NEW_GAME, LOAD_GAME, CONTINUE_GAME}

    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.new_game_button).setOnClickListener((View view) -> nextActvity(ACTIVITIES.NEW_GAME));
        findViewById(R.id.load_game_button).setOnClickListener((View view) -> nextActvity(ACTIVITIES.LOAD_GAME));
        findViewById(R.id.continue_game_button).setOnClickListener((View view) -> nextActvity(ACTIVITIES.CONTINUE_GAME));
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

    private void nextActvity(ACTIVITIES activity) {
        Class activityClass = null;
        switch (activity) {
            case NEW_GAME:
                activityClass = SetupNewGame.class;
                break;
            case LOAD_GAME:
                activityClass = GameLoaderActivity.class;
                break;
            case CONTINUE_GAME:
                activityClass = GameActivity.class;
                break;
        }
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}