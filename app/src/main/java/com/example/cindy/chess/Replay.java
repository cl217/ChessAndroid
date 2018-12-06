package com.example.cindy.chess;


import android.util.Log;

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

    ArrayList<Piece[][]> replay = new ArrayList<>();

    public void add( Piece[][] b ){
        Piece[][]addThis = new Piece[8][8];
        addThis = b;
        replay.add(addThis);
    }

    public Piece[][] undo(){
        replay.remove(replay.size()-1);
        return replay.get(replay.size()-1);
    }

    public int length(){
        return replay.size();
    }
}
