package com.example.cindy.chess;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Have to make an object to be serialized
 */
public class Replay implements Serializable {
    ArrayList<Move> replay = new ArrayList<>();

    private static final String directory = "data";
    private static final String file = "replays";

    public void addMove(int begin, int end){
        replay.add(new Move(begin, end));
    }

    public static Replay read() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + file));
        Replay replay = (Replay) ois.readObject();
        ois.close();
        return replay;
    }

    public class Move{
        int begin;
        int end;
        public Move( int begin, int end){
            this.begin = begin;
            this.end = end;
        }

    }
}
