package com.example.cindy.chess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.content.Intent;

public class GameScreen extends AppCompatActivity {

   private GridLayout boardGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        Bundle bundle = getIntent().getExtras();

        //start board
    }


    private void startBoard(){
        //fill board with buttons
    }
}
