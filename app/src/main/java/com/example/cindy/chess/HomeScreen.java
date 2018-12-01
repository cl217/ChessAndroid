package com.example.cindy.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    private Button newGameB;
    private Button replayB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        newGameB = findViewById(R.id.newGameB);
        replayB = findViewById(R.id.replayB);

        newGameB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                Intent intent = new Intent(HomeScreen.this, GameScreen.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
