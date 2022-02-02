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

        gameDisplay = findViewById(R.id.game_display);
        gameDisplay.setHeadingText(getString(R.string.game_heading_text, androidGame.getNumInnings()));

        pitch_menu = findViewById(R.id.pitch_buttons);
        hit_menu = findViewById(R.id.hit_buttons);
        out_menu = findViewById(R.id.out_buttons);
        continue_menu = findViewById(R.id.continue_buttons);
        gameover_menu = findViewById(R.id.gameover_buttons);

        menus.add(pitch_menu);
        menus.add(hit_menu);
        menus.add(out_menu);
        menus.add(continue_menu);
        menus.add(gameover_menu);

        undo_button = findViewById(R.id.undo_button);
        redo_button = findViewById(R.id.redo_button);
        continue_button = findViewById(R.id.continue_button);

        findViewById(R.id.continue_button).setOnClickListener((View view) -> {
            androidGame.continueGame();
            showMenu(pitch_menu);
            updateMenu();
            gameDisplay.displayGame(androidGame.getCurrentGame());
        });

        findViewById(R.id.ball_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.BALL));
        findViewById(R.id.strike_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.STRIKE));

        findViewById(R.id.hit_button).setOnClickListener((View view) -> showMenu(hit_menu));
        findViewById(R.id.single_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.SINGLE));
        findViewById(R.id.double_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.DOUBLE));
        findViewById(R.id.triple_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.TRIPLE));
        findViewById(R.id.homerun_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.HOMERUN));
        findViewById(R.id.cancel_hit_button).setOnClickListener(this::cancelClicked);

        findViewById(R.id.out_button).setOnClickListener((View view) -> showMenu(out_menu));
        findViewById(R.id.flyout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.FLYOUT));
        findViewById(R.id.groundout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.GROUNDOUT));
        findViewById(R.id.cancel_out_button).setOnClickListener(this::cancelClicked);

        findViewById(R.id.undo_button).setOnClickListener((View view) -> undoGameAction());
        findViewById(R.id.redo_button).setOnClickListener((View view) -> redoGameAction());

        findViewById(R.id.done_button).setOnClickListener((View view) -> done());
        findViewById(R.id.quit_button).setOnClickListener((View view) -> quitGame());

        findViewById(R.id.gameover_undo_button).setOnClickListener((View view) -> {
            showMenu(pitch_menu);
            undoGameAction();
        });

        if (savedInstanceState != null) {
            int shownMenuID = savedInstanceState.getInt("shown_menu_id");
            View menuToShow = findViewById(shownMenuID);
            showMenu(menuToShow);
            if (menuToShow == pitch_menu || androidGame.isWaiting()) {
                updateMenu();
            }
        }

        gameDisplay.displayGame(androidGame.getCurrentGame());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (View menu : menus) {
            if (menu.getVisibility() == View.VISIBLE) {
                outState.putInt("shown_menu_id", menu.getId());
            }
        }
    }

    public void pitchCalled(GameAndroid.PITCH_CALL_TYPES callType) {
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

    private void undoGameAction() {
        String action = androidGame.undoGameAction();
        if (action == null) {
            action = getString(R.string.undo_error_text);
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, getString(R.string.undo_text) + ": " + action);
    }

    private void redoGameAction() {
        String action = androidGame.redoGameAction();
        if (action == null) {
            action = getString(R.string.redo_error_text);
        }
        updateMenu();
        gameDisplay.displayGame(androidGame.getCurrentGame());
        GameAndroid.makeToast(this, getString(R.string.redo_text) +": " + action);
    }

    private void updateMenu() {
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

        if (androidGame.isWaiting()) {
            String continue_state = androidGame.getWaitingState();
            continue_button.setText(continue_state);
            showMenu(continue_menu);
        } else if (androidGame.isGameOver()) {
            showMenu(gameover_menu);
        }
    }

    public void cancelClicked(View view) {
        showMenu(pitch_menu);
        updateMenu();
    }

    public void showMenu(View menuToShow) {
        for (View menu : menus) {
            if (menu != menuToShow) {
                menu.setVisibility(View.GONE);
            } else {
                menu.setVisibility(View.VISIBLE);
            }
        }
    }

    public void done()
    {
        androidGame.gameFinished();
        finish();
    }

    public void quitGame()
    {
        Intent intent = new Intent(this, GameSaverActivity.class);
        startActivity(intent);
        finish();
    }
}