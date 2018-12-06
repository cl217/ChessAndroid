package com.example.cindy.chess;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

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

        System.out.println("Homescreen");
        /*
        System.out.println(path.toPath().toString());
        File[] list = path.listFiles();
        for( int i = 0; i < list.length; i++ ) {
            System.out.println("file: " + list[i].getName());
        }
        */
        for( int i = 0; i < data.allReplays.size(); i++ ){
            System.out.println(data.allReplays.get(i).title);
        }
    }

    private void loadData(){
        try {
            data = data.read(this);
        }catch (IOException e ){
            System.out.println("Broken");
        }catch (ClassNotFoundException e2){
            System.out.println("Beyond Broken");
        }

    }
}
