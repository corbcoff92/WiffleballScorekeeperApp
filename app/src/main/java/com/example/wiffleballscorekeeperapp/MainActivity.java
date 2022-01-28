package com.example.wiffleballscorekeeperapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.GameAndroid;

public class MainActivity extends AppCompatActivity {
    final private String LOG_TAG = "MAIN_ACTIVITY";

    private GameAndroid game;

    private TextView inning_display;
    private TextView home_runs_display;
    private TextView home_hits_display;
    private TextView home_walks_display;
    private TextView away_runs_display;
    private TextView away_hits_display;
    private TextView away_walks_display;
    private TextView count_display;
    private ImageView runner_first_display;
    private ImageView runner_second_display;
    private ImageView runner_third_display;
    private ImageView out_1_display;
    private ImageView out_2_display;
    private ImageView out_3_display;
    private TextView message_display;
    private View pitch_menu;
    private View hit_menu;
    private View out_menu;
    private Button undo_button;
    private Button redo_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new GameAndroid(2, "HOME", "AWAY");

        inning_display = findViewById(R.id.inning_display);
        home_runs_display = findViewById(R.id.home_runs_display);
        home_hits_display = findViewById(R.id.home_hits_display);
        home_walks_display = findViewById(R.id.home_walks_display);
        away_runs_display = findViewById(R.id.away_runs_display);
        away_hits_display = findViewById(R.id.away_hits_display);
        away_walks_display = findViewById(R.id.away_walks_display);
        count_display = findViewById(R.id.count_display);
        message_display = findViewById(R.id.message_display);
        runner_first_display = findViewById(R.id.runner_first_display);
        runner_second_display = findViewById(R.id.runner_second_display);
        runner_third_display = findViewById(R.id.runner_third_display);
        out_1_display = findViewById(R.id.out_1_display);
        out_2_display = findViewById(R.id.out_2_display);
        out_3_display = findViewById(R.id.out_3_display);

        pitch_menu = findViewById(R.id.pitch_buttons);
        hit_menu = findViewById(R.id.hit_buttons);
        out_menu = findViewById(R.id.out_buttons);

        undo_button = findViewById(R.id.undo_button);
        redo_button = findViewById(R.id.redo_button);


        findViewById(R.id.ball_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.BALL));
        findViewById(R.id.strike_button).setOnClickListener((View view) -> pitchCalled(GameAndroid.PITCH_CALL_TYPES.STRIKE));

        findViewById(R.id.hit_button).setOnClickListener((View view) -> showMenu(hit_menu));
        findViewById(R.id.single_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.SINGLE));
        findViewById(R.id.double_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.DOUBLE));
        findViewById(R.id.triple_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.TRIPLE));
        findViewById(R.id.homerun_button).setOnClickListener((View view) -> ballHit(GameAndroid.HIT_TYPES.HOMERUN));
        findViewById(R.id.cancel_hit_button).setOnClickListener((View view) -> cancelClicked(view));

        findViewById(R.id.out_button).setOnClickListener((View view) -> showMenu(out_menu));
        findViewById(R.id.flyout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.FLYOUT));
        findViewById(R.id.groundout_button).setOnClickListener((View view) -> outMade(GameAndroid.OUT_TYPES.GROUNDOUT));
        findViewById(R.id.cancel_out_button).setOnClickListener((View view) -> cancelClicked(view));

        findViewById(R.id.undo_button).setOnClickListener((View view) -> undoGameAction());
        findViewById(R.id.redo_button).setOnClickListener((View view) -> redoGameAction());

        findViewById(R.id.done_button).setOnClickListener((View view) -> finish());

        findViewById(R.id.gameover_undo_button).setOnClickListener((View view) -> {
            undoGameAction();
            findViewById(R.id.gameover_buttons).setVisibility(View.GONE);
            showMenu(pitch_menu);
        });

        displayGame();
    }

    public void pitchCalled(GameAndroid.PITCH_CALL_TYPES callType) {
        game.pitchCalled(callType);
        String toastText = "";
        switch (callType) {
            case BALL:
                toastText = "Ball";
                break;
            case STRIKE:
                toastText = "Strike";
                break;
        }
        displayGame();
        makeToast(toastText);
    }

    private void ballHit(GameAndroid.HIT_TYPES hitType) {
        game.ballHit(hitType);
        String toast_text = "";
        switch (hitType) {
            case SINGLE:
                toast_text = "Single!";
                break;
            case DOUBLE:
                toast_text = "Double!";
                break;
            case TRIPLE:
                toast_text = "Triple!";
                break;
            case HOMERUN:
                toast_text = "Homerun!";
                break;
        }
        displayGame();
        makeToast(toast_text);
    }

    private void outMade(GameAndroid.OUT_TYPES outType) {
        game.outMade(outType);
        String toastText = "";
        switch (outType) {
            case GROUNDOUT:
                toastText = "Groundout";
                break;
            case FLYOUT:
                toastText = "Flyout";
                break;
        }
        displayGame();
        makeToast(toastText);
    }

    private void undoGameAction() {
        String action = game.undoGameAction();
        if (action == null) {
            action = "Cannot undo further...";
        } else {
            updateMenu();
        }
        makeToast("Undo: " + action);
    }

    private void redoGameAction() {
        String action = game.redoGameAction();
        if (action == null) {
            action = "Cannot redo further...";
        } else {
            updateMenu();
        }
        makeToast("Redo: " + action);
    }

    private void updateMenu() {
        if (game.undoAvailable())
            undo_button.setBackgroundColor(getResources().getColor(R.color.button_color));
        else
            undo_button.setBackgroundColor(getResources().getColor(R.color.disabled_button_color));
        if (game.redoAvailable())
            redo_button.setBackgroundColor(getResources().getColor(R.color.button_color));
        else
            redo_button.setBackgroundColor(getResources().getColor(R.color.disabled_button_color));
    }

    public void cancelClicked(View view) {
        showMenu(pitch_menu);
    }

    public void showMenu(View menu) {
        if (pitch_menu != menu) pitch_menu.setVisibility(View.GONE);
        else pitch_menu.setVisibility(View.VISIBLE);
        if (hit_menu != menu) hit_menu.setVisibility(View.GONE);
        else hit_menu.setVisibility(View.VISIBLE);
        if (out_menu != menu) out_menu.setVisibility(View.GONE);
        else out_menu.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void displayGame() {
        updateMenu();

        String inning_text = (game.isTopInning() ? "TOP " : "BOT ") + game.getInning();
        inning_display.setText(inning_text);
        home_runs_display.setText(Integer.toString(game.getHomeRuns()));
        home_hits_display.setText(Integer.toString(game.getHomeHits()));
        home_walks_display.setText(Integer.toString(game.getHomeWalks()));
        away_runs_display.setText(Integer.toString(game.getAwayRuns()));
        away_hits_display.setText(Integer.toString(game.getAwayHits()));
        away_walks_display.setText(Integer.toString(game.getAwayWalks()));
        count_display.setText(getString(R.string.count_text, game.getBalls(), game.getStrikes()));
        message_display.setText(game.getMessage());

        int[] runners = new int[3];
        int[] outs = new int[3];
        int numOuts = game.getOuts();
        for (int i = 0; i < 3; i++) {
            if (!game.isRunnerOnBase(i + 1))
                runners[i] = R.drawable.base_empty;
            else
                runners[i] = R.drawable.base_occupied;
            if (numOuts < i + 1)
                outs[i] = R.drawable.out_empty;
            else
                outs[i] = R.drawable.out_full;
        }
        runner_first_display.setImageResource(runners[0]);
        runner_second_display.setImageResource(runners[1]);
        runner_third_display.setImageResource(runners[2]);
        out_1_display.setImageResource(outs[0]);
        out_2_display.setImageResource(outs[1]);
        out_3_display.setImageResource(outs[2]);
    }

    private void makeToast(String toast_text) {
        Toast toast = Toast.makeText(this, toast_text, Toast.LENGTH_SHORT);
        toast.show();
    }
}