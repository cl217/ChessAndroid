package com.example.cindy.chess;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Have to make an object to be serialized
 */
public class Replay implements Serializable {
    public String title;
    public Date date;

    ArrayList<Board> replay = new ArrayList<>();



    public String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return format.format(date);
    }

    public void add( Board b ){

        Board addThis = new Board();
        Piece[][] save = new Piece[8][8];
        for( int i = 0; i < 8; i++ ){
            for( int k = 0; k < 8; k++ ) {
                if (b.board[i][k] == null) {
                    continue;
                }
                save[i][k] = b.board[i][k];
                //save[i][k].moveYet = b.board[i][k].moveYet; doesn't fix castling
            }
        }
        addThis.board = save;
        addThis.prevType = b.prevType;
        addThis.prevX1 = b.prevX1;
        addThis.prevX2 = b.prevX2;
        addThis.prevY1 = b.prevY1;
        addThis.prevY2 = b.prevY2;
        replay.add(addThis);

    }

    public Board undo(){
        System.out.println("REPLAY.UNDO");
        char prevT = replay.get(replay.size()-1).prevType;
        boolean prevC = replay.get(replay.size()-1).getColor(replay.get(replay.size()-1).prevX2,replay.get(replay.size()-1).prevY2);
        replay.remove(replay.size()-1);
        Board b = replay.get(replay.size()-1);
        Board returnThis = new Board();
        for( int i = 0; i < 8; i++ ){
            for( int k = 0; k < 8; k++ ){
                if( b.board[i][k] == null ){
                    continue;
                }
                returnThis.board[i][k] = b.board[i][k];
                if(returnThis.board[i][k].type == prevT && returnThis.board[i][k].color == prevC){
                    returnThis.board[i][k].moveYet = false;
                }
            }
        }
        returnThis.prevType = b.prevType;
        returnThis.prevX1 = b.prevX1;
        returnThis.prevX2 = b.prevX2;
        returnThis.prevY1 = b.prevY1;
        returnThis.prevY2 = b.prevY2;

        return returnThis;
    }

    public int length(){
        return replay.size();
    }

    /*
    public Replay read(Context context, String name) throws IOException, ClassNotFoundException {
        File path = context.getFilesDir();
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream(path + File.separator + name));
        Replay replay = (Replay) ois.readObject();
        ois.close();
        return replay;
    }
    */
}
