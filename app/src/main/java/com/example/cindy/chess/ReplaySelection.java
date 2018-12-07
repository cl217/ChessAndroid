package com.example.cindy.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

public class ReplaySelection extends AppCompatActivity {
    /*
        data.allReplays hashmap
        key is replay.title.toLowerCase
     */
    Saved data = HomeScreen.data;
    public static String selectedKey;

    private ListView replayLV;
    private ArrayList<String> replayList =  new ArrayList<String>();
    //private String[] replayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_select_screen);

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