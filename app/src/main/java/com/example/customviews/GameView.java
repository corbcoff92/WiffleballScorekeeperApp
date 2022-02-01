package com.example.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.game.Game;
import com.example.wiffleballscorekeeperapp.R;

public class GameView extends TableLayout {
    private TextView inning_display;
    private TextView home_name_display;
    private TextView home_runs_display;
    private TextView home_hits_display;
    private TextView home_walks_display;
    private TextView away_name_display;
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

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.game_view, this);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.GameView);
        TextView heading_textview = findViewById(R.id.heading_display);
        setHeadingText(attributes.getString(R.styleable.GameView_headingText));
        attributes.recycle();

        home_name_display = findViewById(R.id.home_name_display);
        away_name_display = findViewById(R.id.away_name_display);
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
    }

    public void setHeadingText(String headingText) {
        TextView heading_display = findViewById(R.id.heading_display);
        heading_display.setText(headingText);
        invalidate();
        requestLayout();
    }

    public void displayGame(Game game) {
        String inning_text = (game.isTopInning() ? "TOP " : "BOT ") + game.getInning();
        inning_display.setText(inning_text);
        home_name_display.setText(game.getHomeName());
        away_name_display.setText(game.getAwayName());
        home_runs_display.setText(Integer.toString(game.getHomeRuns()));
        home_hits_display.setText(Integer.toString(game.getHomeHits()));
        home_walks_display.setText(Integer.toString(game.getHomeWalks()));
        away_runs_display.setText(Integer.toString(game.getAwayRuns()));
        away_hits_display.setText(Integer.toString(game.getAwayHits()));
        away_walks_display.setText(Integer.toString(game.getAwayWalks()));
        count_display.setText(getResources().getString(R.string.count_text, game.getBalls(), game.getStrikes()));
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

        invalidate();
        requestLayout();
    }
}
