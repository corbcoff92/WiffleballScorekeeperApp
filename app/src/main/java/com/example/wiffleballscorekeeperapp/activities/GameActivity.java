package com.example.wiffleballscorekeeperapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiffleballscorekeeperapp.GameAndroid;
import com.example.wiffleballscorekeeperapp.R;
import com.example.wiffleballscorekeeperapp.customviews.GameView;

import java.util.ArrayList;

/**
 * This activity is used for displaying the current game for user interaction.
 * It contains separate button menus depending on the game situation (pitches, hits, outs, etc)
 */
public class GameActivity extends AppCompatActivity {
    final private GameAndroid androidGame = GameAndroid.getInstance();

    private GameView gameDisplay;
    private View pitch_menu;
    private View hit_menu;
    private View out_menu;
    private View continue_menu;
    private View gameover_menu;
    private Button undo_button;
    private Button redo_button;
    private Button continue_button;
    final private ArrayList<View> menus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Load saved state
        if (savedInstanceState != null) {
            int shownMenuID = savedInstanceState.getInt("shown_menu_id");
            View menuToShow = findViewById(shownMenuID);
            showMenu(menuToShow);
            if (menuToShow == pitch_menu || androidGame.isWaiting()) {
                updateMenu();
            }
        }

        // Find & cache needed View references
        gameDisplay = findViewById(R.id.game_display);
        pitch_menu = findViewById(R.id.pitch_buttons);
        hit_menu = findViewById(R.id.hit_buttons);
        out_menu = findViewById(R.id.out_buttons);
        continue_menu = findViewById(R.id.continue_buttons);
        gameover_menu = findViewById(R.id.gameover_buttons);
        undo_button = findViewById(R.id.undo_button);
        redo_button = findViewById(R.id.redo_button);
        continue_button = findViewById(R.id.continue_button);

        // Fill menus array
        menus.add(pitch_menu);
        menus.add(hit_menu);
        menus.add(out_menu);
        menus.add(continue_menu);
        menus.add(gameover_menu);

        //*************************************  Attach click handlers  *************************************//
        // Continue menu
        findViewById(R.id.continue_button).setOnClickListener((View view) -> continueGame());
        // Pitch menu
        findViewById(R.id.ball_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.BALL));
        findViewById(R.id.strike_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.STRIKE));
        findViewById(R.id.hit_button).setOnClickListener((View view) -> showMenu(hit_menu));
        findViewById(R.id.out_button).setOnClickListener((View view) -> showMenu(out_menu));
        findViewById(R.id.undo_button).setOnClickListener((View view) -> undoGameAction());
        findViewById(R.id.redo_button).setOnClickListener((View view) -> redoGameAction());
        findViewById(R.id.quit_button).setOnClickListener((View view) -> quitGame());
        // Hit menu
        findViewById(R.id.single_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.SINGLE));
        findViewById(R.id.double_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.DOUBLE));
        findViewById(R.id.triple_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.TRIPLE));
        findViewById(R.id.homerun_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.HOMERUN));
        findViewById(R.id.cancel_hit_button).setOnClickListener((View view) -> cancelClicked());
        // Out menu
        findViewById(R.id.flyout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.FLYOUT));
        findViewById(R.id.groundout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.GROUNDOUT));
        findViewById(R.id.cancel_out_button).setOnClickListener((View view) -> cancelClicked());
        // Game over menu
        findViewById(R.id.done_button).setOnClickListener((View view) -> done());
        findViewById(R.id.gameover_undo_button).setOnClickListener((View view) -> undoLastGameAction());

        // Display game
        gameDisplay.setHeadingText(getString(R.string.game_heading_text, androidGame.getNumInnings()));
        gameDisplay.displayGame(androidGame.getCurrentGame());
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Store current menu state so it can be reinstated
        // when during configuration changes
        for (View menu : menus) {
            if (menu.getVisibility() == View.VISIBLE) {
                outState.putInt("shown_menu_id", menu.getId());
            }
        }
    }

    /**
     * Advances the game when it is in a paused state. This method automatically updates and displays
     * the buttons of the pitch menu and the game display.
     */
    private void continueGame() {
        androidGame.continueGame();
        showMenu(pitch_menu);
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
    }

    /**
     * Click handler for the pitch call type buttons (ball & strike). Each button is connected with
     * a specific pitch call type found in {@link GameAndroid.PITCH_CALL_TYPES}.
     *
     * @param callType Indication of the pitch call type. Must be from {@link GameAndroid.PITCH_CALL_TYPES}
     */
    private void pitchCalled(GameAndroid.PITCH_CALL_TYPES callType) {
        androidGame.pitchCalled(callType);
        String toastText = "";
        switch (callType) {
            case BALL:
                toastText = getString(R.string.ball_text);
                break;
            case STRIKE:
                toastText = getString(R.string.strike_text);
                break;
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, toastText);
    }

    /**
     * Click handler for the hit type buttons (single, double, triple, homerun). Each button is connected with
     * a specific hit type found in {@link GameAndroid.HIT_TYPES}.
     *
     * @param hitType Indication of the hit type. Must be from {@link GameAndroid.HIT_TYPES}
     */
    private void ballHit(GameAndroid.HIT_TYPES hitType) {
        androidGame.ballHit(hitType);
        String toast_text = "";
        switch (hitType) {
            case SINGLE:
                toast_text = getString(R.string.single_text) + "!";
                break;
            case DOUBLE:
                toast_text = getString(R.string.double_text) + "!";
                break;
            case TRIPLE:
                toast_text = getString(R.string.triple_text) + "!";
                break;
            case HOMERUN:
                toast_text = getString(R.string.homerun_text) + "!";
                break;
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, toast_text);
    }

    /**
     * Click handler for the out type buttons (groundout, flyout). Each button is connected with
     * a specific out type found in {@link GameAndroid.OUT_TYPES}.
     *
     * @param outType Indication of the out type. Must be from {@link GameAndroid.OUT_TYPES}
     */
    private void outMade(GameAndroid.OUT_TYPES outType) {
        androidGame.outMade(outType);
        String toastText = "";
        switch (outType) {
            case GROUNDOUT:
                toastText = getString(R.string.groundout_text);
                break;
            case FLYOUT:
                toastText = getString(R.string.flyout_text);
                break;
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, toastText);
    }

    /**
     * Click handler for cancel buttons.
     * Displays the pitch menu.
     */
    private void cancelClicked() {
        showMenu(pitch_menu);
    }

    /**
     * Click handler for undo button.
     * Undoes the previous game action and displays a toast indicating such.
     */
    private void undoGameAction() {
        String action = androidGame.undoGameAction();
        if (action == null) {
            action = getString(R.string.undo_error_text);
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, getString(R.string.undo_text) + ": " + action);
    }

    /**
     * Click handler for redo button.
     * Redoes the previously undone game action and displays a toast indicating such.
     */
    private void redoGameAction() {
        String action = androidGame.redoGameAction();
        if (action == null) {
            action = getString(R.string.redo_error_text);
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, getString(R.string.redo_text) + ": " + action);
    }

    /**
     * Click handler for undo button in the game over menu.
     * Returns the user to the pitch menu and undoes the previous game action.
     */
    private void undoLastGameAction() {
        showMenu(pitch_menu);
        undoGameAction();
    }

    /**
     * Completes the current game and finishes the activity.
     */
    private void done() {
        androidGame.gameFinished();
        finish();
    }

    /**
     * Click handler for quit button.
     * Starts game saver activity and finishes this activity.
     */
    private void quitGame() {
        Intent intent = new Intent(this, GameSaverActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Updates the game activity menus. This includes updating the appearance of the undo & redo
     * buttons depending on the availability of those actions, as well as showing continue & game
     * over menus.
     */
    private void updateMenu() {
        // Update appearance of undo & redo buttons depending on their availability
        if (pitch_menu.getVisibility() == View.VISIBLE) {
            if (androidGame.undoAvailable())
                undo_button.setBackgroundColor(getResources().getColor(R.color.button_color));
            else
                undo_button.setBackgroundColor(getResources().getColor(R.color.disabled_button_color));

            if (androidGame.redoAvailable())
                redo_button.setBackgroundColor(getResources().getColor(R.color.button_color));
            else
                redo_button.setBackgroundColor(getResources().getColor(R.color.disabled_button_color));
        }

        // Check for game waiting and game over states
        if (androidGame.isWaiting()) {
            String continue_state = androidGame.getWaitingState();
            continue_button.setText(continue_state);
            showMenu(continue_menu);
        } else if (androidGame.isGameOver()) {
            showMenu(gameover_menu);
        }
    }

    /**
     * Displays the given menu to the screen, and hides all others.
     * @param menuToShow Instance of the menu to be shown.
     */
    private void showMenu(View menuToShow) {
        for (View menu : menus) {
            if (menu != menuToShow) {
                menu.setVisibility(View.GONE);
            } else {
                menu.setVisibility(View.VISIBLE);
            }
        }
    }
}