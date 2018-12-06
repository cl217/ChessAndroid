package com.example.cindy.chess;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Saved implements Serializable {
    public ArrayList<Replay> allReplays = new ArrayList<>();

    public void add( Replay replay){
        allReplays.add(replay);
        System.out.println("IN SAVED");
        for(int i = 0; i < allReplays.size(); i++ ){
            System.out.println(i+": ");
            System.out.println(allReplays.get(i).title);
        }
    }
    public void remove(Replay replay){
        allReplays.remove(replay);
    }
    public boolean isEmpty(){
        return allReplays.isEmpty();
    }


    public Saved (){
        Replay replay = new Replay();
        replay.title = "ignore this"; //so serialization doesnt crash
        allReplays.add(replay);
    }
    public static Saved read(Context context) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(context.openFileInput("replaylist.txt"));
        Saved save = (Saved) ois.readObject();
        ois.close();
        return save;
    }
    public void writeData(Context context){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput("replaylist.txt", Context.MODE_PRIVATE));
            oos.writeObject(this);
            oos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
