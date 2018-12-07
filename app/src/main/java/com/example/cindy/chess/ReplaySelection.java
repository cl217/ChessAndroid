package com.example.cindy.chess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ReplaySelection extends AppCompatActivity {

    /*
        data.allReplays hashmap
        key is replay.title.toLowerCase
     */
    Saved data = HomeScreen.data;
    private ListView replayLV;
    private ArrayList<String> replayList =  new ArrayList<String>();
    //private String[] replayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_select_screen);

        replayLV = findViewById(R.id.replayLV);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, replayList );
        replayLV.setAdapter(arrayAdapter);

        updateList();
    }

    //alphabetical
    public void updateList() {
        for (Replay r : data.allReplays.values()) {
            replayList.add(r.title);
        }
        Collections.sort(replayList, String.CASE_INSENSITIVE_ORDER); //USE TO SORT ALPHABETICALLY

    }


}