package com.example.wiffleballscorekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.game.GameAndroid;

public class GameLoaderActivity extends AppCompatActivity {
    private final GameAndroid gameAndroid = GameAndroid.getInstance();
    private int slotNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loader);

        findViewById(R.id.button_load).setOnClickListener(view -> loadGame());
        findViewById(R.id.button_cancel_load).setOnClickListener(view -> finish());
        findViewById(R.id.textview_load_slot1).setOnClickListener(view -> selectSlotNum(1));
        findViewById(R.id.textview_load_slot2).setOnClickListener(view -> selectSlotNum(2));
        findViewById(R.id.textview_load_slot3).setOnClickListener(view -> selectSlotNum(3));
    }

    private void selectSlotNum(int slotNum) {
        this.slotNum = slotNum;
        makeToast(String.format("Slot %d selected", slotNum));
    }

    private void loadGame() {
        if (slotNum >= 1 && slotNum <= 3) {
            SharedPreferences sharedPreferences = getSharedPreferences("game_slots", MODE_PRIVATE);
            String gameJson = sharedPreferences.getString(String.format(GameSaverActivity.SAVE_SLOT_KEY_FORMAT, slotNum), null);
            if (gameJson != null) {
                gameAndroid.loadGameFromJson(gameJson);
                makeToast(String.format("Game loaded from slot %d", slotNum));
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                finish();
            } else {
                makeToast(String.format("Game slot %d is empty...", slotNum));
            }
        } else {
            makeToast("Please first select a game slot...");
        }
    }

    private void makeToast(String toastText) {
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}