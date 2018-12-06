package com.example.cindy.chess;


import android.content.Context;
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
    public String title;
    public LocalDate date;

    ArrayList<Board> replay = new ArrayList<>();

    public void print( Piece[][] board ) {
        int colDis = 8;
        for( int i = 0; i < 8; i++ ) {
            for( int k = 0; k < 8; k++ ) {
                if( board[i][k] != null ) {
                    System.out.print(board[i][k].name);
                    System.out.print(" ");
                }else {
                    if( i % 2 == 0 ) {
                        if( k % 2 == 0) {
                            System.out.print("   ");
                        }else {
                            System.out.print("## ");
                        }

                    }else {
                        if( k % 2 == 0) {
                            System.out.print("## ");
                        }else {
                            System.out.print("   ");
                        }
                    }
                }
            }
            System.out.println(colDis);
            //System.out.println(" " + i);
            colDis--;
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println("                         ");
        System.out.println("                         ");
        //System.out.println(" 0  1  2  3  4  5  6  7 ");
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
        replay.remove(replay.size()-1);
        Board b = replay.get(replay.size()-1);
        Board returnThis = new Board();
        for( int i = 0; i < 8; i++ ){
            for( int k = 0; k < 8; k++ ){
                if( b.board[i][k] == null ){
                    continue;
                }
                returnThis.board[i][k] = b.board[i][k];
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
