package com.example.cindy.chess;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.IOException;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class HomeScreen extends AppCompatActivity {

    private Button newGameB;
    private Button replayB;
    public static Saved data = new Saved();
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        newGameB = findViewById(R.id.newGameB);
        replayB = findViewById(R.id.replayB);

        context = this;
        loadData();

        newGameB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomeScreen.this, GameScreen.class);
                startActivity(intent);
            }
        });

        replayB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomeScreen.this, ReplaySelection.class);
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        try {
            data = data.read(this);
        }catch (IOException e ){
            return;
            //System.out.println("Broken");
        }catch (ClassNotFoundException e2){
            return;
            //System.out.println("Beyond Broken");
        }

    }
}
