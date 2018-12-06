package com.example.cindy.chess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ReplaySelection extends AppCompatActivity {

    Saved data = HomeScreen.data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_select_screen);

        if( data.isEmpty() ){
            System.out.println("empty");
        }
        for( int i = 0; i<data.allReplays.size(); i++){
            System.out.println(data.allReplays.get(i).title);
        }
    }
}
