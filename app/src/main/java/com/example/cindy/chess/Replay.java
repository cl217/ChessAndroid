package com.example.cindy.chess;



import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class Replay implements Serializable {
    public String title;
    public Date date;
    public String endgame;

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

    public int size(){
        return replay.size();
    }

    public Piece[][] get(int i){
        return replay.get(i).board;
    }
}
