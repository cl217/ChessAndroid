package com.example.cindy.chess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Saved {
    public static ArrayList<Replay> allReplays = new ArrayList<>();

    public void add( Replay replay){
        allReplays.add(replay);
    }
    public void remove(Replay replay){
        allReplays.remove(replay);
    }
    public boolean isEmpty(){
        return allReplays.isEmpty();
    }
    private static final String directory = "data";
    private static final String file = "replays";

    public static Replay read() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + file));
        Replay replay = (Replay) ois.readObject();
        ois.close();
        return replay;
    }

}
