package com.example.wiffleballscorekeeperapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.wiffleballscorekeeperapp.GameAndroid;
import com.example.wiffleballscorekeeperapp.R;

/**
 * This activity is used for setting up a new game. It provides inputs for the user to select the
 * number of innings that the game should last, as well as the names for each team.
 */
public class SetupNewGameActivity extends AppCompatActivity {
    final private static int DEFAULT_NUMBER_OF_INNINGS = 3;
    final private static int MAX_NUMBER_OF_INNINGS = 9;
    final private GameAndroid gameAndroid = GameAndroid.getInstance();

    private NumberPicker numberOfInningsPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_new_game);

        // Set up number picker for the number of innings
        numberOfInningsPicker = findViewById(R.id.number_of_innings_picker);
        numberOfInningsPicker.setMinValue(1);
        numberOfInningsPicker.setMaxValue(MAX_NUMBER_OF_INNINGS);
        numberOfInningsPicker.setWrapSelectorWheel(false);
        numberOfInningsPicker.setValue(DEFAULT_NUMBER_OF_INNINGS);

        // Attach click event handlers
        findViewById(R.id.start_game_button).setOnClickListener((View view) -> launchNewGameActivity());
        findViewById(R.id.cancel_new_game_button).setOnClickListener((View view) -> finish());
    }

    /**
     * Click event handler for start game button. Initializes a new game based on the parameters
     * input by the user, and starts the game activity.
     */
    private void launchNewGameActivity()
    {
        // Get parameters input by user.
        int numberOfInnings = numberOfInningsPicker.getValue();
        EditText away_name_editText = findViewById(R.id.away_name_textEdit);
        EditText home_name_editText = findViewById(R.id.home_name_textEdit);
        String away_name = getTextWithHintAsDefault(away_name_editText);
        String home_name = getTextWithHintAsDefault(home_name_editText);

        // Initialize new game
        gameAndroid.newGame(numberOfInnings, away_name, home_name);

        // Start game activity
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Returns the text from the given EditText object, using the hint as a default value if the
     * field is blank.
     * @param   editText EditText instance to retrieve the text from.
     * @return  Text from the given {@code EditText} if it is not blank, the {@code EditText}'s hint
     *          otherwise.
     */
    private String getTextWithHintAsDefault(EditText editText)
    {
        String text = editText.getText().toString();
        // Check if text is blank
        if (TextUtils.isEmpty(text.trim()))
        {
            text = editText.getHint().toString();
        }
        return text;
    }
}