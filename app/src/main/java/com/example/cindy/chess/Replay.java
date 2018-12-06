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

    public void add( Piece[][] b ){

        Piece[][] save = new Piece[8][8];
        for( int i = 0; i < 8; i++ ){
            for( int k = 0; k < 8; k++ ){
                if( b[i][k] == null ){
                    continue;
                }
                save[i][k] = b[i][k];
                /*
                Piece p = b[i][k];
                Piece copy;
                boolean color = p.color;
                String c =(color == true)? "w": "b";
                char type = p.type;
                switch ( type ){
                    case 'N': copy = new Knight(color, c+"N"); break;
                    case 'Q': copy = new Queen(color, c+"Q"); break;
                    case 'K': copy = new King( color, c+"K"); break;
                    case 'R': copy = new Rook( color, c+"R"); break;
                    case 'B': copy = new Bishop(color, c+"B"); break;
                    default: copy = new Pawn(color, c+"p"); break;
                }
                copy.moveYet = p.moveYet;
                save[i][k] = copy;
                */

            }
        }

        System.out.println("~~~~~BEFORE ADD~~~~~~~~~");
        for( int i = 0; i<replay.size(); i++ ){
            print( replay.get(i) );
        }

        replay.add(save);

        System.out.println("~~~~AFTER ADD~~~~~~~~~~~");

        for( int i = 0; i<replay.size(); i++ ){
            print( replay.get(i) );
        }


    }

    public Piece[][] undo(){
        System.out.println("REPLAY.UNDO");
        replay.remove(replay.size()-1);
        Piece[][] b = replay.get(replay.size()-1);
        Piece[][] returnThis = new Piece[8][8];
        for( int i = 0; i < 8; i++ ){
            for( int k = 0; k < 8; k++ ){
                if( b[i][k] == null ){
                    continue;
                }
                returnThis[i][k] = b[i][k];
            }
        }
        return returnThis;
    }

    public int length(){
        return replay.size();
    }
}
