package com.example.wiffleballscorekeeperapp;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.game.GameAndroid;

public class MainActivity extends AppCompatActivity {
    final private String LOG_TAG = this.getClass().getSimpleName();
    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.new_game_button).setOnClickListener((View view) -> newGame());

        findViewById(R.id.exit_button).setOnClickListener((View view) -> finish());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Button continueGameButton = findViewById(R.id.continue_game_button);
        if (gameAndroid.getCurrentGame() != null){
            continueGameButton.setVisibility(View.VISIBLE);
            continueGameButton.setOnClickListener((View view) -> continueGame());
        }
        else
        {
            continueGameButton.setVisibility(View.GONE);
        }
    }

    private void newGame()
    {
        Intent intent = new Intent(this, SetupNewGame.class);
        startActivity(intent);
    }

    private void continueGame()
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}