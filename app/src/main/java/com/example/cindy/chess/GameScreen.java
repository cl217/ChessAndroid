package com.example.cindy.chess;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class GameScreen extends AppCompatActivity {

    private GridLayout boardGrid;
    private TextView displayText;

    private Button undoB;
    private Button drawB;
    private Button resignB;
    private View.OnClickListener buttonListener;

    private int start = -1;
    private int end = -1;
    private boolean turn = true; //white's turn = true, black = false
    private boolean gameEnded = false;

    private Board b = new Board();

    //to be saved for replay
    ArrayList<Board>move = new ArrayList<Board>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        undoB = findViewById(R.id.undoB);
        drawB = findViewById(R.id.drawB);
        resignB = findViewById(R.id.resignB);
        undoB.setOnClickListener(buttonListener);
        drawB.setOnClickListener(buttonListener);
        resignB.setOnClickListener(buttonListener);
        boardGrid = findViewById(R.id.boardGrid);
        displayText = findViewById(R.id.displayText);
        b.initialize();
        displayBoard(b.board);

    }

    private void displayBoard( Piece[][] board ){
        boardGrid.removeAllViews();
        int i = 0;
        for( int y = 0; y < 8; y++ ) {
            for (int x = 0; x < 8; x++) {
                TextView temp = new TextView(this);
                temp.setWidth(73);
                temp.setHeight(73);

                if (board[y][x] != null){
                    temp.setText(board[y][x].name);
                    temp.setTypeface(null, Typeface.BOLD);
                }else{
                    temp.setText("  ");
                }
                temp.setGravity(Gravity.CENTER);

                buttonListener = new View.OnClickListener(){
                    public void onClick (View view){
                        switch (view.getId()) {
                            case R.id.undoB:

                                // do something when undo is clicked

                                break;
                            case R.id.drawB:

                                // do something when draw is clicked

                                break;
                            case R.id.resignB:

                                // do something when resign is clicked

                                break;
                            default:
                                break;
                        }

                    }
                };

                boardGrid.addView(temp, i);
                boardGrid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("index", Integer.toString(boardGrid.indexOfChild(view)));
                        if( gameEnded == true ){
                            return;
                        }
                        int[] coord = convert(boardGrid.indexOfChild(view));
                        if (start != -1) { //already a piece selected
                            boardGrid.getChildAt(start).setBackgroundColor(Color.TRANSPARENT);
                            if( b.board[coord[1]][coord[0]] != null && b.board[coord[1]][coord[0]].color == turn ){
                                start = boardGrid.indexOfChild(view);
                                view.setBackgroundColor(Color.YELLOW);
                            }else {
                                end = boardGrid.indexOfChild(view);
                                int[] moveStart = convert(start);
                                int[] moveEnd = convert(end);
                                if(b.valid(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], '/', turn)){
                                    b.move(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], '/');
                                    displayBoard(b.board);
                                    move.add(new Board(b.board));
                                    turn = !turn;
                                    start = -1;
                                }
                            }
                        } else{
                            if(  b.board[coord[1]][coord[0]] != null && b.board[coord[1]][coord[0]].color == turn ) {
                                start = boardGrid.indexOfChild(view);
                                view.setBackgroundColor(Color.YELLOW);
                            }
                        }
                    }
                });
                i++;
            }
        }
    }

    /*
    private void updateDisplay(){

    }
    */

    private static int[] convert( int gridIndex ) {
        int[] coord = new int[2]; //[x][y]
        int i = 0;
        for( int y = 0; y < 8; y++ ){
            for( int x = 0; x < 8; x++ ){
                if( i == gridIndex ){
                    coord[0] = x;
                    coord[1] = y;
                    return coord;
                }
                i++;
            }
        }

        return coord;
    }
}
