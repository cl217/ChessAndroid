package com.example.cindy.chess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
    private ArrayList<Replay> listOfReplays = new ArrayList<>();
    private int longClickedPos = 0;
    private boolean titleOrder = true;
    //private String[] replayList;
    private ArrayAdapter<String> adapter;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, replayList);
        replayLV.setAdapter(adapter);

        for (Replay r : data.allReplays.values()) {
            replayList.add(r.title + " - " + r.getDate());
            listOfReplays.add(r);
        }

        Spinner dropDown = findViewById(R.id.dropDown);
        String[] selection = new String[] { "Name", "Date" };
        ArrayAdapter<String>adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,selection);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter2);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                switch (pos) {
                    case 0:
                        orderTitle();
                        titleOrder = true;
                        break;
                    case 1:
                        orderDate();
                        titleOrder = false;
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

        replayLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> p, View v, int pos, long id){
                longClickedPos = pos;
                deletePopup();
                return true;
            }
        });

    }

    private void orderDate(){
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
        replayLV.setAdapter(adapter);
    }

    //alphabetical order
    private void orderTitle() {
        Collections.sort(replayList, String.CASE_INSENSITIVE_ORDER); //USE TO SORT ALPHABETICALLY
        replayLV.setAdapter(adapter);
    }

    private void toReplay(int pos) {
        selectedKey = getKey(replayList.get(pos));
        Intent intent = new Intent(this, ReplayScreen.class);
        startActivity(intent);
    }

    private String getKey(String str){
        return str.substring(0, str.length()-22);
    }


    private void deletePopup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this replay?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.remove(getKey(replayList.get(longClickedPos)));
                replayList.remove(longClickedPos);
                listOfReplays.remove(longClickedPos);
                replayLV.setAdapter(adapter);
                data.writeData(HomeScreen.context);
                if(titleOrder == true ){
                    orderTitle();
                }else{
                    orderDate();
                }
            }
        });
        builder.show();
    }
}