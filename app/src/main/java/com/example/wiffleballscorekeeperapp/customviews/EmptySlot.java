package com.example.wiffleballscorekeeperapp.customviews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.wiffleballscorekeeperapp.R;

public class EmptySlot extends CardView {
    public EmptySlot(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.empty_slot, this);
    }

    public void setText(String slotText) {
        TextView textView = findViewById(R.id.slot_textview);
        textView.setText(slotText);
    }
}
