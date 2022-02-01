package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class SetupNewGame extends AppCompatActivity {
    final private static int DEFAULT_NUMBER_OF_INNINGS = 3;
    final private static int MAX_NUMBER_OF_INNINGS = 9;

    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    private NumberPicker numberOfInningsPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_new_game);

        numberOfInningsPicker = findViewById(R.id.number_of_innings_picker);

        numberOfInningsPicker.setMinValue(1);
        numberOfInningsPicker.setMaxValue(MAX_NUMBER_OF_INNINGS);
        numberOfInningsPicker.setWrapSelectorWheel(false);
        numberOfInningsPicker.setValue(DEFAULT_NUMBER_OF_INNINGS);

        findViewById(R.id.start_game_button).setOnClickListener((View view) -> launchNewGameActivity());
        findViewById(R.id.cancel_new_game_button).setOnClickListener((View view) -> finish());

    }

    private void launchNewGameActivity()
    {
        int numberOfInnings = numberOfInningsPicker.getValue();
        EditText away_name_editText = findViewById(R.id.away_name_textEdit);
        EditText home_name_editText = findViewById(R.id.home_name_textEdit);

        String away_name = getTextWithHintAsDefault(away_name_editText);
        String home_name = getTextWithHintAsDefault(home_name_editText);

        gameAndroid.newGame(numberOfInnings, away_name, home_name);

        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);

        finish();
    }

    private String getTextWithHintAsDefault(EditText editText)
    {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text.trim()))
        {
            text = editText.getHint().toString();
        }
        return text;
    }
}