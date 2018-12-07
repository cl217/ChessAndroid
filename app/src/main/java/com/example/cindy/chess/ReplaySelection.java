package com.example.cindy.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

public class ReplaySelection extends AppCompatActivity {
    /*
        data.allReplays hashmap
        key is replay.title.toLowerCase
     */
    Saved data = HomeScreen.data;
    public static String selectedKey;

    private Button backB;
    private ListView replayLV;
    private ArrayList<String> replayList =  new ArrayList<String>();
    //private String[] replayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_select_screen);

        backB = findViewById(R.id.backBS);
        backB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReplaySelection.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        replayLV = findViewById(R.id.replayLV);

        //routeNames = getResources().getStringArray(R.array.routes_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, replayList);
        replayLV.setAdapter(adapter);

        for (Replay r : data.allReplays.values()) {
            replayList.add(r.title + " - " + r.getDate());
        }

        orderTitle();

        replayLV.setOnItemClickListener((p,v,pos,id) -> toReplay(pos));
    }

    private void orderDate(){
        /*
             replayList needs to be sorted.
             replay1.date.compareTo(replay2.date)
                can be used to compare dates
                but how to order order in an arrayList?
         */
    }

    //alphabetical order
    private void orderTitle() {
        Collections.sort(replayList, String.CASE_INSENSITIVE_ORDER); //USE TO SORT ALPHABETICALLY
    }

    private void toReplay(int pos) {
        selectedKey = getKey(replayList.get(pos));
        Intent intent = new Intent(this, ReplayScreen.class);
        startActivity(intent);
    }

    private String getKey(String str){
        return str.substring(0, str.length()-22);
    }

}