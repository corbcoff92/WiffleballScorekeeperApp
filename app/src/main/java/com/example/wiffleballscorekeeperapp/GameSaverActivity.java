package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.game.GameAndroid;

public class GameSaverActivity extends AppCompatActivity {
    public final static String SAVE_SLOT_KEY_FORMAT = "GAME_SLOT_%d";
    private final GameAndroid gameAndroid = GameAndroid.getInstance();
    private int slotNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_saver);

        findViewById(R.id.button_save).setOnClickListener(view -> saveGame());
        findViewById(R.id.button_dont_save).setOnClickListener(view -> gameNotSaved());
        findViewById(R.id.textview_save_slot1).setOnClickListener(view -> selectSlotNum(1));
        findViewById(R.id.textview_save_slot2).setOnClickListener(view -> selectSlotNum(2));
        findViewById(R.id.textview_save_slot3).setOnClickListener(view -> selectSlotNum(3));
    }

    private void selectSlotNum(int slotNum) {
        this.slotNum = slotNum;
        makeToast(String.format("Slot %d selected", slotNum));
    }

    private void saveGame() {
        if (slotNum >= 1 && slotNum <= 3) {
            SharedPreferences sharedPreferences = getSharedPreferences("game_slots", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String gameJson = gameAndroid.getCurrentGameJson();
            editor.putString(String.format(SAVE_SLOT_KEY_FORMAT, slotNum), gameJson);
            editor.apply();
            makeToast(String.format("Game saved to slot %d", slotNum));
            finish();
        } else {
            makeToast("Please first select a game slot...");
        }
    }

    private void gameNotSaved()
    {
        makeToast("Game not saved...");
        finish();
    }

    private void makeToast(String toastText)
    {
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}