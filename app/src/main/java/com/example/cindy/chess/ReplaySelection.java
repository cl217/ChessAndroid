package com.example.cindy.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReplaySelection extends AppCompatActivity implements Comparator {
    /*
        data.allReplays hashmap
        key is replay.title.toLowerCase
     */
    Saved data = HomeScreen.data;
    public static String selectedKey;

    private Button backB;
    private ListView replayLV;
    private ArrayList<String> replayList =  new ArrayList<String>();
    private ArrayList<Replay> listOfReplays = new ArrayList<>();
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
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, replayList);
        replayLV.setAdapter(adapter);

        for (Replay r : data.allReplays.values()) {
            replayList.add(r.title + " - " + r.getDate());
            listOfReplays.add(r);
        }

        orderDate();

        Spinner dropDown = findViewById(R.id.dropDown);
        String[] selection = new String[] { "Date", "Alphabetical" };
        ArrayAdapter<String>adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,selection);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter2);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                switch (pos) {
                    case 0:
                        orderDate();
                        replayLV.setAdapter(adapter);
                        break;
                    case 1:
                        orderTitle();
                        replayLV.setAdapter(adapter);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> p) {
                // TODO Auto-generated method stub
            }
        });

        replayLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                ReplaySelection.this.toReplay(pos);
            }
        });
    }

    private void orderDate(){
        /*
             replayList needs to be sorted.
             replay1.date.compareTo(replay2.date)
                can be used to compare dates
                but how to order order in an arrayList?
         */
        Collections.sort(listOfReplays, new Comparator<Replay>() {
            public int compare(Replay r1, Replay r2) {
                if (r1.getDate() == null || r2.getDate() == null)
                    return 0;
                return r1.getDate().compareTo(r2.getDate());
            }
        });

        replayList.clear();
        for (Replay r : listOfReplays) {
            replayList.add(r.title + " - " + r.getDate());
        }
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

    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
}