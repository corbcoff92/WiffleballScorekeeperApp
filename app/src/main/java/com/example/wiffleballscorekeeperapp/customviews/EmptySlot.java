package com.example.wiffleballscorekeeperapp.customviews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.wiffleballscorekeeperapp.R;

/**
 * Custom {@code View} used for displaying an empty saved game slot.
 */
public class EmptySlot extends CardView {
    public EmptySlot(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.empty_slot, this);
    }

    /**
     * Used for setting the text identifying the empty saved game slot.
     * @param slotText String that should be displayed to indicate the
     *                 empty saved game slot.
     */
    public void setText(String slotText) {
        TextView textView = findViewById(R.id.slot_textview);
        textView.setText(slotText);
    }
}