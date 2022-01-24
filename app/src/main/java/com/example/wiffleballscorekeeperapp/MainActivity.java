package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.game.Game;

public class MainActivity extends AppCompatActivity {
    private Game game = new Game(2, "HOME", "AWAY");
    private TextView inning_display;
    private TextView home_runs_display;
    private TextView home_hits_display;
    private TextView home_walks_display;
    private TextView away_runs_display;
    private TextView away_hits_display;
    private TextView away_walks_display;
    private TextView count_display;
    private TextView runners_display;
    private TextView message_display;
    private View pitch_menu;
    private View hit_menu;
    private View out_menu;
    final private String LOG_TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inning_display = findViewById(R.id.inning_display);
        home_runs_display = findViewById(R.id.home_runs_display);
        home_hits_display = findViewById(R.id.home_hits_display);
        home_walks_display = findViewById(R.id.home_walks_display);
        away_runs_display = findViewById(R.id.away_runs_display);
        away_hits_display = findViewById(R.id.away_hits_display);
        away_walks_display = findViewById(R.id.away_walks_display);
        runners_display = findViewById(R.id.runners_display);
        count_display = findViewById(R.id.count_display);
        message_display = findViewById(R.id.message_display);

        pitch_menu = findViewById(R.id.pitch_buttons);
        hit_menu = findViewById(R.id.hit_buttons);
        out_menu = findViewById(R.id.out_buttons);

        findViewById(R.id.ball_button).setOnClickListener((View view) -> {
            game.callBall();
            checkGameOver();
            displayGame();
        });
        findViewById(R.id.strike_button).setOnClickListener((View view) -> {
            game.callStrike();
            checkGameOver();
            displayGame();
        });


        findViewById(R.id.hit_button).setOnClickListener((View view) ->{
            showMenu(hit_menu);
        });

        findViewById(R.id.single_button).setOnClickListener((View view) -> {
            game.advanceRunnersHit(1);
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });
        findViewById(R.id.double_button).setOnClickListener((View view) -> {
            game.advanceRunnersHit(2);
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });
        findViewById(R.id.triple_button).setOnClickListener((View view) -> {
            game.advanceRunnersHit(3);
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });
        findViewById(R.id.homerun_button).setOnClickListener((View view) -> {
            game.advanceRunnersHit(4);
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });

        findViewById(R.id.cancel_hit_button).setOnClickListener((View view) -> cancelClicked(view));

        findViewById(R.id.out_button).setOnClickListener((View view) -> {
            showMenu(out_menu);
        });
        findViewById(R.id.flyout_button).setOnClickListener((View view) -> {
            game.flyOut();
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });
        findViewById(R.id.groundout_button).setOnClickListener((View view) -> {
            game.groundOut();
            showMenu(pitch_menu);
            checkGameOver();
            displayGame();
        });

        findViewById(R.id.cancel_out_button).setOnClickListener((View view) -> cancelClicked(view));

        displayGame();
    }

    public void checkGameOver()
    {
        if (game.isGameOver)
        {
            pitch_menu.setVisibility(View.GONE);
            findViewById(R.id.gameover_buttons).setVisibility(View.VISIBLE);
        }
    }

    public void cancelClicked(View view)
    {
        showMenu(pitch_menu);
    }

    public void showMenu(View menu)
    {
        if (pitch_menu != menu) pitch_menu.setVisibility(View.GONE); else pitch_menu.setVisibility(View.VISIBLE);
        if (hit_menu != menu) hit_menu.setVisibility(View.GONE); else hit_menu.setVisibility(View.VISIBLE);
        if (out_menu != menu) out_menu.setVisibility(View.GONE); else out_menu.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void displayGame() {
        String inning_text = (game.isTopInning() ?  "TOP " : "BOT ") + game.getInning();
        char runner_on_first = (game.isRunnerOnBase(1) ? 'X' : 'O');
        char runner_on_second = (game.isRunnerOnBase(2) ? 'X' : 'O');
        char runner_on_third = (game.isRunnerOnBase(3) ? 'X' : 'O');

        inning_display.setText(inning_text);
        home_runs_display.setText(Integer.toString(game.getHomeRuns()));
        home_hits_display.setText(Integer.toString(game.getHomeHits()));
        home_walks_display.setText(Integer.toString(game.getHomeWalks()));
        away_runs_display.setText(Integer.toString(game.getAwayRuns()));
        away_hits_display.setText(Integer.toString(game.getAwayHits()));
        away_walks_display.setText(Integer.toString(game.getAwayWalks()));
        count_display.setText(getString(R.string.count_text, game.getBalls(), game.getStrikes(), game.getOuts()));
        runners_display.setText(String.format("   %c   \n%c     %c\n   O   ", runner_on_second, runner_on_third, runner_on_first));
        message_display.setText(game.getMessage());
    }
}