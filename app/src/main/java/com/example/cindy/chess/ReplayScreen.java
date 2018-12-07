package com.example.cindy.chess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ReplayScreen extends AppCompatActivity {

    Replay replay;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_play_screen);
        replay = HomeScreen.data.allReplays.get(ReplaySelection.selectedKey.toLowerCase());
    }
}
