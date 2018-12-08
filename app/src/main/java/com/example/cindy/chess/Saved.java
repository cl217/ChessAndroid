package com.example.cindy.chess;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 */

public class Saved implements Serializable {
    //public ArrayList<Replay> allReplays = new ArrayList<>();
    public HashMap<String,Replay> allReplays = new HashMap<>();

    public void add( Replay replay){
        allReplays.put( replay.title.toLowerCase(),replay);
    }

    public void remove(String key){
        allReplays.remove(key.toLowerCase());
    }
    public boolean isEmpty(){
        return allReplays.isEmpty();
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
            return;
        } catch (IOException e) {
            return;
        }

    }

}
