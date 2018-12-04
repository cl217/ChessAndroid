package com.example.cindy.chess;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Have to make an object to be serialized
 */
public class Replay implements Serializable {
    String title;
    LocalDate date;

    ArrayList<Board> replay = new ArrayList<>();

    public void add( Board b ){
        replay.add(b);
    }

    public Board get( int i ){
        return replay.get(i);
    }

    public int length(){
        return replay.size();
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
