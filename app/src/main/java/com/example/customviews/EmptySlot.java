package com.example.customviews;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.wiffleballscorekeeperapp.R;

public class EmptySlot extends CardView {
    public EmptySlot(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.empty_slot, this);

    }
}
